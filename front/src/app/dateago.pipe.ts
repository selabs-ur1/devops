import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dateago',
  pure: true

})
export class DateagoPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    if (value) {
        const seconds = Math.floor((+new Date() - +new Date(value)) / 1000);
        if (seconds < 29) { // less than 30 seconds ago will show as 'Just now'
            return 'quelques secondes';
        }
        const intervals = {
            année: 31536000,
            mois: 2592000,
            semaine: 604800,
            jour: 86400,
            heure: 3600,
            minute: 60,
            seconde: 1
        };
        let counter;
        // tslint:disable-next-line:forin
        for (const i in intervals) {
            counter = Math.floor(seconds / intervals[i]);
            if (counter > 0){
                if (counter === 1 || i === 'mois') {
                    return counter + ' ' + i + ''; // singular (1 day ago)
                } else {
                    return counter + ' ' + i + 's'; // plural (2 days ago)
                }
              }
        }
    }
    return value;
}

}
