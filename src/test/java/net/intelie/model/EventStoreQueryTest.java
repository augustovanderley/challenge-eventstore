package net.intelie.model;


import static org.testng.AssertJUnit.assertFalse;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import java.util.ArrayList;
import java.util.List;

import net.intelie.builder.EventStoreBuilder;
import net.intelie.consumer.EventConsumer;

public class EventStoreQueryTest {
	
	private EventStore memoryEventStore;
	List<Event> events;
	private Event event1;
	private Event event2;
	private Event event3;
	private Event event4;
	private Event event5;
	private EventIterator queryResult;
	
	@BeforeMethod
	public void setUp() {
		memoryEventStore = new MemoryEventStore();
    	event1 = new Event("type1", 33L);
    	event2 = new Event("type1", 36L);
    	event3 = new Event("type1", 38L);
    	event4 = new Event("type1", 39L);
    	event5 = new Event("type1", 42L);
    	events = new ArrayList<Event>();
    	events.add(event1);
		events.add(event2);
		events.add(event3);
		events.add(event4);
		events.add(event5);
		
	}
	
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void query_EmptyStringGiven_ShouldThrowException() {
    	memoryEventStore.query("", 122L, 124L);
    }
    
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void query_NullStringGiven_ShouldThrowException() {
    	memoryEventStore.query(null, 122L, 124L);
    }
    
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void query_NegativeStartTimeGiven_ShouldThrowException(){    	
    	memoryEventStore.query("type1", -3L, 3L);
    }
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void query_NegativeEndingTimeGiven_ShouldThrowException() {    	
    	memoryEventStore.query("type1", -3L, -2L);
    }
    
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void query_EndingBeforeStartGiven_ShouldThrowException() {
    	memoryEventStore.query("type1", 30L, 10L);
    }
    
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void query_StartSameEndingGiven_ShouldThrowException() {
    	memoryEventStore.query("type1", 30L, 30L);
    }
        
    @Test
    public void query_NoElementsInsertedGiven_ShouldReturnEmptyIterator() {
    	queryResult = memoryEventStore.query("type1", 30L, 35L);
    	assertFalse(queryResult.moveNext());
    }
    
    @Test
    public void query_LastEventTimestampEqualToEndTimeGiven_ShouldReturnListWithoutLastEvent() {
    	memoryEventStore = new EventStoreBuilder().memoryEventStore().insert(events).create();
    	queryResult = memoryEventStore.query("type1", 33L, 42L);
    	List<Event> eventsExtracted = new EventConsumer(queryResult).extractNextEvents();
    	assertThat(eventsExtracted, hasSize(4));
    	assertThat(eventsExtracted, contains(event1, event2, event3, event4));
    	assertThat(eventsExtracted, not(hasItem(event5)));
    }
    
    @Test
    public void  query_LastEventTimestampLessThanEndTimeGiven_ShouldReturnListWithLastEvent() {
    	memoryEventStore = new EventStoreBuilder().memoryEventStore().insert(events).create();
    	queryResult = memoryEventStore.query("type1", 33L, 43L);
    	List<Event> eventsExtracted = new EventConsumer(queryResult).extractNextEvents();
    	assertThat(eventsExtracted, hasSize(5));
    	assertThat(eventsExtracted, contains(event1, event2, event3, event4, event5));
    	
    }
    
    @Test
    public void query_NoEventsBetweenStartAndEndGiven_ShouldReturnEmptyList() {
    	memoryEventStore = new EventStoreBuilder().memoryEventStore().insert(events).create();
    	queryResult = memoryEventStore.query("type1", 37L, 38L);
    	List<Event> eventsExtracted = new EventConsumer(queryResult).extractNextEvents();
    	assertThat(eventsExtracted.isEmpty(), is(true));
    }
    
    @AfterMethod
	public void close() {
    	try {
    		if(queryResult != null) queryResult.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
