package net.intelie.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An EventStore implementation that stores events in memory.
 * <p>
 */
public class MemoryEventStore implements EventStore {

	
	/*
	 * In order to store events, a ConcurrentHashMap was chosen, having the event
	 * type String as key and a List of StoredEvent with this type as value. This
	 * way, events are grouped by type. With this Collection, put and get operations
	 * are O(1). Thus, adding new types and access to the List of StoredEvent are
	 * O(1).
	 * 
	 * ConcurrentHashMap guarantees full concurrency of retrieval, having all operations thread-safe.
	 */
	ConcurrentHashMap<String, List<StoredEvent>> typeToTimestamps = new ConcurrentHashMap<String, List<StoredEvent>>();
	
	
    /**
     * Stores an event
     *
     * @param event
     */
	@Override
	public synchronized void insert(Event event) {
		/*
		 * This action must be really fast, as many insertions are expected. Using
		 * the ConcurrentHashMap collection, each key insertion (put) is O(1). 
		 * Furthermore, new events from an existing type are added in the end of the List of Stored Event. 
		 * Appending to the end of a list takes O(1). Thus, this method takes O(1).  
		 * 
		 * This method must be synchronized, as thread interference must be blocked. Each new key has as value a synchronizedList. 
		 */
		if(event == null) throw new IllegalArgumentException();
		typeToTimestamps.putIfAbsent(event.type(), Collections.synchronizedList(new ArrayList<StoredEvent>()));
		typeToTimestamps.get(event.type()).add(new StoredEvent(event));
	}

    /**
     * Removes all events of specific type.
     *
     * @param type
     */
	@Override
	public void removeAll(String type) {
		/*
		 * Removing a key and its corresponding value from a ConcurrentHashMap takes O(1).
		 * So, this method takes O(1).
		 * 
		 * A synchronize statement is not needed, as the remove operation is thread-safe.
		 */
		if(type == null || type == "") throw new IllegalArgumentException();
		typeToTimestamps.remove(type);
	}

    /**
     * Retrieves an iterator for events based on their type and timestamp.
     *
     * @param type      The type we are querying for.
     * @param startTime Start timestamp (inclusive).
     * @param endTime   End timestamp (exclusive).
     * @return An iterator where all its events have same type as
     * {@param type} and timestamp between {@param startTime}
     * (inclusive) and {@param endTime} (exclusive).
     */
	@Override
	public EventIterator query(String type, long startTime, long endTime) {
		/*
		 * 
		 * ConcurrentHashMap takes O(1) to retrieve a List<StoredEvent> from a specific
		 * type. This action iterates over a List of values from a specified event type,
		 * filtering events. A list traversal takes O(n). So this method takes O(n) to
		 * filter all the events.
		 * 
		 * This method accesses events that are not ordered by timestamp and returns an iterator with unordered events. 
		 * Should the iterator return ordered elements, others solutions would be best suited.
		 * 
		 * 
		 */
		
		if(invalidsArguments(type, startTime, endTime)) throw new IllegalArgumentException();
		 
		List<StoredEvent> storedEventsFromType = typeToTimestamps.get(type);
		if(notFound(storedEventsFromType)) return emptyIterator();
		
		return new MemoryEventIterator(filterStoredEvents(storedEventsFromType, startTime, endTime));
	}
	
	private boolean notFound(List<StoredEvent> storedEvents) {
		return storedEvents == null || storedEvents.isEmpty();
	}

	private MemoryEventIterator emptyIterator() {
		return new MemoryEventIterator(new LinkedList<StoredEvent>());
	}

	private LinkedList<StoredEvent> filterStoredEvents(List<StoredEvent> storedEventsFromType, long startTime,
			long endTime) {
		/*
		 * Reading from a ConcurrentHashMap is thread-safe as long as inner elements are
		 * also thread-safe. Thus, the List of StoredEvent had to make use of an
		 * Iterator and a synchronized statement blocking the List, in order to filter elements.   
		 * 
		 */
		LinkedList<StoredEvent> eventsInInterval = new LinkedList<StoredEvent>();


		synchronized (storedEventsFromType) {
			Iterator<StoredEvent> i = storedEventsFromType.iterator();
			while (i.hasNext()) {
				StoredEvent storedEvent = i.next();
				if (storedEvent.isActive() && (storedEvent.retrieveTimeStamp() >= startTime
						&& storedEvent.retrieveTimeStamp() < endTime)) {
					eventsInInterval.add(storedEvent);
				}
			}
		}

		return eventsInInterval;
	}

	private boolean invalidsArguments(String type, long startTime, long endTime) {
		return type == null || type.isEmpty() || startTime < 0  || startTime >= endTime;
	}

}
