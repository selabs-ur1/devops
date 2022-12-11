import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Equal, Repository } from 'typeorm';
import { Chat } from './chat.entity';

@Injectable()
export class ChatsService {
  
    constructor(@InjectRepository(Chat) private repository: Repository<Chat>) {}

    private CHARS: string = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ234567890";

    public async create(id: number): Promise<Chat> {
      let slug: string = "";
        for (let i = 0; i < 12; i++) {
            slug += this.CHARS.charAt(Math.floor(Math.random() * this.CHARS.length));
        }

      const newChat = await this.repository.create({
        pollId: id,
        url: "https://tlk.io/"+slug
      });
      this.repository.save(newChat);
      return newChat;
    }

    public async getByPollId(idToFind: number): Promise<Chat>{
      return (await this.repository.findOne({ pollId: Equal(idToFind) }));
    }

    public async setUrl(idToFind: number, url:string): Promise<Chat>{
      const chatToUpdate: Promise<Chat> = this.getByPollId(idToFind);
      if(chatToUpdate === undefined){
        return undefined;
      }
      (await chatToUpdate).url = url;
      this.repository.save(await chatToUpdate);
      return chatToUpdate;
    }

    public async del(id: number): Promise<boolean> {
      if ((await this.repository.findOne({ pollId: Equal(id) })) != undefined) {
        this.repository.delete(await this.getByPollId(id));
        return true;
      }
      return false;
    }
}
