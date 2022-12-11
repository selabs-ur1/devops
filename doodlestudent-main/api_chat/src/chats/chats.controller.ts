import { Body, Controller, Delete, Get, HttpException, HttpStatus, Param, Post, Put } from '@nestjs/common';
import { Chat } from './chat.entity';
import { ChatsService } from './chats.service';

@Controller('chat')
export class ChatsController {
    constructor(private service: ChatsService) {}

    @Post(':id')
    public async create(@Param() parameter): Promise<Chat> {
        return this.service.create(parameter.id);
    }

    @Get(':id')
    public async getByPollId(@Param() parameter): Promise<string> {
        return (await this.service.getByPollId(parameter.id)).url;
    }

    @Put(':id')
    public async setUrl(@Body() input: any, @Param() parameter): Promise<Chat> {
        const res: Chat = (await this.service.setUrl(parameter.id, input.url));
        if (res === undefined) {
            throw new HttpException(
                'Could not find a chat with poll id: ' + parameter.id,
                HttpStatus.NOT_FOUND,
            );
        }
        return res;
    }

    @Delete(':id')
    public async del(@Param() parameter): Promise<boolean> {
        return this.service.del(parameter.id);
    }
}
