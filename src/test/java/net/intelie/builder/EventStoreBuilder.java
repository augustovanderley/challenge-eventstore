package net.intelie.builder;

import java.util.List;

import net.intelie.model.Event;
import net.intelie.model.EventStore;
import net.intelie.model.MemoryEventStore;

public class EventStoreBuilder {
	private EventStore eventStore;
	
	public EventStoreBuilder memoryEventStore() {
		eventStore = new MemoryEventStore();
		return this;
	}
	
	public EventStoreBuilder insertEvent(Event event) {
		eventStore.insert(event);
		return this;
	}
	
	public EventStoreBuilder insertEvent(List<Event> events) {
		for (Event event : events) {
			eventStore.insert(event);
		}
		return this;
	}
	
	
	public EventStoreBuilder removeAll(String type) {
		eventStore.removeAll(type);
		return this;
	}
}
