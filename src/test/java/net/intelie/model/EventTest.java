package net.intelie.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.Test;


public class EventTest {
	

    @Test
    public void contructor_ValidValuesGiven_ShouldReturnValidEvent() {
        Event event = new Event("some_type", 123L);

        assertThat(event.timestamp(), is(123L));
        assertThat(event.type(), is("some_type"));

    }
    
    @Test(expectedExceptions=IllegalArgumentException.class)
    @SuppressWarnings("unused")
    public void contructor_NullTypeGiven_ShouldReturnException() {
        Event event = new Event(null, 123L);
    }
    
    @Test(expectedExceptions=IllegalArgumentException.class)
    @SuppressWarnings("unused")
    public void contructor_EmptyTypeGiven_ShouldReturnException() {
        Event event = new Event("", 123L);
    }
    
    @Test(expectedExceptions=IllegalArgumentException.class)
    @SuppressWarnings("unused")
    public void contructor_NegativeValueGiven_ShouldReturnException() {
        Event event = new Event("", -3L);
    }
    
    @Test(expectedExceptions=IllegalArgumentException.class)
    @SuppressWarnings("unused")
    public void contructor_ZeroValueGiven_ShouldReturnException() {
        Event event = new Event("", 0L);
    }
    
    
    @Test
    public void equals_TwoDiffEventsGiven_ShouldReturnFalse() {
    	Event event = new Event("type", 5L);
    	Event event2 = new Event("type", 5L);
        assertThat(event.equals(event2), is(false));
    }
    
    @Test
    public void equals_SameEventGiven_ShouldReturnTrue() {
    	Event event = new Event("type", 5L);
    	Event event2 = event;
        assertThat(event.equals(event2), is(true));
    }
    
    @Test
	public void equals_NullObjectGiven_ShouldReturnFalse() {
		Event event = new Event("type", 5L);
		assertThat(event.equals(null), is(false));
	}
    
    @SuppressWarnings("unlikely-arg-type")
	@Test
	public void equals_DiffObjectGiven_ShouldReturnFalse() {
		Event event = new Event("type", 5L);
		StoredEvent storedevent = new StoredEvent(event);
		assertThat(event.equals(storedevent), is(false));
	}
    
    @Test
    public void hashCode_SameEventGiven_ShouldReturnTrue() {
    	Event event = new Event("type", 5L);
    	Event event2 = event;
        assertThat(event.hashCode() == event2.hashCode(), is(true));
    }

}