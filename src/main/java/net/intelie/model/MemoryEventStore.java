package net.intelie.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryEventStore implements EventStore {

	Map<String, List<Long>> typeToTimestamps = new HashMap<String, List<Long>>();
	
	@Override
	public void insert(Event event) {
		String typeKey = event.type();
		if(typeToTimestamps.containsKey(typeKey)) {
			List<Long> timestamps = typeToTimestamps.get(typeKey);
			timestamps.add(event.timestamp());
		} else {
			List<Long> timestamps = new ArrayList<Long>();
			timestamps.add(event.timestamp());
			typeToTimestamps.put(typeKey, timestamps);
		}
		
		//typeToTimestamps.putIfAbsent(event.type(), new ArrayList<Long>());
		//typeToTimestamps.get(event.type()).add(event.timestamp());

	}

	@Override
	public void removeAll(String type) {
		typeToTimestamps.remove(type);
	}

	@Override
	public EventIterator query(String type, long startTime, long endTime) {
		if(invalidsArguments(type, startTime, endTime)) throw new IllegalArgumentException();
		
		List<Long> timeStamps = typeToTimestamps.get(type);
		
		if(noTimeStampsFound(timeStamps)) return new MemoryEventIterator(new ArrayList<Event>());
		
		List<Event> eventsInInterval = new ArrayList<Event>();
		for (Long singleTimeStamp : timeStamps) {
			if(singleTimeStamp >= startTime && singleTimeStamp < endTime) {
				eventsInInterval.add(new Event(type, singleTimeStamp));
			}
		}
		return new MemoryEventIterator(eventsInInterval);
	}

	private boolean noTimeStampsFound(List<Long> timeStamps) {
		return timeStamps == null || timeStamps.isEmpty();
	}

	private boolean invalidsArguments(String type, long startTime, long endTime) {
		return type == null || type.isEmpty() || startTime < 0 || endTime < 0 || startTime >= endTime;
	}

}
