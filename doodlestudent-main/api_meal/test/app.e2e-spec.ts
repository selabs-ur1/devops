import { Test } from '@nestjs/testing';
import { INestApplication } from '@nestjs/common';
import * as request from 'supertest';
import { AppModule } from './../src/app.module';
import { Meal } from '../src/app.entity';
import { TypeOrmModule } from '@nestjs/typeorm/dist/typeorm.module';
import { Repository } from 'typeorm';

let app: INestApplication;
let repository : Repository<Meal>

beforeAll(async () => {
  const module= await Test.createTestingModule({
    imports: [
      AppModule,
      TypeOrmModule.forRoot({
        type: 'sqlite',
        database: 'testMealDb.db',
        entities: [Meal],
        synchronize: false,
      }),
      TypeOrmModule.forFeature([Meal])
    ]
  }).compile();

  app = module.createNestApplication();
  await app.init();
  repository = module.get('MealRepository');
});

afterAll(async () => {
  await app.close();
});

  afterEach(async () => {
    await repository.query(`DELETE FROM meal;`);
  });

  //-------------------------------------- TESTS --------------------------
describe('POST /poll/-1', () => {
  it('should create a meal in db', () => {
    return request.agent(app.getHttpServer())
      .post('/poll')
      .send({pollId : 1, content :"frite", auteur :"didier"})
      .expect(201) //201 = created
  });
});

describe('GET /poll/1/mealpreferences', () => {
  it('should return all meals from poll 1', async () => {

    await repository.save([
      { pollId : 1, content : "courgettes", auteur : "patrick" },
      { pollId : 1, content : "pates", auteur : "jack" },
    ]);

    const { body } = await request.agent(app.getHttpServer())
      .get('/poll/1/mealpreferences')
      .expect('Content-Type', /json/)
      .expect(200)
    
    expect(body).toMatchObject([
      { id : expect.anything(), pollId : 1, content : "frite", auteur: 'didier' },
      { id : expect.anything(), pollId : 1, content : "courgettes", auteur: 'patrick' },
      { id : expect.any(Number), pollId : 1, content : "pates", auteur: 'jack' }
    ]);
  });
});

describe('DELETE /poll/:pollId', () => {
  it('should return true en should get an empty tab after get req', async () => {

    await request.agent(app.getHttpServer())
      .delete('/poll/1')
      .expect(200)

    const { body } = await request.agent(app.getHttpServer())
      .get('/poll/1/mealpreferences')
      .expect('Content-Type', /json/)
      .expect(200)
      .expect('[]')
  });
});
