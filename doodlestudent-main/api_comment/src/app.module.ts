import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Comment } from './app.entity'

@Module({
  imports: [TypeOrmModule.forRoot({
      type: 'sqlite',
      database: 'comments.db',
      entities: [Comment],
      synchronize: true,
  }), TypeOrmModule.forFeature([Comment])],
  controllers: [AppController],
  providers: [AppService],
  exports: [AppService]
})
export class AppModule {}
