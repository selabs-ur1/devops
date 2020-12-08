import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Poll, PollChoice, User, ChoiceUser, PollCommentElement, EventDTOAndSelectedChoice } from './model/model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PollService {

  constructor(private http: HttpClient) { }

  public createPoll(p: Poll): Observable<Poll> {
    console.log('create poll');
    return this.http.post<Poll>('/api/polls', p);
  }


  public updtatePoll(p: Poll): Observable<Poll> {
    return this.http.put<Poll>('/api/poll/update1', p);
  }


  public getPollBySlugId(slugId: string): Observable<Poll>{
    return this.http.get<Poll>('/api/poll/slug/' + slugId);
  }

  public getComentsBySlugId(slugId: string): Observable<PollCommentElement[]>{
    return this.http.get<PollCommentElement[]>('/api/polls/' + slugId + '/comments');
  }

  public getPollBySlugAdminId(slugId: string): Observable<Poll>{
    return this.http.get<Poll>('/api/poll/aslug/' + slugId);

  }

  public updateChoice4user( cu: ChoiceUser): Observable<User>{

    return this.http.post<User>('/api/poll/choiceuser/', cu);
  }

  public addComment4Poll( slug: string, comment: PollCommentElement ): Observable<PollCommentElement>{

    return this.http.post<PollCommentElement>('/api/poll/comment/' + slug, comment);
  }

  selectEvent(choiceid: number): Observable<void> {
    return this.http.post<void>('/api/poll/selectedchoice/' + choiceid, null);

  }

  getICS(slug: string, ics: string): Observable<EventDTOAndSelectedChoice> {
    return this.http.get<EventDTOAndSelectedChoice>('/api/ics/polls/' + slug + '/' + btoa(ics));
  }


}
