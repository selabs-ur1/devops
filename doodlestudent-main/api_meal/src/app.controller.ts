import { Controller, Get, Post, Delete, Body, Param, HttpException, HttpStatus } from '@nestjs/common';
import { AppService } from './app.service';
import { Meal } from './app.entity'

@Controller('/poll')
export class AppController {
  constructor(private readonly appService: AppService) {}

  @Get("/:pollId/mealpreferences")
  async getAllMealPreferencesFromPoll(@Param('pollId') pollId) : Promise<Meal[]> {
      return await this.appService.getAllMealPreferencesFromPoll(pollId)
  }

  @Get("/:pollId/mealpreference/:mealId")
  async getMealPreferenceFromPoll(@Param('pollId') pollId, @Param('mealId') mealId) : Promise<Meal> {
      return await this.appService.getMealPreferenceFromPoll(pollId, mealId)
  }

  @Post()
  async addMealPreference(@Body() input: Meal) : Promise<Meal> {
      console.log(input)
      return await this.appService.addMealPreference(input.pollId, input.content, input.auteur)
  }

  @Delete("/:pollId")
  async removeAllMealPreferencesFromPoll(@Param('pollId') pollId) : Promise<boolean> {
      const removed = await this.appService.removeAllMealPreferencesFromPoll(pollId)
      if (removed == null) {
          throw new HttpException("No such poll", HttpStatus.NOT_FOUND)
      }
      return true
  }

  @Delete("/mealpreference/:mealId")
  async removeMealPreferenceById(@Param('mealId') mealId) : Promise<boolean> {
      const removed = await this.appService.removeMealPreferenceById(mealId)
      if (removed == null) {
          throw new HttpException("No such comment.", HttpStatus.NOT_FOUND)
      }
      return true
  }


}

