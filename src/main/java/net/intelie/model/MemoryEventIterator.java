package net.intelie.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * An iterator over an event collection memory based. 
 * This iterator is created as a result of calling the method query in MemoryEventStore. 
 *  
 */
public class MemoryEventIterator implements EventIterator {
	/*
	 * This Class consists of: 
	 * 	- A LinkedList of StoredEvents, which contains all the retrieved events. 
	 *  - A StoredEvent to keep track of the current StoredEvent. 
	 *  - An iterator to iterate over the LinkedList 
	 *  - A flag to indicate that the end of the List was reached.
	 * 
	 * The choice for a LinkedList was made because each element points to the next
	 * element, making it easy to iterate over and allowing fast insertions and
	 * deletions in the current element, although those two last functionalities were not
	 * implemented in this class at this moment.
	 */
	
	private LinkedList<StoredEvent> storedEvents;
	private StoredEvent currentStoredEvent = null;
	private Iterator<StoredEvent> iterator;
	private boolean reachedEndofIterator;
	
	
	/**
	 * Create a MemoryEventIterator
	 * 
	 * @param storedEvents
	 */
	public MemoryEventIterator(List<StoredEvent> storedEvents) {
		if(storedEvents == null) throw new IllegalArgumentException();
		this.storedEvents = (LinkedList<StoredEvent>) storedEvents;
		reachedEndofIterator = false;
		iterator = this.storedEvents.iterator();
	}

	
	/**
	 * Close this MemoryEventIterator
	 * 
	 * */
	@Override
	public void close() {
		storedEvents = null;
		iterator = null;
		currentStoredEvent = null;

	}

    /**
     * Move the iterator to the next event, if any.
     *
     * @return false if the iterator has reached the end, true otherwise.
     */
	@Override
	public boolean moveNext() {
		if(storedEvents == null) return false;
		if(!iterator.hasNext()) {
			reachedEndofIterator = true;
			close();
			return false;
		}
		currentStoredEvent = iterator.next();
		return true;
	}

    /**
     * Gets the current event ref'd by this iterator.
     *
     * @return the event itself.
     * @throws IllegalStateException if {@link #moveNext} was never called
     *                               or its last result was {@code false}.
     */
	@Override
	public Event current() {
		if(!hasCurrent()) throw new IllegalStateException();
		return currentStoredEvent.getEvent();
	}
	
    /**
     * Remove current event from its store.
     *
     * @throws IllegalStateException if {@link #moveNext} was never called
     *                               or its last result was {@code false}.
     */
	@Override
	public void remove() {
		/*
		 * As lot of remove calls are expected, removing must be a fast operation. This
		 * method does a Soft Delete, inactivating the current StoredEvent instead of
		 * truly deleting it from the ConcurrentHashMap. This way, removing an event is
		 * O(1).
		 * 
		 * Keeping the inactive StoredEvents helps in logging, keeping track of
		 * modifications and restoration of data.
		 * 
		 * If memory is an issue and StoredEvents should be deleted, then a deleting
		 * service/thread could run in a scheduled interval of time, iterating through
		 * all the elements of the ConcurrentHashMap and deleting any that is inactive.
		 */
		if(!hasCurrent()) throw new IllegalStateException();
		currentStoredEvent.inactive();
	}

	private boolean hasCurrent() {
		return currentStoredEvent != null && !reachedEndofIterator;
	}
}
