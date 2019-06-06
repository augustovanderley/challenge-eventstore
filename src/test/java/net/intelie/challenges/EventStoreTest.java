package net.intelie.challenges;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EventStoreTest {

    @Test
    public void addingEmptyStore() {
    	Event event = new Event("type1", 123L);
    	
    	EventStore memoryEventStore = new MemoryEventStore();
    	memoryEventStore.insert(event);
    	
    	EventIterator queryResult = memoryEventStore.query("type1", 122L, 124L);
    	assertTrue(queryResult.moveNext());	
		Event retrievedEvent = queryResult.current();
    	assertEquals("type1" , retrievedEvent.type() );
    	assertEquals(123L , retrievedEvent.timestamp() );
    	assertFalse(queryResult.moveNext() );
    }
    
    @Test
    public void addingSameTypeDiffNumber() {
    	Event event = new Event("type1", 123L);
    	Event event2 = new Event("type1", 124L);
    	
    	EventStore memoryEventStore = new MemoryEventStore();
    	memoryEventStore.insert(event);
    	memoryEventStore.insert(event2);
    	
    	EventIterator queryResult = memoryEventStore.query("type1", 122L, 125L);
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
    public void addingDiffTypeDiffNumber() {
    	Event event = new Event("type1", 123L);
    	Event event2 = new Event("type2", 124L);
    	
    	EventStore memoryEventStore = new MemoryEventStore();
    	memoryEventStore.insert(event);
    	memoryEventStore.insert(event2);
    	
    	EventIterator queryResult = memoryEventStore.query("type1", 122L, 125L);
    	assertTrue(queryResult.moveNext());	
		Event retrievedEvent = queryResult.current();
    	assertEquals("type1" , retrievedEvent.type() );
    	assertEquals(123L , retrievedEvent.timestamp() );
    	assertFalse(queryResult.moveNext() );
    	
    	
    }
    
    @Test
    public void addingDiffTypeSameNumber() {
    	Event event = new Event("type1", 124L);
    	Event event2 = new Event("type2", 124L);
    	
    	EventStore memoryEventStore = new MemoryEventStore();
    	memoryEventStore.insert(event);
    	memoryEventStore.insert(event2);
    	
    	EventIterator queryResult = memoryEventStore.query("type1", 122L, 125L);
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
    public void addingSameTypeSameNumber() {
    	Event event = new Event("type1", 124L);
    	Event event2 = new Event("type1", 124L);
    	
    	EventStore memoryEventStore = new MemoryEventStore();
    	memoryEventStore.insert(event);
    	memoryEventStore.insert(event2);
    	
    	EventIterator queryResult = memoryEventStore.query("type1", 122L, 125L);
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
}
