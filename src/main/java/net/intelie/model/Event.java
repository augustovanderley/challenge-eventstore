package net.intelie.model;

/**
 * This is just an event stub, feel free to expand it if needed.
 */
public class Event {
    private final String type;
    private final long timestamp;

    public Event(String type, long timestamp) {
    	if(type == null || type.isEmpty() || timestamp <= 0L) throw new IllegalArgumentException();
        this.type = type;
        this.timestamp = timestamp;
    }

    public String type() {
        return type;
    }

    public long timestamp() {
        return timestamp;
    }
}
