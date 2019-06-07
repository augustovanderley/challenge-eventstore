package net.intelie.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import net.intelie.model.Event;
import net.intelie.model.EventIterator;
import net.intelie.model.EventStore;
import net.intelie.model.MemoryEventStore;

public class EventStoreRemoveAllTest {
    @Test
    public void removeAllEmptyStore() {
    	
    	EventStore memoryEventStore = new MemoryEventStore();
    	
    	memoryEventStore.removeAll("type1");
    	
    	EventIterator queryResult = memoryEventStore.query("type1", 122L, 124L);
    	assertFalse(queryResult.moveNext());	
    }
    
    @Test
    public void removeAllFilledStore() {
    	
    	EventStore memoryEventStore = new MemoryEventStore();
    	memoryEventStore.insert(new Event("type1", 123L));
    	memoryEventStore.insert(new Event("type1", 124L));
    	
    	EventIterator queryResult = memoryEventStore.query("type1", 122L, 125L);
    	assertTrue(queryResult.moveNext());	
    	Event retrievedEvent = queryResult.current();
    	assertEquals("type1" , retrievedEvent.type() );
    	assertEquals(123L , retrievedEvent.timestamp() );
    	assertTrue(queryResult.moveNext());	
    	retrievedEvent = queryResult.current();
    	assertEquals("type1" , retrievedEvent.type() );
    	assertEquals(124L , retrievedEvent.timestamp() );
    	
    	
    	memoryEventStore.removeAll("type1");
    	
    	queryResult = memoryEventStore.query("type1", 122L, 125L);
    	assertFalse(queryResult.moveNext());	
    }
}
