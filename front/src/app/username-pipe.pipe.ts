import { Pipe, PipeTransform } from '@angular/core';
import { User } from './model/model';

@Pipe({
  name: 'usernamePipe'
})
export class UsernamePipePipe implements PipeTransform {

  transform(value: User[], id: number): string {
    return value.filter(u => u.id === id)[0].username;
  }

}
