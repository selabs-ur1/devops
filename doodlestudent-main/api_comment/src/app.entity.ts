import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm'

@Entity()
export class Comment {
    @PrimaryGeneratedColumn()
    id : number;

    @Column()
    pollId: number;

    @Column()
    content: string;

    @Column()
    auteur: string;

    constructor(id: number, pollId: number, content: string, auteur: string) {
        this.id = id
        this.pollId = pollId
        this.content = content
        this.auteur = auteur
    }
}
