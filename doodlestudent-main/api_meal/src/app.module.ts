import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { Meal } from './app.entity';

@Module({
  imports: [
    TypeOrmModule.forRoot({
            type: 'sqlite',
            database: 'meal.db',
            entities: [Meal],
            synchronize: true,
          }),
    TypeOrmModule.forFeature([Meal])
  ],
  controllers: [AppController],
  providers: [AppService],
  exports : [AppService]
})
export class AppModule {}
