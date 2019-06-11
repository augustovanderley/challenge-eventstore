package net.intelie.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.Test;

public class StoredEventTest {
	@Test
	public void equals_TwoDiffStoredEventsGiven_ShouldReturnFalse() {
		Event event = new Event("type", 5L);
		Event event2 = new Event("type", 5L);
		StoredEvent storedEvent = new StoredEvent(event);
		StoredEvent storedEvent2 = new StoredEvent(event2);
		assertThat(storedEvent.equals(storedEvent2), is(false));
	}

	@Test
	public void equals_SameStoredEventGiven_ShouldReturnTrue() {
		Event event = new Event("type", 5L);
		StoredEvent storedEvent = new StoredEvent(event);
		StoredEvent storedEvent2 = storedEvent;
		assertThat(storedEvent.equals(storedEvent2), is(true));
	}
	
	@Test
	public void equals_NullObjectGiven_ShouldReturnFalse() {
		Event event = new Event("type", 5L);
		StoredEvent storedEvent = new StoredEvent(event);
		assertThat(storedEvent.equals(null), is(false));
	}
	
    @SuppressWarnings("unlikely-arg-type")
	@Test    
	public void equals_DiffObjectGiven_ShouldReturnFalse() {
		Event event = new Event("type", 5L);
		StoredEvent storedevent = new StoredEvent(event);
		assertThat(storedevent.equals(event), is(false));
	}

	@Test
	public void hashCode_SameStoredEventGiven_ShouldReturnTrue() {
		Event event = new Event("type", 5L);
		StoredEvent storedEvent = new StoredEvent(event);
		StoredEvent storedEvent2 = storedEvent;
		assertThat(storedEvent.hashCode() == storedEvent2.hashCode(), is(true));
	}
}
