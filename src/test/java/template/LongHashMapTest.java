package template;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import template.datastructure.LongEntryIterator;
import template.datastructure.LongHashMap;

public class LongHashMapTest {

    @Test
    public void test2() {
        int n = 10000000;
        LinkedHashMap<Long, Long> hm = new LinkedHashMap<Long, Long>(n);
        for (long i = 0; i < n; i++) {
            hm.put(i, -i);
        }

        for (long i = 0; i < n; i++) {
            Assert.assertTrue(hm.containsKey(i));
            Assert.assertEquals(-i, hm.get(i).longValue());
        }

        for (long i = 0; i < 2 * n; i++) {
            if (i % 2 == 0) {
                hm.remove(i);
            }
        }

        Iterator<Map.Entry<Long, Long>> iterator = hm.entrySet().iterator();
        for (int i = 1; i < n; i += 2) {
            Assert.assertTrue(iterator.hasNext());
            Map.Entry<Long, Long> entry = iterator.next();
            Assert.assertEquals(i, entry.getKey().longValue());
            Assert.assertEquals(-i, entry.getValue().longValue());
        }
    }

    @Test
    public void test1() {
        int n = 10000000;
        LongHashMap hm = new LongHashMap(5000000, true);
        for (int i = 0; i < n; i++) {
            hm.put(i, -i);
        }

        for (int i = 0; i < n; i++) {
            Assert.assertTrue(hm.containKey(i));
            Assert.assertEquals(-i, hm.get(i));
        }

        for (int i = 0; i < 2 * n; i++) {
            if (i % 2 == 0) {
                hm.remove(i);
            }
        }

        LongEntryIterator iterator = hm.iterator();
        for (int i = 1; i < n; i += 2) {
            Assert.assertTrue(iterator.hasNext());
            iterator.next();
            Assert.assertEquals(i, iterator.getEntryKey());
            Assert.assertEquals(-i, iterator.getEntryValue());
        }
    }
}
