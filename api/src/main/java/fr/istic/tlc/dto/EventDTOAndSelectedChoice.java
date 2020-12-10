package fr.istic.tlc.dto;

import java.util.List;

public class EventDTOAndSelectedChoice {
	
	List<EventDTO> eventdtos;
	List<Long> selectedChoices;
	public List<EventDTO> getEventdtos() {
		return eventdtos;
	}
	public void setEventdtos(List<EventDTO> eventdtos) {
		this.eventdtos = eventdtos;
	}
	public List<Long> getSelectedChoices() {
		return selectedChoices;
	}
	public void setSelectedChoices(List<Long> selectedChoices) {
		this.selectedChoices = selectedChoices;
	}

}
