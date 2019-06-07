package net.intelie.model;

import java.util.concurrent.atomic.AtomicInteger;

public class StoredEvent {

	private final static AtomicInteger id  = new AtomicInteger();
	private final Event event;
	private boolean active;
	
	public StoredEvent(Event event) {
		this.event = event;
		active = true;
		id.incrementAndGet();
		System.out.println(id);
	}

	public long retrieveTimeStamp() {
		return event.timestamp();
	}
	
	public Event getEvent() {
		return event;
		
	}
}
