package net.intelie.model;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;

import net.intelie.builder.EventStoreBuilder;
import net.intelie.consumer.EventConsumer;

public class MemoryEventIteratorRemoveTest {
	
	
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

	@Test(expectedExceptions = IllegalStateException.class)
	public void remove_EmptyListGiven_ShouldThrowException() {
    	queryResult = new EventStoreBuilder().memoryEventStore().query("type1", 33L, 43L);
    	queryResult.remove();
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void remove_MoveNextFalseGiven_ShouldThrowException() {
    	queryResult = new EventStoreBuilder().memoryEventStore().query("type1", 33L, 43L);
    	assertThat(queryResult.moveNext(), is(false));
    	queryResult.remove();
	}
	
	
	@Test
	public void remove_ListWithFiveEventsGiven_ShouldRemoveFirstEvent() {
    	memoryEventStore = new EventStoreBuilder().memoryEventStore().insert(events).create();
    	queryResult = memoryEventStore.query("type1", 33L, 43L);
    	queryResult.moveNext();
    	queryResult.remove();
    	closingEventIterator();
    	queryResult = memoryEventStore.query("type1", 33L, 43L);
    	List<Event> eventsExtracted = new EventConsumer(queryResult).extractNextEvents();
    	assertThat(eventsExtracted, hasSize(4));
    	assertThat(eventsExtracted, contains(event2, event3, event4, event5));
	}
	
	@Test 
	public void remove_ListWithFiveEventsGiven_ShouldRemoveLastEvent() {
    	memoryEventStore = new EventStoreBuilder().memoryEventStore().insert(events).create();
    	queryResult = memoryEventStore.query("type1", 33L, 43L);
    	queryResult.moveNext();
    	queryResult.moveNext();
    	queryResult.moveNext();
    	queryResult.moveNext();
    	queryResult.moveNext();
    	queryResult.remove();
    	closingEventIterator();
    	queryResult = memoryEventStore.query("type1", 33L, 43L);
    	List<Event> eventsExtracted = new EventConsumer(queryResult).extractNextEvents();
    	assertThat(eventsExtracted, hasSize(4));
    	assertThat(eventsExtracted, contains(event1, event2, event3, event4));
	}
	
	@Test 
	public void remove_ListWithFiveEventsGiven_ShouldRemoveAllEvents() {
    	memoryEventStore = new EventStoreBuilder().memoryEventStore().insert(events).create();
    	queryResult = memoryEventStore.query("type1", 33L, 43L);
    	while(queryResult.moveNext()) {
    		queryResult.remove();
    	}
    	closingEventIterator();
    	queryResult = memoryEventStore.query("type1", 33L, 43L);
    	List<Event> eventsExtracted = new EventConsumer(queryResult).extractNextEvents();
    	assertThat(eventsExtracted.isEmpty(), is(true));
	}
	
    @AfterMethod
	public void close() {
    	closingEventIterator();
    }
    
	private void closingEventIterator() {
    	try {
    		if(queryResult != null) queryResult.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
