import { Body, Controller, Delete, Get, HttpException, HttpStatus, Param, Post, Put } from '@nestjs/common';
import { Team } from './team.entity';
import { TeamService } from './team.service';

@Controller('team')
export class TeamController {
    constructor(private service: TeamService) {}

    @Post(":name")
    public async create(@Param() parameter): Promise<Team> {
        return this.service.create(parameter.name);
    }

    @Get("/name/:name")
    public async getTeam(@Param() parameter): Promise<Team> {
        return (await this.service.getTeam(parameter.name));
    }

    @Get("/id/:id")
    public async getTeamById(@Param() parameter): Promise<Team> {
        return (await this.service.getTeamById(parameter.id));
    }

    @Delete(":name")
    public async del(@Param() parameter): Promise<boolean> {
        return this.service.del(parameter.name);
    }
}
