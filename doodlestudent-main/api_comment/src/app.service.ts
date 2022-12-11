import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, DeleteResult } from 'typeorm'
import { Comment } from './app.entity'

@Injectable()
export class AppService {

    constructor(
        @InjectRepository(Comment)
        private repository : Repository<Comment>
    ) {}

  async getAllCommentsForPoll(pollId: number) : Promise<Comment[]> {
      const comments = await this.repository.find({pollId : pollId})
      console.log("comments = " + comments)
      return comments
  }

  async addComment(pollId: number, content: string, auteur: string) : Promise<Comment> {
      const newComment = this.repository.create({pollId: pollId, content: content, auteur: auteur})
      this.repository.save(newComment)
      return newComment
  }

  async removeAllCommentsForPoll(pollId: number) : Promise<DeleteResult> {
      return this.repository.delete({pollId: pollId})
  }

  async removeCommentsById(commentId: number) : Promise<DeleteResult> {
      return this.repository.delete({id: commentId})
  }

}
