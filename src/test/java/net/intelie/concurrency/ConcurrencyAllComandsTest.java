package net.intelie.concurrency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import net.intelie.model.Event;
import net.intelie.model.EventIterator;
import net.intelie.model.MemoryEventStore;

public class ConcurrencyAllComandsTest {
	private static final int NUMBER_INVOCATIONS = 50;
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
		
		
	}
	
	@Test(threadPoolSize = 10, invocationCount = NUMBER_INVOCATIONS)
	public void allMethods_ListOfEventsGiven_ShouldInvokeMethodsWithSucess() throws InterruptedException {
		AtomicInteger i = new AtomicInteger();
		while(i.get() < events.size()) {
			memoryEventStore.insert(events.get(i.get()));
			i.incrementAndGet();
		}

		EventIterator queryResult = memoryEventStore.query("type1", 1L, 10001L);
		queryResult.moveNext();
		queryResult.remove();
		
		AtomicInteger j = new AtomicInteger();
		while(j.get() < events.size()) {
			memoryEventStore.insert(events.get(j.get()));
			j.incrementAndGet();
		}
		
		memoryEventStore.removeAll("type1");
		
		AtomicInteger k = new AtomicInteger();
		while(k.get() < events.size()) {
			memoryEventStore.insert(events.get(k.get()));
			k.incrementAndGet();
		}
		
		EventIterator queryResult2 = memoryEventStore.query("type1", 1L, 10001L);
		queryResult2.moveNext();
		queryResult2.remove();

	}
	

}
