package template;

import org.junit.Assert;
import org.junit.Test;
import template.primitve.generated.LongObjectHashMap;

import java.util.HashMap;
import java.util.Map;

public class LongObjectHashMapTest {
    @Test
    public void test1() {
        LongObjectHashMap<Integer> map = new LongObjectHashMap<>(10000000, true);
        for (int i = 0; i < 10000000; i++) {
            map.put(i, -i);
        }
        for(int i = 0; i < 10000000; i++){
            //Assert.assertTrue(map.containKey(i));
            Assert.assertEquals(-i, map.getOrDefault(i, 0).intValue());
        }
    }

    @Test
    public void test3() {
        LongObjectHashMap<Integer> map = new LongObjectHashMap<>(10000, true);
        for (int i = 0; i < 10000; i++) {
            map.put(i, -i);
        }
        for(int i = 0; i < 10000; i++){
            Assert.assertTrue(map.containKey(i));
            Assert.assertEquals(-i, map.getOrDefault(i, 0).intValue());
        }
        for(int i = 0; i < 10000; i += 2){
            map.remove(i);
        }
        for(int i = 0; i < 10000; i++){
            Assert.assertTrue(map.containKey(i) == (i % 2 == 1));
        }
        for (int i = 0; i < 10000; i++) {
            map.put(i, i);
        }
        for(int i = 0; i < 10000; i++){
            Assert.assertTrue(map.containKey(i));
            Assert.assertEquals(i, map.getOrDefault(i, 0).intValue());
        }

    }

    @Test
    public void test2() {
        Map<Long, Integer> map = new HashMap<Long, Integer>(10000000);
        for (int i = 0; i < 10000000; i++) {
            map.put((long)i, -i);
        }
        for(int i = 0; i < 10000000; i++){
            //Assert.assertTrue(map.containKey(i));
            Assert.assertEquals(-i, map.getOrDefault((long)i, 0).intValue());
        }
    }

}
