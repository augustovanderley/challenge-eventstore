package net.intelie.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.After;
import org.junit.Test;

import net.intelie.builder.EventStoreBuilder;
import net.intelie.consumer.EventConsumer;

public class EventStoreRemoveAllTest {
    private EventIterator queryResult;

	@Test
    public void removeAllEmptyStore() {
    	
    	EventStore memoryEventStore = new MemoryEventStore();
    	
    	memoryEventStore.removeAll("type1");
    	
    	queryResult = memoryEventStore.query("type1", 122L, 124L);
    	List<Event> eventsExtracted = new EventConsumer(queryResult).extractNextEvents();
    	assertThat(eventsExtracted.isEmpty(), is(true));	
    }
    
    @Test
    public void removeAll_NotEmptyTypeStoreGiven_ShouldReturnEmptyTypeStore() {
    	
    	EventStore memoryEventStore = new EventStoreBuilder().memoryEventStore().insert(new Event("type1", 123L))
    			.insert(new Event("type1", 124L)).create();
    	queryResult = memoryEventStore.query("type1", 122L, 125L);
    	
    	memoryEventStore.removeAll("type1");
    	
    	queryResult = memoryEventStore.query("type1", 122L, 125L);
    	List<Event> eventsExtracted = new EventConsumer(queryResult).extractNextEvents();
    	assertThat(eventsExtracted.isEmpty(), is(true));
  
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
