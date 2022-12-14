export interface Poll {
 createdAt?: Date;
 description?: string;
 has_meal?: boolean;
 id?: number;
 location?: string;
 padURL?: string;
 pollChoices?: PollChoice[];
 selectedChoice ?: PollChoice;
 pollComments?: PollCommentElement[];
 pollMealPreferences?: PollCommentElement[];
 slug?: string;
 slugAdmin?: string;
 title?: string;
 tlkURL?: string;
 updatedAt?: Date;
 clos ?: boolean;
}

export interface PollChoice {
 endDate?: Date;
 id?: number;
 startDate?: Date;
 users?: User[];
}

export interface User {
 id?: number;
 username?: string;
 mail?: string;
}

export interface ChoiceUser {
  username?: string;
  mail?: string;
  pref?: string;
  ics?: string;
  choices?: number[];
 }

export interface PollCommentElement {
 content?: string;
 id?: number;
 auteur?: string;
}

export interface EventDTO{
  startDate?: Date;
  endDate?: Date;
  description?: string;
}


export interface EventDTOAndSelectedChoice {
  eventdtos?: EventDTO[];
  selectedChoices?: number[];
}
