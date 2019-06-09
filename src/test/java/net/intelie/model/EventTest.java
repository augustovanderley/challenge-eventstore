package net.intelie.model;

import org.junit.Test;

import net.intelie.model.Event;

import static org.junit.Assert.assertEquals;


public class EventTest {
	

    @Test
    public void contructor_ValidValuesGiven_ShouldReturnValidEvent() {
        Event event = new Event("some_type", 123L);

        assertEquals(123L, event.timestamp());
        assertEquals("some_type", event.type());

    }
    
    @Test(expected=IllegalArgumentException.class)
    @SuppressWarnings("unused")
    public void contructor_NullTypeGiven_ShouldReturnException() {
        Event event = new Event(null, 123L);
    }
    
    @Test(expected=IllegalArgumentException.class)
    @SuppressWarnings("unused")
    public void contructor_EmptyTypeGiven_ShouldReturnException() {
        Event event = new Event("", 123L);
    }
    
    @Test(expected=IllegalArgumentException.class)
    @SuppressWarnings("unused")
    public void contructor_NegativeValueGiven_ShouldReturnException() {
        Event event = new Event("", -3L);
    }
    
    @Test(expected=IllegalArgumentException.class)
    @SuppressWarnings("unused")
    public void contructor_ZeroValueGiven_ShouldReturnException() {
        Event event = new Event("", 0L);
    }
    

}