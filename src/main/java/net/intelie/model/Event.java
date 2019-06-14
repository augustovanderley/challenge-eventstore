package net.intelie.model;

import java.util.concurrent.atomic.AtomicLong;

/**
 * An event class. Must be created with a type and a timestamp. Each event gets a unique id.
 */
public class Event {
	private static AtomicLong nextId = new AtomicLong();
	
	private final long id;
    private final String type;
    private final long timestamp;

    public Event(String type, long timestamp) {
    	if(type == null || type.isEmpty() || timestamp <= 0L) throw new IllegalArgumentException();
    	this.id = nextId.getAndIncrement();
        this.type = type;
        this.timestamp = timestamp;
        
    }

    public String type() {
        return type;
    }

    public long timestamp() {
        return timestamp;
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
		if (!(obj instanceof Event)) {
			return false;
		}
		Event other = (Event) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}
    
    @Override
    	public String toString() {
    		return "Event id: " + id +  ", type: " + type + ", timestamp: " + timestamp; 
    	}
    
    
}
