package net.intelie.builder;

import java.util.List;

import net.intelie.model.Event;
import net.intelie.model.EventIterator;
import net.intelie.model.EventStore;
import net.intelie.model.MemoryEventStore;

public class EventStoreBuilder {
	private EventStore eventStore;
	private EventIterator query;
	
	public EventStoreBuilder memoryEventStore() {
		eventStore = new MemoryEventStore();
		return this;
	}
	
	public EventStoreBuilder insert(Event event) {
		eventStore.insert(event);
		return this;
	}
	
	public EventStoreBuilder insert(List<Event> events) {
		for (Event event : events) {
			eventStore.insert(event);
		}
		return this;
	}
	
	
	public EventStoreBuilder removeAll(String type) {
		eventStore.removeAll(type);
		return this;
	}
	
	
	public EventIterator query(String type, long start, long end) {
		query = eventStore.query(type, start, end);
		return query;
	}
	public EventStore create() {
		return eventStore;
	}
}
