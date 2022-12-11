import { Controller, Get, Post, Put, Delete, Body, Param, HttpException, HttpStatus } from '@nestjs/common';
import { AppService } from './app.service';
import { Carpooling } from './app.entity'

@Controller('/poll')
export class AppController {
  constructor(private readonly appService: AppService) {}

  @Get("/:pollId/carpooling")
  async getAllCarpoolingsFromPoll(@Param('pollId') pollId) : Promise<Carpooling[]> {
      return await this.appService.getAllCarpoolingsFromPoll(pollId)
  }

  @Get("/carpooling/:carpoolingId")
  async getCarpoolingFromPoll(@Param('carpoolingId') carpoolingId) : Promise<Carpooling> {
      console.log("getCarpoolingFromPoll(" + carpoolingId + ")");
      return await this.appService.getCarpoolingFromPoll(carpoolingId)
  }

  @Get("/:pollId/carpooling/:carpoolingId/users")
  async getUsersinCarpoolingFromPoll(@Param('carpoolingId') carpoolingId, @Param('pollId') pollId) : Promise<string[]> {
      return await this.appService.getUsersinCarpoolingFromPoll(carpoolingId,pollId)
  }

  @Get("/carpooling/:carpoolingId/:mail")
  async addCarpooler(@Param('carpoolingId') carpoolingId, @Param('mail') mail) : Promise<Carpooling> {
      console.log("carpoolingId:" + carpoolingId + "; mail:" + mail);
      return await this.appService.addCarpooler(carpoolingId, mail);
  }
  @Post()
  async addCarpooling(@Body() input: Carpooling) : Promise<Carpooling> {
      console.log(input);
      let c_mails = input.carpoolers_mails;
      if (c_mails == null) {
        c_mails = [];
      }
      return await this.appService.addCarpooling(input.pollId, input.driver, input.departure_localisation, input.departure_time, input.arriving_time, input.places, c_mails);
    }

  /*@Put("/:pollId/carpooling/:carpoolingId")
  async putUser(@Body() input: Carpooling): Promise<Carpooling>{
    console.log(input)
    return this.appService.putCarpooling(input.id, input.pollId, input.driver, input.departure_localisation, input.departure_time, input.arriving_time, input.places, input.places_restantes, input.carpoolers_mails)
  }*/

 @Put('/carpooling')
  async putUserInCarpooling(@Body() input): Promise<Carpooling>{
    console.log("controller",input.carpoolingId, input.carpoolers_mail)
    return this.appService.putUserInCarpooling(input.carpoolingId, input.carpoolers_mail)
  }


  @Delete("/:pollId/carpooling")
  async removeAllCarpoolingsFromPoll(@Param('pollId') pollId) : Promise<boolean> {
      const removed = await this.appService.removeAllCarpoolingsFromPoll(pollId)
      if (removed == null) {
          throw new HttpException("No such poll", HttpStatus.NOT_FOUND)
      }
      return true
  }

  @Delete("/:pollId/carpooling/:carpoolingId")
  async removeCarpoolingById(@Param('carpoolingId') carpoolingId, @Param('pollId') pollId) : Promise<boolean> {
      const removed = await this.appService.removeCarpoolingById(carpoolingId, pollId)
      if (removed == null) {
          throw new HttpException("No such comment.", HttpStatus.NOT_FOUND)
      }
      return true
  }
}
