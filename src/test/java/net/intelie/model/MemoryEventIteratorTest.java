package net.intelie.model;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import java.util.LinkedList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MemoryEventIteratorTest {

	private EventIterator eventIterator;
	private LinkedList<StoredEvent> storedEvents;
	private Event event1;
	private Event event2;

	@BeforeMethod
	public void setUp() {
		storedEvents = new LinkedList<StoredEvent>();
		event1 = new Event("type1", 123L);
		event2 = new Event("type1", 125L);
		StoredEvent storedEvent = new StoredEvent(event1);
		storedEvents.add(storedEvent);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void constructor_NullValueGiven_ShouldReturnException() {
		eventIterator = new MemoryEventIterator(null);
	}

	@Test 
	public void hasNext_EmptyListGiven_ShouldReturnFalse() {
		eventIterator = new MemoryEventIterator(new LinkedList<StoredEvent>());
		assertThat(eventIterator.moveNext(), is(false));
	}

	@Test
	public void hasNext_OneEventGiven_ShouldReturnTrueThenFalse() {
		eventIterator = new MemoryEventIterator(storedEvents);
		assertThat(eventIterator.moveNext(), is(true));
		assertThat(eventIterator.moveNext(), is(false));
	}


	@Test
	public void hasNext_ClosedIteratorGiven_ShouldReturnFalse() {
		eventIterator = new MemoryEventIterator(storedEvents);
		closingEventIterator();
		assertThat(eventIterator.moveNext(), is(false));
	}

	@Test(expectedExceptions = IllegalStateException.class)
	@SuppressWarnings("unused")
	public void current_ClosedIteratorGiven_ShouldReturnException() {
		eventIterator = new MemoryEventIterator(storedEvents);
		assertThat(eventIterator.moveNext(), is(true));
		closingEventIterator();
		Event current = eventIterator.current();
	}

	@Test(expectedExceptions = IllegalStateException.class)
	public void remove_ClosedIteratorGiven_ShouldReturnException() {
		eventIterator = new MemoryEventIterator(storedEvents);
		assertThat(eventIterator.moveNext(), is(true));
		closingEventIterator();
		eventIterator.remove();
	}

	@Test(expectedExceptions = IllegalStateException.class)
	@SuppressWarnings("unused")
	public void current_NoCallToMoveNextGiven_ShouldReturnException() {
		eventIterator = new MemoryEventIterator(storedEvents);
		Event current = eventIterator.current();
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	@SuppressWarnings("unused")
	public void current_MoveNextFalseGiven_ShouldReturnException() {
		eventIterator = new MemoryEventIterator(new LinkedList<StoredEvent>());
		assertThat(eventIterator.moveNext(), is(false));
		Event current = eventIterator.current();
	}

	@Test
	public void current_OneEventGiven_ShouldReturnSameEvent() {
		eventIterator = new MemoryEventIterator(storedEvents);
		assertThat(eventIterator.moveNext(), is(true));
		Event current = eventIterator.current();
		assertThat(current, is(event1));
		assertThat(eventIterator.moveNext(), is(false));
	}

	@Test
	public void current_TwoEventGiven_ShouldReturnSameEvents() {
		storedEvents.add(new StoredEvent(event2));
		eventIterator = new MemoryEventIterator(storedEvents);
		assertThat(eventIterator.moveNext(), is(true));
		Event current = eventIterator.current();
		assertThat(current, is(event1));
		assertThat(eventIterator.moveNext(), is(true));
		current = eventIterator.current();
		assertThat(current, is(event2));
		assertThat(eventIterator.moveNext(), is(false));
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void current_NoMoreElementsInIteratorGiven_ShouldReturnException() {
		storedEvents.add(new StoredEvent(event2));
		eventIterator = new MemoryEventIterator(storedEvents);
		assertThat(eventIterator.moveNext(), is(true));
		Event current = eventIterator.current();
		assertThat(eventIterator.moveNext(), is(true));
		current = eventIterator.current();
		assertThat(current, is(event2));
		assertThat(eventIterator.moveNext(), is(false));
		current = eventIterator.current();
	}
	
	@AfterMethod
	public void close() {
		closingEventIterator();
	}

	private void closingEventIterator() {
		try {
			if (eventIterator != null)
				eventIterator.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
