import { Test } from '@nestjs/testing';
import { INestApplication } from '@nestjs/common';
import * as request from 'supertest';
import { AppModule } from './../src/app.module';
import { Comment } from '../src/app.entity';
import { TypeOrmModule } from '@nestjs/typeorm/dist/typeorm.module';
import { Repository } from 'typeorm';

let app: INestApplication;
let repository : Repository<Comment>

beforeAll(async () => {
  const module= await Test.createTestingModule({
    imports: [
      AppModule,
      TypeOrmModule.forRoot({
        type: 'sqlite',
        database: 'testCommentDb.db',
        entities: [Comment],
        synchronize: false,
      }),
      TypeOrmModule.forFeature([Comment])
    ]
  }).compile();

  app = module.createNestApplication();
  await app.init();
  repository = module.get('CommentRepository');
});

afterAll(async () => {
  await app.close();
});

afterEach(async () => {
  await repository.query(`DELETE FROM comment;`);
});

  //-------------------------------------- TESTS --------------------------
describe('POST /poll', () => {
  it('should create a comment in db', async () => {
    const { body } = await request.agent(app.getHttpServer())
      .post('/poll/')
      .send({pollId : 1, content :"un commentaire", auteur :"francois"})
      .expect(201) //201 = created

    expect(body).toMatchObject(
      {pollId : 1, content : "un commentaire", auteur: "francois" }
    );
  });
});

describe('GET /poll/2', () => {
  it('should return all comments from poll 2', async () => {

    await repository.save([
      { pollId : 2, content : "petit comm", auteur : "patrick" },
      { pollId : 2, content : "autre petit comm", auteur : "jack" },
    ]);

    const { body } = await request.agent(app.getHttpServer())
      .get('/poll/2')
      .expect('Content-Type', /json/)
      .expect(200)
    
    expect(body).toMatchObject([
      { id : expect.any(Number), pollId : 2, content : "petit comm", auteur: 'patrick' },
      { id : expect.any(Number), pollId : 2, content : "autre petit comm", auteur: 'jack' }
    ]);
  });
});

describe('DELETE /poll/2', () => {
  it('should delete poll 2 and return true', async () => {

    await request.agent(app.getHttpServer())
      .delete('/poll/2')
      .expect(200)

    const { body } = await request.agent(app.getHttpServer())
      .get('/poll/2')
      .expect('Content-Type', /json/)
      .expect(200)
      .expect('[]')
  });
});
