package net.intelie.model;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.intelie.builder.EventStoreBuilder;

public class EventStoreQueryTest {
	
	private EventStore memoryEventStore;
	List<Event> events = new ArrayList<Event>();
	
	@Before
	public void setUp() {
		memoryEventStore = new MemoryEventStore();
    	
    	Event event1 = new Event("type1", 33L);
    	Event event2 = new Event("type1", 36L);
    	Event event3 = new Event("type1", 38L);
    	Event event4 = new Event("type1", 39L);
    	Event event5 = new Event("type1", 42L);

    	events.add(event1);
		events.add(event2);
		events.add(event3);
		events.add(event4);
		events.add(event5);
	}
	
    @Test(expected=IllegalArgumentException.class)
    public void queryEmptyString() {
    	memoryEventStore.query("", 122L, 124L);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void queryNullString() {
    	memoryEventStore.query(null, 122L, 124L);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void queryNegativeStartTime() {    	
    	memoryEventStore.query("type1", -3L, 3L);
    }
    @Test(expected=IllegalArgumentException.class)
    public void queryNegativeEndingTime() {    	
    	memoryEventStore.query("type1", -3L, -2L);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void queryEndingBeforeStart() {
    	memoryEventStore.query("type1", 30L, 10L);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void queryStartSameEnding() {
    	memoryEventStore.query("type1", 30L, 30L);
    }
    
    @Test
    public void queryNoElements() {
    	EventIterator queryResult = memoryEventStore.query("type1", 30L, 35L);
    	assertFalse(queryResult.moveNext());
    }
    
    @Test
    public void queryWithElementsInBorder() {
    	memoryEventStore = new EventStoreBuilder().memoryEventStore().insertEvent(events).create();
    	EventIterator queryResult = memoryEventStore.query("type1", 33L, 42L);
    	assertTrue(queryResult.moveNext());
    }
}
