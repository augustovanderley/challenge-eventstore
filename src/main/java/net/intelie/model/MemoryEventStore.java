package net.intelie.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryEventStore implements EventStore {

	ConcurrentHashMap<String, List<StoredEvent>> typeToTimestamps = new ConcurrentHashMap<String, List<StoredEvent>>();
	
	@Override
	public void insert(Event event) {
		typeToTimestamps.putIfAbsent(event.type(), Collections.synchronizedList(new ArrayList<StoredEvent>()));
		typeToTimestamps.get(event.type()).add(new StoredEvent(event));

		
	}

	@Override
	public void removeAll(String type) {
		typeToTimestamps.remove(type);
	}

	@Override
	public EventIterator query(String type, long startTime, long endTime) {
		if(invalidsArguments(type, startTime, endTime)) throw new IllegalArgumentException();
		
		List<StoredEvent> storedEventsFromType = typeToTimestamps.get(type);
		if(notFound(storedEventsFromType)) return new MemoryEventIterator(new LinkedList<StoredEvent>());
		
		return new MemoryEventIterator(retrieveStoredEvents(storedEventsFromType, startTime, endTime));
	}

	private LinkedList<StoredEvent> retrieveStoredEvents(List<StoredEvent> storedEventsFromType , long startTime, long endTime) {

		LinkedList<StoredEvent> eventsInInterval = new LinkedList<StoredEvent>();
		storedEventsFromType
			.stream()
			.filter(se -> (se.isActive() && (se.retrieveTimeStamp() >= startTime && se.retrieveTimeStamp() < endTime)))
			.forEach(eventsInInterval::add);
		return eventsInInterval;
	}

	private boolean notFound(List<StoredEvent> storedEvents) {
		return storedEvents == null || storedEvents.isEmpty();
	}

	private boolean invalidsArguments(String type, long startTime, long endTime) {
		return type == null || type.isEmpty() || startTime < 0  || startTime >= endTime;
	}

}
