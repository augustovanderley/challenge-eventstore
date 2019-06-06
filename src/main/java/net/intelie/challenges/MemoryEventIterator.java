package net.intelie.challenges;

import java.util.ArrayList;
import java.util.List;

public class MemoryEventIterator implements EventIterator {
	
	private List<Event> events = new ArrayList<Event>();
	private int index = -1;
	private boolean status;
	public MemoryEventIterator(List<Event> events) {
		this.events = events;
		status = false;
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean moveNext() {
		if(events.isEmpty() || (index + 1) >= events.size()) {
			status = false;
			return false;
		}
		index++;
		status = true;
		return true;
	}

	@Override
	public Event current() {
		if (index < 0 || status == false) {
			//TODO CHECK 
			//if {@link #moveNext} was never called
		    // *                               or its last result was {@code false}.
			throw new IllegalStateException();
		}
		return events.get(index);
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}

}
