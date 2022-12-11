import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, DeleteResult } from 'typeorm'
import { Meal } from './app.entity'

@Injectable()
export class AppService {

    constructor(
        @InjectRepository(Meal)
        private repository : Repository<Meal>
    ) {}

  async getAllMealPreferencesFromPoll(pollId: number) : Promise<Meal[]> {
      const meals = await this.repository.find({
          select: {
            id: true,
            pollId: true,
            content: true,
            auteur: true
          },
          where: { pollId : pollId }
      })
      return meals
  }

  async getMealPreferenceFromPoll(pollId: number, mealId : number) : Promise<Meal> {
    const mealpref = await this.repository.findOne({
        select: {
          id: true,
          pollId: true,
          content: true,
          auteur: true
        },
        where: { pollId : pollId }
    })
    return mealpref
}

  async addMealPreference(pollId: number, content: string, auteur: string) : Promise<Meal> {
      const newMealPreference = this.repository.create({pollId: pollId, content: content, auteur: auteur})
      this.repository.save(newMealPreference)
      return newMealPreference
  }

  async removeAllMealPreferencesFromPoll(pollId: number) : Promise<DeleteResult> {
      return this.repository.delete({pollId: pollId})
  }

  async removeMealPreferenceById(mealId: number) : Promise<DeleteResult> {
    return this.repository.delete({id: mealId})
}


}
