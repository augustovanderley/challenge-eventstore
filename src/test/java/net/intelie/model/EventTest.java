package net.intelie.model;

import org.junit.Test;

import net.intelie.model.Event;

import static org.junit.Assert.assertEquals;

public class EventTest {
	

    @Test
    public void contructorTest() {
        Event event = new Event("some_type", 123L);

        assertEquals(123L, event.timestamp());
        assertEquals("some_type", event.type());
    }
    
    @Test(expected=IllegalArgumentException.class)
    @SuppressWarnings("unused")
    public void contructorNullType() {
        Event event = new Event(null, 123L);
    }
    
    @Test(expected=IllegalArgumentException.class)
    @SuppressWarnings("unused")
    public void contructorEmptyType() {
        Event event = new Event("", 123L);
    }
    
    @Test(expected=IllegalArgumentException.class)
    @SuppressWarnings("unused")
    public void contructorNegativeValue() {
        Event event = new Event("", -3L);
    }
    
    @Test(expected=IllegalArgumentException.class)
    @SuppressWarnings("unused")
    public void contructorZeroValue() {
        Event event = new Event("", 0L);
    }
    

}