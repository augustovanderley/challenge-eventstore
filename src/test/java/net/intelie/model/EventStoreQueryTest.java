package net.intelie.model;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.intelie.builder.EventConsumer;
import net.intelie.builder.EventStoreBuilder;

public class EventStoreQueryTest {
	
	private EventStore memoryEventStore;
	List<Event> events = new ArrayList<Event>();
	private Event event1;
	private Event event2;
	private Event event3;
	private Event event4;
	private Event event5;
	
	@Before
	public void setUp() {
		memoryEventStore = new MemoryEventStore();
    	
    	event1 = new Event("type1", 33L);
    	event2 = new Event("type1", 36L);
    	event3 = new Event("type1", 38L);
    	event4 = new Event("type1", 39L);
    	event5 = new Event("type1", 42L);

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
    public void queryNoElementsInserted() {
    	EventIterator queryResult = memoryEventStore.query("type1", 30L, 35L);
    	assertFalse(queryResult.moveNext());
    }
    
    @Test
    public void queryWithAllElementsExcludingLast() {
    	memoryEventStore = new EventStoreBuilder().memoryEventStore().insertEvent(events).create();
    	EventIterator queryResult = memoryEventStore.query("type1", 33L, 42L);
    	List<Event> eventsExtracted = new EventConsumer(queryResult).extractAllEvents();
    	assertThat(eventsExtracted, hasSize(4));
    	assertThat(eventsExtracted, contains(event1, event2, event3, event4));
    	assertThat(eventsExtracted, not(hasItem(event5)));
    }
    
    @Test
    public void queryWithAllElementsIncludingLast() {
    	memoryEventStore = new EventStoreBuilder().memoryEventStore().insertEvent(events).create();
    	EventIterator queryResult = memoryEventStore.query("type1", 33L, 43L);
    	List<Event> eventsExtracted = new EventConsumer(queryResult).extractAllEvents();
    	assertThat(eventsExtracted, hasSize(5));
    	assertThat(eventsExtracted, contains(event1, event2, event3, event4, event5));
    	
    }
    
    @Test
    public void queryReturningNoEvents() {
    	memoryEventStore = new EventStoreBuilder().memoryEventStore().insertEvent(events).create();
    	EventIterator queryResult = memoryEventStore.query("type1", 37L, 38L);
    	List<Event> eventsExtracted = new EventConsumer(queryResult).extractAllEvents();
    	assertThat(eventsExtracted.isEmpty(), is(true));
    }
}
