import lru.LRUMap;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class LRUMapTests {
    private LRUMap<String,String> map;

    @Before public void setUp() {
        this.map=new LRUMap<>(5);
        map.put("1","one");
        map.put("2","two");
        map.put("A","Aa");
        map.put("B","Bb");
        map.put("halla","lua");
    }

    @Test public void testGetReturnsValue() throws Exception{
        assertEquals("one",map.get("1"));
    }

    @Test public void testPutRemovesEldestEntry() throws Exception{
        map.put("11","oneone");
        assertNull(map.get("1"));
    }

    @Test public void testGetMovesEntryToFront() throws Exception{
        String x=map.get("A");
        assertNotNull(x);
        assertEquals("Aa",map.getFrontValue());
    }

}
