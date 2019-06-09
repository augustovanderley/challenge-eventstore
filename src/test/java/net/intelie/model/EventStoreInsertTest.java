package net.intelie.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.After;
import org.junit.Test;

import net.intelie.builder.EventStoreBuilder;
import net.intelie.consumer.EventConsumer;

public class EventStoreInsertTest {

    private EventIterator queryResult;

	@Test
    public void insert_EmptyStoreGiven_ShouldReturnAListWith1Event() {
    	Event event = new Event("type1", 123L);

    	queryResult = new EventStoreBuilder().memoryEventStore().insert(event).query("type1", 122L, 124L);
    	assertThat(queryResult.moveNext(), is(true));	
    	assertThat(queryResult.current(), is(event));
    	assertThat(queryResult.moveNext(), is(false));
    }
    
    @Test
    public void insert_SameTypeDiffNumberEventsGiven_ShouldReturnListWith2Events() {
    	Event event = new Event("type1", 123L);
    	Event event2 = new Event("type1", 124L);
    	
    	queryResult = new EventStoreBuilder().memoryEventStore().insert(event)
    			.insert(event2).query("type1", 122L, 125L);
    	
    	
    	List<Event> eventsExtracted = new EventConsumer(queryResult).extractNextEvents();
    	assertThat(eventsExtracted, hasSize(2));
    	assertThat(eventsExtracted, contains(event, event2));
    }
    
    @Test
    public void insert_DiffTypeDiffNumberEventsGiven_ShouldReturn2ListWith1EventEach() {
    	Event event = new Event("type1", 123L);
    	Event event2 = new Event("type2", 124L);

    	
    	EventStore memoryEventStore = new EventStoreBuilder().memoryEventStore().insert(event)
    			.insert(event2).create();
    	
    	queryResult = memoryEventStore.query("type1", 122L, 125L);
    	
    	List<Event> eventsExtracted = new EventConsumer(queryResult).extractNextEvents();
    	assertThat(eventsExtracted, hasSize(1));
    	assertThat(eventsExtracted, contains(event));
    	    	
    	queryResult = memoryEventStore.query("type2", 122L, 125L);
    	eventsExtracted = new EventConsumer(queryResult).extractNextEvents();
    	assertThat(eventsExtracted, hasSize(1));
    	assertThat(eventsExtracted, contains(event2));
    	
    	
    }
    
    @Test 
    public void insert_DiffTypeSameNumberEventsGiven_ShouldReturn2ListWith1EventEach() {
    	Event event = new Event("type1", 124L);
    	Event event2 = new Event("type2", 124L);
    	
    	EventStore memoryEventStore = new EventStoreBuilder().memoryEventStore().insert(event)
    			.insert(event2).create();
    	
    	queryResult = memoryEventStore.query("type1", 122L, 125L);
    	
    	List<Event> eventsExtracted = new EventConsumer(queryResult).extractNextEvents();
    	assertThat(eventsExtracted, hasSize(1));
    	assertThat(eventsExtracted, contains(event));

    	
    	queryResult = memoryEventStore.query("type2", 122L, 125L);
    	eventsExtracted = new EventConsumer(queryResult).extractNextEvents();
    	assertThat(eventsExtracted, hasSize(1));
    	assertThat(eventsExtracted, contains(event2));

    }
    
    @Test
    public void insert_SameTypeSameNumberEventsGiven_ShouldReturn1ListWith2Events() {
    	Event event = new Event("type1", 124L);
    	Event event2 = new Event("type1", 124L);

    	
       	queryResult = new EventStoreBuilder().memoryEventStore().insert(event)
    			.insert(event2).query("type1", 122L, 125L);
       	
    	List<Event> eventsExtracted = new EventConsumer(queryResult).extractNextEvents();
    	assertThat(eventsExtracted, hasSize(2));
    	assertThat(eventsExtracted, contains(event, event2));
    }
    
    @Test
    public void insert_StoreWithTypeRemoveAllGiven_ShouldReturn1ListWith1Event() {
    	

    	Event event = new Event("type1", 123L);
    	Event event2 = new Event("type1", 124L);
    	Event event3 = new Event("type1", 123L);

    	EventStore memoryEventStore = new EventStoreBuilder().memoryEventStore().insert(event)
    			.insert(event2).create();
       	queryResult = memoryEventStore.query("type1", 122L, 125L);
       	List<Event> eventsExtracted = new EventConsumer(queryResult).extractNextEvents();
    	assertThat(eventsExtracted, hasSize(2));
    	assertThat(eventsExtracted, contains(event, event2));
    	
    	memoryEventStore.removeAll("type1");
    	
    	
    	queryResult = memoryEventStore.query("type1", 122L, 125L);
       	eventsExtracted = new EventConsumer(queryResult).extractNextEvents();
    	assertThat(eventsExtracted.isEmpty(), is(true));
    	
    	memoryEventStore.insert(event3);
    	queryResult = memoryEventStore.query("type1", 122L, 125L);
       	eventsExtracted = new EventConsumer(queryResult).extractNextEvents();
    	assertThat(eventsExtracted, hasSize(1));
    	assertThat(eventsExtracted, contains(event3));
    }
    
    @After
    public void close() {
    	try {
    		if(queryResult != null) queryResult.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
