package net.intelie.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MemoryEventIterator implements EventIterator {
	
	
	private LinkedList<StoredEvent> storedEvents;
	private StoredEvent currentStoredEvent = null;
	private Iterator<StoredEvent> iterator;
	private boolean reachedEndofIterator;
	
	
	
	public MemoryEventIterator(List<StoredEvent> storedEvents) {
		if(storedEvents == null) throw new IllegalArgumentException();
		this.storedEvents = (LinkedList<StoredEvent>) storedEvents;
		reachedEndofIterator = false;
		iterator = this.storedEvents.iterator();
	}

	@Override
	public void close() {
		storedEvents = null;
		currentStoredEvent = null;

	}

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

	@Override
	public Event current() {
		if(!hasCurrent()) throw new IllegalStateException();
		return currentStoredEvent.getEvent();
	}
	

	@Override
	public void remove() {
		if(!hasCurrent()) throw new IllegalStateException();
		currentStoredEvent.inactive();
	}

	private boolean hasCurrent() {
		return currentStoredEvent != null && !reachedEndofIterator;
	}
}
