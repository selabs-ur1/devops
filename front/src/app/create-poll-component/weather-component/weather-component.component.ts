import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { Observable } from 'rxjs';
import { WeatherInput } from 'src/app/model/model';
import { WeatherService } from 'src/app/weather-service.service';
import * as moment from'moment';
@Component({
  selector: 'weather-component',
  templateUrl: './weather-component.component.html',
  styleUrls: ['./weather-component.component.css']
})
export class WeatherComponent implements OnInit, OnChanges {
  @Input()
  weatherInput : WeatherInput
  @Input()
  zipCode:string
  iconsCode: string[] = []
  constructor(public weatherService:WeatherService) { }

  ngOnChanges(changes: SimpleChanges): void {
    this.getWeather();
  }

  ngOnInit(): void {
    this.getWeather();
    }
  
    private getWeather():void{
      if(this.zipCode && this.weatherInput){
        this.weatherService.loadData(this.zipCode).subscribe(res=>{
          const numberOfDay = moment(this.weatherInput.lastDay).diff(moment(this.weatherInput.firstDay),'days'); 
          let currentDate:moment.Moment = moment(this.weatherInput.firstDay);
          for(var i=0;i<7;i++)  {
            if(i<(7-numberOfDay)){
              this.iconsCode[i]=null;
            }else{
              this.iconsCode[i]= this.weatherService.getWeatherIcon(this.zipCode,currentDate.toDate());
              currentDate=currentDate.add(1,'days')
  
            }
          } 
        })
            

      }

    }
  }






 