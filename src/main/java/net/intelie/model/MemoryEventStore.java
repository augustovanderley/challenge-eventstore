package net.intelie.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MemoryEventStore implements EventStore {

	Map<String, List<StoredEvent>> typeToTimestamps = new HashMap<String, List<StoredEvent>>();
	
	@Override
	public void insert(Event event) {
		typeToTimestamps.putIfAbsent(event.type(), new ArrayList<StoredEvent>());
		typeToTimestamps.get(event.type()).add(new StoredEvent(event));
	}

	@Override
	public void removeAll(String type) {
		typeToTimestamps.remove(type);
	}

	@Override
	public EventIterator query(String type, long startTime, long endTime) {
		if(invalidsArguments(type, startTime, endTime)) throw new IllegalArgumentException();
		
		List<StoredEvent> storedEvents = typeToTimestamps.get(type);
		
		if(noTimeStampsFound(storedEvents)) return new MemoryEventIterator(new LinkedList<StoredEvent>());
		
		List<StoredEvent> eventsInInterval = new LinkedList<StoredEvent>();
		
		for (StoredEvent storedEvent : storedEvents) {
			if(storedEvent.retrieveTimeStamp() >= startTime && storedEvent.retrieveTimeStamp() < endTime) {
				eventsInInterval.add(storedEvent);
			}
		}
		return new MemoryEventIterator(eventsInInterval);
	}

	private boolean noTimeStampsFound(List<StoredEvent> storedEvents) {
		return storedEvents == null || storedEvents.isEmpty();
	}

	private boolean invalidsArguments(String type, long startTime, long endTime) {
		return type == null || type.isEmpty() || startTime < 0 || endTime < 0 || startTime >= endTime;
	}

}
