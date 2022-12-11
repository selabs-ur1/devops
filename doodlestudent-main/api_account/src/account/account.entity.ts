import { Column, Entity, PrimaryGeneratedColumn } from 'typeorm';

@Entity()
export class Account {
  @PrimaryGeneratedColumn()
  public id: number;
  @Column()
  public firstname: string;
  @Column()
  public lastname: string;
  @Column()
  public mail: string;
  @Column()
  public password: string;
  @Column()
  public agendaIcs: string;
}