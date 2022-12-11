import { Column, Entity, PrimaryGeneratedColumn } from 'typeorm';

@Entity()
export class Poll {
  @PrimaryGeneratedColumn()
  public id: number;
  @Column()
  public accountId: number;
  @Column()
  public pollId: number;
  @Column()
  public admin: number;
}