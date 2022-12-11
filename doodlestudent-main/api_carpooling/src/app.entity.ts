
import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';

@Entity()
export class Carpooling {

    @PrimaryGeneratedColumn()
    id : number;

    @Column()
    pollId: number;

    @Column()
    driver: string;

    @Column()
    departure_localisation: string;

    @Column()
    departure_time: string;

    @Column()
    arriving_time: string;

    @Column()
    places: number;

    @Column()
    places_restantes: number;

    @Column("simple-array")
    carpoolers_mails: string[];

    constructor(pollId: number, driver: string, departure_localisation: string, departure_time: string, arriving_time: string, places: number, carpoolers_mails: string[]) {
        this.pollId = pollId
        this.driver = driver
        this.departure_localisation = departure_localisation
        this.departure_time = departure_time
        this.arriving_time = arriving_time
        this.places = places
        this.places_restantes = places
        if (carpoolers_mails) {
          for(let i=0 ; i++ ; i<carpoolers_mails.length){
              this.carpoolers_mails[i] = carpoolers_mails[i]
          }
        }
        else {
          this.carpoolers_mails = []
        }
    }

    addCarpooler(mail: string) : void {
      let existAt : number = -1
      for(let i=0 ; i<this.carpoolers_mails.length ; i++){
        if(this.carpoolers_mails[i].localeCompare(mail)==0){existAt=i}
      }

      if(existAt==-1){ //on ajoute un utilisateur dans la voiture
        this.carpoolers_mails[this.carpoolers_mails.length] = mail;
        this.places_restantes--
      }
      else {
        if((this.places-this.places_restantes)>0){
          this.places_restantes ++
          this.carpoolers_mails.splice(existAt,1)
          console.log(this.carpoolers_mails)
        }
      }
    }
}
