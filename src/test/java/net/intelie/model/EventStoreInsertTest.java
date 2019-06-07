package net.intelie.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import net.intelie.model.Event;
import net.intelie.model.EventIterator;
import net.intelie.model.EventStore;
import net.intelie.model.MemoryEventStore;

public class EventStoreInsertTest {

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
    
    @Test
    public void addingAfterRemoveAll() {
    	
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
    	
    	memoryEventStore.insert(new Event("type1", 123L));
    	queryResult = memoryEventStore.query("type1", 122L, 125L);
    	assertTrue(queryResult.moveNext());	
    	retrievedEvent = queryResult.current();
    	assertEquals("type1" , retrievedEvent.type() );
    	assertEquals(123L , retrievedEvent.timestamp() );
    	assertFalse(queryResult.moveNext());	
    }
}
