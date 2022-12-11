import { Column, Entity, PrimaryColumn } from 'typeorm';

@Entity()
export class Chat {
  @PrimaryColumn()
  public pollId: number;
  @Column()
  public url: string;
}