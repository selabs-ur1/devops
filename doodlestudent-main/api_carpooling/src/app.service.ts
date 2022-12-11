import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, DeleteResult } from 'typeorm'
import { Carpooling } from './app.entity';

@Injectable()
export class AppService {

  constructor(
    @InjectRepository(Carpooling)
    private repository : Repository<Carpooling>
) {}

async getAllCarpoolingsFromPoll(pollId: number) : Promise<Carpooling[]> {
  const carpooling = await this.repository.find({pollId : pollId})
  return carpooling
}

async getCarpoolingFromPoll(carpoolingId : number) : Promise<Carpooling> {
const carpooling = await this.repository.findOne({id : carpoolingId})
return carpooling
}

async getUsersinCarpoolingFromPoll(carpoolingId : number, pollId: number) : Promise<string[]> {
  const carpooling = await this.repository.findOne({id : carpoolingId, pollId : pollId})
  return carpooling.carpoolers_mails
  }

async addCarpooling(pollId: number, driver: string, departure_localisation: string, departure_time: string, arriving_time: string, places: number, carpoolers_mails: string[]) : Promise<Carpooling> {
  const newCarpoolingPreference = this.repository.create({pollId: pollId, driver: driver,
    departure_localisation: departure_localisation,
    departure_time: departure_time, arriving_time: arriving_time, places: places,
    places_restantes: places, carpoolers_mails: carpoolers_mails})
  this.repository.save(newCarpoolingPreference)
  return newCarpoolingPreference
}

/*async putCarpooling(id: number, pollId: number, driver: string, departure_localisation: string, departure_time: string, arriving_time: string, places: number, places_restantes: number, carpoolers_mails: string[]) : Promise<Carpooling> {
  const carpoolingModify : Carpooling = await this.getCarpoolingFromPoll(id, pollId)
  console.log(driver)
  if(driver !== undefined){
    carpoolingModify.driver = driver
  }
  if(departure_localisation !== undefined){
    carpoolingModify.departure_localisation = departure_localisation
  }
  if(departure_time !== undefined){
    carpoolingModify.departure_time = departure_time
  }
  if(arriving_time !== undefined){
    carpoolingModify.arriving_time = arriving_time
  }
  if(places !== undefined){
    carpoolingModify.places = places
  }
  if(places_restantes !== undefined){
    carpoolingModify.places_restantes = places_restantes
  }

  carpoolingModify.carpoolers_mails = carpoolers_mails
  if(carpoolers_mails[0] !== undefined){
    for(let i=0 ; i++ ; i<carpoolers_mails.length){
      carpoolingModify.carpoolers_mails[i] = carpoolers_mails[i]
    }
  }
  this.repository.save(carpoolingModify)
  return carpoolingModify;
}*/

async putUserInCarpooling(id: number, carpoolers_mail: string) : Promise<Carpooling> {
  const carpoolingModify : Carpooling = await this.getCarpoolingFromPoll(id)
  if(carpoolingModify!==undefined){
    let existAt : number = -1
   for(let i=0 ; i<carpoolingModify.carpoolers_mails.length ; i++){
     if(carpoolingModify.carpoolers_mails[i].localeCompare(carpoolers_mail)==0){existAt=i}
   }
    if(existAt==-1){ //on ajoute un utilisateur dans la voiture
    console.log("1er if--",existAt)
     if(carpoolingModify.places_restantes>0){
        console.log("1er.1 if")
        carpoolingModify.places_restantes --
        carpoolingModify.carpoolers_mails.push(carpoolers_mail)
        console.log(carpoolingModify.carpoolers_mails)
      }
      else(console.log("plus de place dans la voiture"))
    }

    else { //on supprime un utilisateur de la voiture
      if((carpoolingModify.places-carpoolingModify.places_restantes)>0){
        carpoolingModify.places_restantes ++
        carpoolingModify.carpoolers_mails.splice(existAt,1)
        console.log(carpoolingModify.carpoolers_mails)
      }
      else(console.log("personne dans la voiture"))
    }
}
else(console.log("ces attributs de sondage ne sont pas bons"))
  this.repository.save(carpoolingModify)
  return carpoolingModify
}

async addCarpooler(carpoolingId: number, mail: string) : Promise<Carpooling> {
  console.log('addCarpooler('+carpoolingId+','+ mail +')');
  const carpooling = await this.repository.findOne({id : carpoolingId})
  carpooling.addCarpooler(mail);

  this.repository.save(carpooling);

  return carpooling;
}

async removeAllCarpoolingsFromPoll(pollId: number) : Promise<DeleteResult> {
  return this.repository.delete({pollId: pollId})
}

async removeCarpoolingById(carpoolingId: number, pollId: number) : Promise<DeleteResult> {
return this.repository.delete({id : carpoolingId, pollId : pollId})
}
}
