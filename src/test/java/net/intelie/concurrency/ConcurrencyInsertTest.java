package net.intelie.concurrency;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import net.intelie.consumer.EventConsumer;
import net.intelie.model.Event;
import net.intelie.model.EventIterator;
import net.intelie.model.MemoryEventStore;

public class ConcurrencyInsertTest {
	
	private static final int NUMBER_INVOCATIONS = 50;
	private MemoryEventStore memoryEventStore;
	private List<String> types;
	private List<Event> events;
	private long eventsTimeStampCount;
	@BeforeTest
	public void setUp() {
		memoryEventStore = new MemoryEventStore();
		types = Arrays.asList("type1");
		events = new ArrayList<Event>();
		eventsTimeStampCount = 0;
		for (String type : types) {
			for(int i = 1; i <= 10000; i++) {
				events.add(new Event(type, i ));
				eventsTimeStampCount +=  i;
			}
		}
		
		
	}
	
	@Test(threadPoolSize = 10, invocationCount = NUMBER_INVOCATIONS)
	public void insert_ListOfEventsToInsertGiven_ShouldInvokeMethodWithSucess() throws InterruptedException {
		AtomicInteger i = new AtomicInteger();
		while(i.get() < events.size()) {
			memoryEventStore.insert(events.get(i.get()));
			i.incrementAndGet();
		}
	}
	
	@Test(alwaysRun = true, dependsOnMethods= {"insert_ListOfEventsToInsertGiven_ShouldInvokeMethodWithSucess"} )
	public void insert_EventsInsertedConcurrently_ShouldReturnEventsCountEqualToListSizeTimesNumberOfInvocations() {
		EventIterator queryResult = memoryEventStore.query("type1", 1L, 10001L);
    	long extractedEventsTimeStampCount = 0;
    	List<Event> eventsExtracted = new EventConsumer(queryResult).extractNextEvents();
    	for (Event event : eventsExtracted) {
			extractedEventsTimeStampCount += event.timestamp(); 
		}
    	assertThat(extractedEventsTimeStampCount, equalTo(eventsTimeStampCount * NUMBER_INVOCATIONS));
    	assertThat(eventsExtracted, hasSize(events.size()*NUMBER_INVOCATIONS));
    	
	}
}
