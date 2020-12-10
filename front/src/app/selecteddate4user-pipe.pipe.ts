import { Pipe, PipeTransform } from '@angular/core';
import { PollChoice } from './model/model';
import { EventInput } from '@fullcalendar/core';

@Pipe({
  name: 'selecteddate4userPipe'
})
export class Selecteddate4userPipePipe implements PipeTransform {

  transform(value: PollChoice[], id: number, ev: EventInput): boolean {
    return value.map( e => e.id).includes(ev.extendedProps.choiceid);
  }

}
