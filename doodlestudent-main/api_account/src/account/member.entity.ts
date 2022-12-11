import { Column, Entity, PrimaryGeneratedColumn } from 'typeorm';

@Entity()
export class Member {
  @PrimaryGeneratedColumn()
  public id: number;
  @Column()
  public accountId: number;
  @Column()
  public teamId: number;
}