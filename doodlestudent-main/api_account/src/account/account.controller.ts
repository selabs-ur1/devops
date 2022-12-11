import { Body, Controller, Delete, Get, HttpException, HttpStatus, Param, Post, Put } from '@nestjs/common';
import { Team } from 'src/team/team.entity';
import { Account } from './account.entity';
import { AccountService } from './account.service';
import { Member } from './member.entity';
import { Poll } from './poll.entity';

@Controller('account')
export class AccountController {
    constructor(private service: AccountService) {}

    @Post()
    public async create(@Body() input: any): Promise<Account> {
        return this.service.create(input.firstname, input.lastname, input.mail, input.password);
    }

    @Get()
    public async getAccount(@Body() input: any): Promise<Account> {
        return (await this.service.getAccount(input.mail));
    }

    @Get(":mail")
    public async getAccountBis(@Param() parameter): Promise<Account> {
        return (await this.service.getAccount(parameter.mail));
    }

    @Get("/team")
    public async getTeams(@Body() input: any): Promise<Team[]> {
        return (await this.service.getTeams(input.mail));
    }

    @Get("/polls/admin/:mail")
    public async getPollsAdmin(@Param() parameter): Promise<number[]> {
        return (await this.service.getPollsAdmin(parameter.mail));
    }

    @Get("/polls/member/:mail")
    public async getPollsMember(@Param() parameter): Promise<number[]> {
        return (await this.service.getPollsMember(parameter.mail));
    }

    @Get("/agenda/:mail")
    public async getAgendaIcs(@Param() parameter): Promise<String> {
        return (await this.service.getAccount(parameter.mail)).agendaIcs;
    }

    @Get("/teamMembers/:name")
    public async getMailMembers(@Param() parameter): Promise<string[]> {
        return (await this.service.getMailMembers(parameter.name));
    }

    @Put("/mail")
    public async setMail(@Body() input: any): Promise<Account> {
        const res: Account = (await this.service.setMail(input.mail, input.newMail));
        if (res === undefined) {
            throw new HttpException(
                'Could not find an account with mail: ' + input.mail,
                HttpStatus.NOT_FOUND,
            );
        }
        return res;
    }

    @Put("/team")
    public async addTeam(@Body() input: any): Promise<Member> {
        const res: Member = (await this.service.addTeam(input.mail, input.teamName));
        return res;
    }

    @Put("/poll")
    public async addPoll(@Body() input: any): Promise<Poll> {
        const res: Poll = (await this.service.addPoll(input.mail, input.pollId, input.admin));
        if (res === undefined) {
            throw new HttpException(
                'Could not find an account with mail: ' + input.mail,
                HttpStatus.NOT_FOUND,
            );
        }
        return res;
    }

    @Put("/agenda")
    public async setAgendaIcs(@Body() input: any): Promise<Account> {
        const res: Account = (await this.service.setAgendaIcs(input.mail, input.agendaIcs));
        if (res === undefined) {
            throw new HttpException(
                'Could not find an account with mail: ' + input.mail,
                HttpStatus.NOT_FOUND,
            );
        }
        return res;
    }

    @Delete()
    public async del(@Body() input: any): Promise<boolean> {
        return this.service.del(input.mail);
    }
}
