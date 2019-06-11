package net.intelie.model;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class StoredEvent {

	private static AtomicLong nextId = new AtomicLong();
	
	private final long id;
	private final Event event;
	private volatile boolean active;
	
	public StoredEvent(Event event) {
		if(event == null) throw new IllegalArgumentException();
		this.id = nextId.incrementAndGet();
		this.event = event;
		active = true;
	}

	public long retrieveTimeStamp() {
		return event.timestamp();
	}
	
	public Event getEvent() {
		return event;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof StoredEvent)) {
			return false;
		}
		StoredEvent other = (StoredEvent) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

	public synchronized boolean isActive() {
		return active;
	}

	public synchronized void inactive() {
		active = false;
	}
	
	
}
