package template;

import org.junit.Assert;
import org.junit.Test;
import template.datastructure.PairingHeap;

import java.util.Comparator;

import static template.datastructure.PairingHeap.NIL;

/** 
* PairingHeap Tester. 
* 
* @author <Authors name> 
* @since <pre>十一月 10, 2019</pre> 
* @version 1.0 
*/ 
public class PairingHeapTest { 

    @Test
    public void test1(){
        PairingHeap<Integer> a = new PairingHeap<>(1);
        PairingHeap<Integer> b = new PairingHeap<>(2);
        PairingHeap<Integer> c = new PairingHeap<>(3);

        PairingHeap<Integer> h = PairingHeap.merge(b, a, Comparator.naturalOrder());
        h = PairingHeap.merge(h, c, Comparator.naturalOrder());

        Assert.assertEquals(1, PairingHeap.peek(h).intValue());
        h = PairingHeap.pop(h, Comparator.naturalOrder());
        Assert.assertEquals(2, PairingHeap.peek(h).intValue());
        h = PairingHeap.pop(h, Comparator.naturalOrder());
        Assert.assertEquals(3, PairingHeap.peek(h).intValue());
        h = PairingHeap.pop(h, Comparator.naturalOrder());
    }

    @Test
    public void test2(){
        PairingHeap<Integer> a = new PairingHeap<>(1);
        PairingHeap<Integer> b = new PairingHeap<>(2);
        PairingHeap<Integer> c = new PairingHeap<>(3);

        PairingHeap<Integer> h = PairingHeap.merge(b, a, Comparator.naturalOrder());
        h = PairingHeap.merge(h, c, Comparator.naturalOrder());
        h = PairingHeap.decrease(h, c, 1, Comparator.naturalOrder());

        Assert.assertEquals(1, PairingHeap.peek(h).intValue());
        h = PairingHeap.pop(h, Comparator.naturalOrder());
        Assert.assertEquals(1, PairingHeap.peek(h).intValue());
        h = PairingHeap.pop(h, Comparator.naturalOrder());
        Assert.assertEquals(2, PairingHeap.peek(h).intValue());
        h = PairingHeap.pop(h, Comparator.naturalOrder());
    }

    @Test
    public void test3(){
        int n = 3000;
        PairingHeap<Integer> heap = NIL;
        Comparator<Integer> cmp = Comparator.naturalOrder();
        for(int i = 0; i < n; i++){
            heap = PairingHeap.merge(heap, new PairingHeap<>(i), cmp);
        }
        for(int i = 0; i < n; i++){
            Assert.assertEquals(i, PairingHeap.peek(heap).intValue());
            heap = PairingHeap.pop(heap, cmp);
        }
    }
} 
