package net.intelie.challenges;

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
		// TODO Auto-generated method stub

	}

	@Override
	public EventIterator query(String type, long startTime, long endTime) {
		List<Long> timeStamps = typeToTimestamps.get(type);
		List<Event> eventsInInterval = new ArrayList<Event>();
		for (Long singleTimeStamp : timeStamps) {
			if(singleTimeStamp >= startTime && singleTimeStamp < endTime) {
				eventsInInterval.add(new Event(type, singleTimeStamp));
			}
		}
		return new MemoryEventIterator(eventsInInterval);
	}

}
