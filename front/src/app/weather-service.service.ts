import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from'moment';

@Injectable({
  providedIn: 'root'
})
export class WeatherService {
  constructor(private http: HttpClient) { }
  private list=[];

  public  getWeatherIcon(zipCode:string,date:Date):string{
    var weather=this.list.filter(element =>{
      return (moment(element.datetime,'YYYY-MM-DD').diff(moment(date),'days')==0);

    })
    return weather[0]?.weather?.icon

  }

  public loadData(zipCode):Observable<any>{
    return this.http.get<any>('/api/weather/'+zipCode)
      .pipe<any>(
        map(result =>{ 
          this.list=result.data 
          return 
        })
      );
  }
}
