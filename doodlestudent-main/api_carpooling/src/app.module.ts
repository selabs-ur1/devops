import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Carpooling } from './app.entity';

@Module({
  imports: [
    TypeOrmModule.forRoot({
          type: 'sqlite',
          database: 'carpooling.db',
          entities: [Carpooling],
          synchronize: true,
        }),
    TypeOrmModule.forFeature([Carpooling])
  ],
  controllers: [AppController],
  providers: [AppService],
  exports : [AppService]
})
export class AppModule {}
