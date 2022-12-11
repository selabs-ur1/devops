import { Injectable, SerializeOptions } from '@nestjs/common';
import { Account } from './account.entity';
import { Equal, Repository } from 'typeorm';
import { InjectRepository } from '@nestjs/typeorm';
import * as bcrypt from 'bcrypt';
import { TeamService } from 'src/team/team.service';
import { Team } from 'src/team/team.entity';
import { Member } from './member.entity';
import { Poll } from './poll.entity';

@Injectable()
export class AccountService {
    constructor(@InjectRepository(Account) private repository: Repository<Account>, 
                @InjectRepository(Member) private memberRepository: Repository<Member>,
                @InjectRepository(Poll) private pollRepository: Repository<Poll>,
                private teamService: TeamService) {}

    public async create(firstname: string, lastname: string , mail: string, password: string): Promise<Account> {

        const saltOrRounds = 10;
        const hash = await bcrypt.hash(password, saltOrRounds);

        const newAccount = await this.repository.create({
          firstname: firstname,
          lastname: lastname,
          mail: mail, 
          password: hash, 
          agendaIcs: ""
        });

        this.repository.save(newAccount);
        return newAccount;
      }

      public async getAccount(mail: string): Promise<Account>{
        return (await this.repository.findOne({ mail: Equal(mail) }));
      }

      public async getAccountById(id: number): Promise<Account>{
        return (await this.repository.findOne({ id: Equal(id) }));
      }

      public async getTeams(mail: string): Promise<Team[]>{
        const members: Promise<Member[]> = this.memberRepository.find({ accountId: Equal((await this.getAccount(mail)).id) });
        let teams: Team[] = [];
        (await members).forEach(async member => {
          teams.push(await this.teamService.getTeamById(member.teamId));
        })
        await new Promise(resolve => setTimeout(resolve, 100));
        return teams;
      }

      public async getPollsAdmin(mail: string): Promise<number[]>{
        const polls: Promise<Poll[]> = this.pollRepository.find({ accountId: Equal((await this.getAccount(mail)).id),
                                                                  admin: Equal(1) });
        let pollsId: number[] = [];
        (await polls).forEach(async poll => {
          pollsId.push(poll.pollId);
        })
        return pollsId;
      }

      public async getPollsMember(mail: string): Promise<number[]>{
        const polls: Promise<Poll[]> = this.pollRepository.find({ accountId: Equal((await this.getAccount(mail)).id),
                                                                  admin: Equal(0) });
        let pollsId: number[] = [];
        (await polls).forEach(async poll => {
          pollsId.push(poll.pollId);
        })
        return pollsId;
      }

      public async getMailMembers(name: string): Promise<string[]>{
        const teamId: number = (await this.teamService.getTeam(name)).id;
        const members: Promise<Member[]> = this.memberRepository.find({ teamId: Equal(teamId) });
        let mails: string[] = [];
        (await members).forEach(async member => {
          mails.push(await (await this.repository.findOne({ id: Equal(member.accountId) })).mail);
        })
        await new Promise(resolve => setTimeout(resolve, 100));
        return mails;
      }

      public async setMail(mail: string, newMail: string): Promise<Account>{
        const accountToUpdate: Promise<Account> = this.getAccount(mail);
        if(accountToUpdate === undefined){
          return undefined;
        }
        (await accountToUpdate).mail = newMail;
        this.repository.save(await accountToUpdate);
        return accountToUpdate;
      }

      public async addTeam(mail: string, teamName: string): Promise<Member>{
        const accountId: number = (await this.getAccount(mail)).id;
        const teamId: number = (await this.teamService.getTeam(teamName)).id;
        const newMember = await this.memberRepository.create({
          accountId: accountId,
          teamId: teamId,
        });

        this.memberRepository.save(newMember);
        return newMember;
      }

      public async addPoll(mail: string, pollId: number, admin: number): Promise<Poll>{
        const accountId: number = (await this.getAccount(mail)).id;
        const newPoll = await this.pollRepository.create({
          accountId: accountId,
          pollId: pollId,
          admin: admin
        });

        this.pollRepository.save(newPoll);
        return newPoll;
      }

      public async setAgendaIcs(mail: string, agendaIcs: string): Promise<Account>{
        const accountToUpdate: Promise<Account> = this.getAccount(mail);
        if(accountToUpdate === undefined){
          return undefined;
        }
        (await accountToUpdate).agendaIcs = agendaIcs;
        this.repository.save(await accountToUpdate);
        return accountToUpdate;
      }

      public async del(mail: string): Promise<boolean> {
        const account: Promise<Account> = this.getAccount(mail)
        if (account != undefined) {
          this.repository.delete(await account);
          return true;
        }
        return false;
      }
}

