import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Poll, PollChoice, User, ChoiceUser, PollCommentElement, CarpoolingProposition, EventDTOAndSelectedChoice, Account } from './model/model';
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

  public getCarpoolingPropositionsBySlugId(slugId: string) : Observable<CarpoolingProposition[]> {
      return this.http.get<CarpoolingProposition[]>('/api/polls/' + slugId + '/carpoolings');
      /* PollRessourceEx.java (api) ligne 86 */
  }

  public getMealPreferenceCommentsById(id: number): Observable<PollCommentElement[]>{
    return this.http.get<PollCommentElement[]>('/api/polls/' + id + '/mealpreferences');
    //return this.http.get<PollCommentElement[]>('/api/polls/80/mealpreferences');
  }

  public getPollBySlugAdminId(slugId: string): Observable<Poll>{
    return this.http.get<Poll>('/api/poll/aslug/' + slugId);

  }

  public updateChoice4user(cu: ChoiceUser, pollId: number): Observable<User>{
    console.log("updateChoice4user() : /api/poll/choiceuser/"+pollId);
    return this.http.post<User>('/api/poll/choiceuser/'+pollId, cu);
  }

  public addComment4Poll(slug: string, comment: PollCommentElement ): Observable<PollCommentElement>{

    return this.http.post<PollCommentElement>('/api/poll/comment/' + slug, comment);
  }

  selectEvent(choiceid: number): Observable<void> {
    return this.http.post<void>('/api/poll/selectedchoice/' + choiceid, null);

  }

  getICS(slug: string, ics: string): Observable<EventDTOAndSelectedChoice> {
    return this.http.get<EventDTOAndSelectedChoice>('/api/ics/polls/' + slug + '/' + btoa(ics));
  }

  addCarpooler(carpoolingId : number, mail: string) : Observable<string> {
    //debugger;
    console.log("joinCarpooling("+carpoolingId+"; "+mail+") - poll-service");
    console.log('/api/poll/carpooling/'+carpoolingId+'/'+mail);
    return this.http.get<string>('/api/poll/carpooling/'+carpoolingId+'/'+mail);
  }

  proposeCarpooling(driver: string, departure_localisation: string,
    departure_time: string, arriving_time: string, places: string, pollId : number) : Observable<void> {
    //debugger;
    const carpooling = {
        pollId: pollId,
        driver: driver,
        departure_localisation: departure_localisation,
        departure_time: departure_time,
        arriving_time: arriving_time,
        places: places
    }

    console.log("proposeCarpooling() - poll-service");
    console.log('/api/poll/' + pollId + '/carpooling/');
    return this.http.post<void>('/api/poll/' + pollId + '/carpooling/', carpooling);
  }

  public registration(firstname: string, lastname: string, mail: string, password: string): Observable<Account> {
    return this.http.post('/api/polls/createUser/'+ firstname + '/' + lastname + '/' + mail+ '/' + password, null);
  }

  public connection(mail: string, password: string): Observable<Account> {
    console.log('connection');
    return this.http.post<Account>('/api/polls/connection'+ '/' + mail+ '/' + password, null);
  }

  public getAccount(): Observable<Account> {
    return this.http.get<Account>('/api/polls/getAccount');
  }

  public logout(): Observable<Boolean> {
    return this.http.get<Boolean>('/api/polls/logout');
  }

  public addPoll(mail: string, slug: string, admin: number): Observable<Boolean> {
    return this.http.put<Boolean>('/api/polls/addPoll/'+mail+'/'+slug+'/'+admin, null);
  }

  public getPollAdmin(mail: string): Observable<Poll[]> {
    return this.http.get<Poll[]>('/api/polls/getPollAdmin/'+mail);
  }

  public getPollMember(mail: string): Observable<Poll[]> {
    return this.http.get<Poll[]>('/api/polls/getPollMember/'+mail);
  }

  public changeIcs(mail: string, ics: string): Observable<Boolean> {
    return this.http.put<Boolean>('/api/polls/changeIcs/'+mail+'/'+ics, null);
  }

  public getIcs(mail: string): Observable<string> {
    return this.http.get<string>('/api/polls/getIcs/'+mail);
  }

}
