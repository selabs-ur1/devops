import { Test } from '@nestjs/testing';
import { INestApplication } from '@nestjs/common';
import * as request from 'supertest';
import { AppModule } from './../src/app.module';
import { Chat } from '../src/chats/chat.entity';
import { TypeOrmModule } from '@nestjs/typeorm/dist/typeorm.module';
import { Repository } from 'typeorm';

let app: INestApplication;
let repository : Repository<Chat>

beforeAll(async () => {
  const module= await Test.createTestingModule({
    imports: [
      AppModule,
      TypeOrmModule.forRoot({
        type: 'sqlite',
        database: 'testChatDb.db',
        entities: [Chat],
        synchronize: false,
      }),
      TypeOrmModule.forFeature([Chat])
    ]
  }).compile();

  app = module.createNestApplication();
  await app.init();
  repository = module.get('ChatRepository');
});

afterAll( async() => {
  await app.close();
});

  //-------------------------------------- TESTS --------------------------
describe('POST /chat/2', () => {
  it('should create an url for poll 2', async () => {
    const { body } = await request.agent(app.getHttpServer())
      .post('/chat/2')
      .expect(201) //201 = created

      expect(body).toMatchObject({pollId : "2", url : expect.any(String)});
  });
});

describe('GET /chat/1', () => {
  it('should return url of chatroom for poll 1', async () => {

    await repository.save({ pollId : 1, url : "some url" });

    request.agent(app.getHttpServer())
      .get('/chat/1')
      .expect('Content-Type', String)
      .expect(200)
      .expect("some url")
  });
});

describe('DELETE /chat/2', () => {
  it('should delete poll 2 and return true', async () => {

    await request.agent(app.getHttpServer())
      .delete('/chat/2')
      .expect(200)

    request.agent(app.getHttpServer())
      .get('/chat/2')
      .expect(500)
  });
});

/* can't understand why it's not working with this test
describe('PUT /chat/1', () => {
  it('should change url of poll 1', async () => {

    //repository.save({ pollId : 1, url : "old url" });

    const body = await request.agent(app.getHttpServer())
      .put("/chat/1")
      .send({ url : "new url" })
      .catch((error) => {
        console.error(error,'Promise error | ',{body});  
      })

  });
});
*/