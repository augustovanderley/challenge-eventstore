package net.intelie.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;

import net.intelie.model.Event;
import net.intelie.model.EventIterator;
import net.intelie.model.EventStore;
import net.intelie.model.MemoryEventStore;

public class EventStoreInsertTest {

    private EventIterator queryResult;

	@Test
    public void insert_EmptyStoreGiven_ShouldReturnAListWith1Event() {
    	Event event = new Event("type1", 123L);
    	
    	EventStore memoryEventStore = new MemoryEventStore();
    	memoryEventStore.insert(event);
    	
    	queryResult = memoryEventStore.query("type1", 122L, 124L);
    	assertTrue(queryResult.moveNext());	
		Event retrievedEvent = queryResult.current();
    	assertEquals("type1" , retrievedEvent.type() );
    	assertEquals(123L , retrievedEvent.timestamp() );
    	assertFalse(queryResult.moveNext() );
    }
    
    @Test
    public void insert_SameTypeDiffNumberEventsGiven_ShouldReturnListWith2Events() {
    	Event event = new Event("type1", 123L);
    	Event event2 = new Event("type1", 124L);
    	
    	EventStore memoryEventStore = new MemoryEventStore();
    	memoryEventStore.insert(event);
    	memoryEventStore.insert(event2);
    	
    	queryResult = memoryEventStore.query("type1", 122L, 125L);
    	assertTrue(queryResult.moveNext());	
		Event retrievedEvent = queryResult.current();
    	assertEquals("type1" , retrievedEvent.type() );
    	assertEquals(123L , retrievedEvent.timestamp() );
    	assertTrue(queryResult.moveNext());
    	retrievedEvent = queryResult.current();
    	assertEquals("type1" , retrievedEvent.type() );
    	assertEquals(124L , retrievedEvent.timestamp() );
    	assertFalse(queryResult.moveNext() );
    }
    
    @Test
    public void insert_DiffTypeDiffNumberEventsGiven_ShouldReturn2ListWith1EventEach() {
    	Event event = new Event("type1", 123L);
    	Event event2 = new Event("type2", 124L);
    	
    	EventStore memoryEventStore = new MemoryEventStore();
    	memoryEventStore.insert(event);
    	memoryEventStore.insert(event2);
    	
    	queryResult = memoryEventStore.query("type1", 122L, 125L);
    	assertTrue(queryResult.moveNext());	
		Event retrievedEvent = queryResult.current();
    	assertEquals("type1" , retrievedEvent.type() );
    	assertEquals(123L , retrievedEvent.timestamp() );
    	assertFalse(queryResult.moveNext() );
    	
    	queryResult = memoryEventStore.query("type2", 122L, 125L);
    	assertTrue(queryResult.moveNext());	
		retrievedEvent = queryResult.current();
    	assertEquals("type2" , retrievedEvent.type() );
    	assertEquals(124L , retrievedEvent.timestamp() );
    	assertFalse(queryResult.moveNext() );
    	
    	
    }
    
    @Test 
    public void insert_DiffTypeSameNumberEventsGiven_ShouldReturn2ListWith1EventEach() {
    	Event event = new Event("type1", 124L);
    	Event event2 = new Event("type2", 124L);
    	
    	EventStore memoryEventStore = new MemoryEventStore();
    	memoryEventStore.insert(event);
    	memoryEventStore.insert(event2);
    	
    	queryResult = memoryEventStore.query("type1", 122L, 125L);
    	assertTrue(queryResult.moveNext());	
		Event retrievedEvent = queryResult.current();
    	assertEquals("type1" , retrievedEvent.type() );
    	assertEquals(124L , retrievedEvent.timestamp() );
    	assertFalse(queryResult.moveNext() );	
    	
    	queryResult = memoryEventStore.query("type2", 122L, 125L);
    	assertTrue(queryResult.moveNext());	
		retrievedEvent = queryResult.current();
    	assertEquals("type2" , retrievedEvent.type() );
    	assertEquals(124L , retrievedEvent.timestamp() );
    	assertFalse(queryResult.moveNext() );	
    }
    
    @Test
    public void insert_SameTypeSameNumberEventsGiven_ShouldReturn1ListWith2Events() {
    	Event event = new Event("type1", 124L);
    	Event event2 = new Event("type1", 124L);
    	
    	EventStore memoryEventStore = new MemoryEventStore();
    	memoryEventStore.insert(event);
    	memoryEventStore.insert(event2);
    	
    	queryResult = memoryEventStore.query("type1", 122L, 125L);
    	assertTrue(queryResult.moveNext());	
		Event retrievedEvent = queryResult.current();
    	assertEquals("type1" , retrievedEvent.type() );
    	assertEquals(124L , retrievedEvent.timestamp() );
    	assertTrue(queryResult.moveNext());	
		retrievedEvent = queryResult.current();
    	assertEquals("type1" , retrievedEvent.type() );
    	assertEquals(124L , retrievedEvent.timestamp() );
    	assertFalse(queryResult.moveNext() );	
    }
    
    @Test
    public void insert_StoreWithTypeRemovedGiven_ShouldReturn1ListWith1Event() {
    	
    	EventStore memoryEventStore = new MemoryEventStore();
    	memoryEventStore.insert(new Event("type1", 123L));
    	memoryEventStore.insert(new Event("type1", 124L));
    	
    	queryResult = memoryEventStore.query("type1", 122L, 125L);
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
    	
    	memoryEventStore.insert(new Event("type1", 123L));
    	queryResult = memoryEventStore.query("type1", 122L, 125L);
    	assertTrue(queryResult.moveNext());	
    	retrievedEvent = queryResult.current();
    	assertEquals("type1" , retrievedEvent.type() );
    	assertEquals(123L , retrievedEvent.timestamp() );
    	assertFalse(queryResult.moveNext());	
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
