package net.intelie.concurrency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import net.intelie.model.Event;
import net.intelie.model.MemoryEventStore;

public class ConcurrencyRemoveAllTest {
	
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
	public void removeAll_ListOfEventsToRemoveGiven_ShouldInvokeMethodWithSucess() throws InterruptedException {
		AtomicInteger i = new AtomicInteger();
		while(i.get() < events.size()) {
			memoryEventStore.insert(events.get(i.get()));
			i.incrementAndGet();
		}

		memoryEventStore.removeAll("type1");

		
		AtomicInteger j = new AtomicInteger();
		while(j.get() < events.size()) {
			memoryEventStore.insert(events.get(j.get()));
			j.incrementAndGet();
		}
	}
	
}
