import { Controller, Get, Post, Delete, Body, Param, HttpException, HttpStatus } from '@nestjs/common';
import { AppService } from './app.service';
import { Comment } from './app.entity'

@Controller('/poll')
export class AppController {
  constructor(private readonly appService: AppService) {}

  @Get("/:pollId")
  async getAllCommentsForPoll(@Param('pollId') pollId) : Promise<Comment[]> {
      return await this.appService.getAllCommentsForPoll(pollId)
  }

  @Post()
  async addComment(@Body() input: Comment) : Promise<Comment> {
      //console.log(input)
      return await this.appService.addComment(input.pollId, input.content, input.auteur)
  }

  @Delete("/:pollId")
  async removeAllCommentsForPoll(@Param('pollId') pollId) : Promise<boolean> {
      const removed = await this.appService.removeAllCommentsForPoll(pollId)
      if (removed == null) {
          throw new HttpException("No such poll.", HttpStatus.NOT_FOUND)
      }
      return true
  }

  @Delete("/comment/:commentId")
  async removeCommentsById(@Param('commentId') commentId) : Promise<boolean> {
      const removed = await this.appService.removeCommentsById(commentId)
      if (removed == null) {
          throw new HttpException("No such comment.", HttpStatus.NOT_FOUND)
      }
      return true
  }


}
