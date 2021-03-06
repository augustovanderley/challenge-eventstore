package net.intelie.consumer;

import java.util.ArrayList;
import java.util.List;

import net.intelie.model.Event;
import net.intelie.model.EventIterator;

public class EventConsumer {

	private EventIterator queryResult;

	public EventConsumer(EventIterator queryResult) {
		this.queryResult = queryResult;
	}

	public List<Event> extractNextEvents() {
		List<Event> events = new ArrayList<Event>();
		while(queryResult.moveNext()) {
			events.add(queryResult.current());
		}
		return events;
	}

}
