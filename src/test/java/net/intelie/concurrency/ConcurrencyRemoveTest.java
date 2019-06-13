package net.intelie.concurrency;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import net.intelie.consumer.EventConsumer;
import net.intelie.model.Event;
import net.intelie.model.EventIterator;
import net.intelie.model.MemoryEventStore;

public class ConcurrencyRemoveTest {
	private static final int NUMBER_INVOCATIONS = 50;
	private MemoryEventStore memoryEventStore;
	private List<String> types;
	private List<Event> events;
	AtomicInteger countInvocations = new AtomicInteger();
	
	@BeforeTest
	public void setUp() {
		memoryEventStore = new MemoryEventStore();
		types = Arrays.asList("type1");
		events = new ArrayList<Event>();
		for (String type : types) {
			for(int i = 1; i <= 10000; i++) {
				events.add(new Event(type, i ));
			}
		}
		for(int i = 0; i < events.size(); i++) {
			memoryEventStore.insert(events.get(i));
		}
		countInvocations.incrementAndGet();

	}
	
	@Test(threadPoolSize = 10, invocationCount = NUMBER_INVOCATIONS)
	public void remove_ListOfEventsGiven_ShouldInvokeMethodWithSucess() throws InterruptedException {

		EventIterator queryResult = memoryEventStore.query("type1", 1L, 10001L);
		queryResult.moveNext();
		queryResult.remove();
		
		countInvocations.incrementAndGet();
		
		if(countInvocations.get() == NUMBER_INVOCATIONS) {
			List<Event> eventsExtracted = new EventConsumer(queryResult).extractNextEvents();
			assertThat(eventsExtracted.size(), is(greaterThanOrEqualTo(events.size() - NUMBER_INVOCATIONS)));
		}
	}
	

}
