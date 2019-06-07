package net.intelie.model;


import org.junit.Before;
import org.junit.Test;

public class EventStoreQueryTest {
	
	private EventStore memoryEventStore;

	@Before
	public void setUp() {
		memoryEventStore = new MemoryEventStore();
	}
	
    @Test(expected=IllegalArgumentException.class)
    public void queryEmptyString() {
    	memoryEventStore.query("", 122L, 124L);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void queryNullString() {
    	memoryEventStore.query(null, 122L, 124L);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void queryNegativeStartTime() {    	
    	memoryEventStore.query("type1", -3L, 3L);
    }
    @Test(expected=IllegalArgumentException.class)
    public void queryNegativeEndingTime() {    	
    	memoryEventStore.query("type1", -3L, -2L);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void queryEndingBeforeStart() {
    	memoryEventStore.query("type1", 30L, 10L);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void queryStartSameEnding() {
    	memoryEventStore.query("type1", 30L, 30L);
    }
    
    @Test
    public void queryNoElements() {
    	memoryEventStore.query("type1", 30L, 30L);
    }
}
