import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  await app.listen(3005);
  //curl -X POST -d "pollId=1&driver=JC&departure_localisation=Rennes&departure_time=10:00&arriving_time=18:00&places=4&carpoolers_mails=[]" http://localhost:3005/poll
}
bootstrap();
