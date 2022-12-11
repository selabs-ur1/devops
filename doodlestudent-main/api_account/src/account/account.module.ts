import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { AccountController } from './account.controller';
import { AccountService } from './account.service';
import { Account } from './account.entity';
import { TeamService } from 'src/team/team.service';
import { TeamModule } from 'src/team/team.module';
import { Member } from './member.entity';
import { Poll } from './poll.entity';

@Module({
  imports: [TypeOrmModule.forFeature([Account]), TypeOrmModule.forFeature([Member]), TypeOrmModule.forFeature([Poll]), TeamModule],
  controllers: [AccountController],
  providers: [AccountService],
  exports: [AccountService]
})
export class AccountModule {}
