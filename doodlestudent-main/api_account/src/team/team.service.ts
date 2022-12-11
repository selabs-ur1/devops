import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { AccountService } from 'src/account/account.service';
import { Equal, Repository } from 'typeorm';
import { Team } from './team.entity';

@Injectable()
export class TeamService {
    constructor(@InjectRepository(Team) private repository: Repository<Team>) {}

    public async create(name: string): Promise<Team> {
        const newTeam = await this.repository.create({
          name: name
        });

        this.repository.save(newTeam);
        return newTeam;
    }

    public async getTeam(name: string): Promise<Team>{
        return (await this.repository.findOne({ name: Equal(name) }));
    }

    public async getTeamById(id: number): Promise<Team>{
      return (await this.repository.findOne({ id: Equal(id) }));
   }

    public async del(name: string): Promise<boolean> {
        const team: Promise<Team> = this.getTeam(name)
        if (team != undefined) {
          this.repository.delete(await team);
          return true;
        }
        return false;
    }
}
