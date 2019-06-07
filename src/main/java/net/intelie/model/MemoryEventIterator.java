package net.intelie.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MemoryEventIterator implements EventIterator {
	//TODO MUST BE A LINKED LIST
	
	
	private final LinkedList<StoredEvent> storedEvents;
	private StoredEvent currentStoredEvent = null;
	private Iterator<StoredEvent> iterator;
	private boolean reachedEndofIterator;
	
	
	
	public MemoryEventIterator(List<StoredEvent> storedEvents) {
		this.storedEvents = (LinkedList<StoredEvent>) storedEvents;
		reachedEndofIterator = false;
		iterator = this.storedEvents.iterator();

	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean moveNext() {
		if(!iterator.hasNext()) {
			reachedEndofIterator = true;
			return false;
		}
		currentStoredEvent = iterator.next();
		return true;
	}

	@Override
	public Event current() {
		if(currentStoredEvent == null || reachedEndofIterator) {
			throw new IllegalStateException();
		}
		return currentStoredEvent.getEvent();
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}

}
