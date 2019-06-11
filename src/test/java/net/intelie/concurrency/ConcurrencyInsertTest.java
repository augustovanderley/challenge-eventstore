package net.intelie.concurrency;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import net.intelie.consumer.EventConsumer;
import net.intelie.model.Event;
import net.intelie.model.EventIterator;
import net.intelie.model.MemoryEventStore;

public class ConcurrencyInsertTest {
	
	private static final int numberInvocations = 50;
	private MemoryEventStore memoryEventStore;
	private List<String> types;
	private List<Event> events;
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
		//String string = types.get(ThreadLocalRandom.current().nextInt(types.size()));
		//long randomLong = ThreadLocalRandom.current().nextLong(10);
		
		
	}
	
	@Test(threadPoolSize = 10, invocationCount = numberInvocations)
	public void insert_ListOfEventsToInsertGiven_ShouldReturnAListOfEventsTimesNumberOfCalls() throws InterruptedException {
		AtomicInteger i = new AtomicInteger();
		while(i.get() < events.size()) {
			memoryEventStore.insert(events.get(i.get()));
			i.incrementAndGet();
		}
		

	}
	
	@AfterTest
	public void ending() {
		EventIterator queryResult = memoryEventStore.query("type1", 1L, 10001L);
    	
    	List<Event> eventsExtracted = new EventConsumer(queryResult).extractNextEvents();
    	
    	assertThat(eventsExtracted, hasSize(events.size()*numberInvocations));
    	
	}
}
