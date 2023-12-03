package template.datastructure;

import org.junit.Assert;
import org.junit.Test;
import template.utils.SequenceUtils;
import template.utils.ToLongFunction;

import java.util.ArrayList;
import java.util.List;

public class LongKDTreeTest {

    @Test
    public void testSearch2() {
        LongKDTree tree = new LongKDTree(2, (long) 2e18);
        tree.add(SequenceUtils.wrapArray(0L, 0L));
        tree.add(SequenceUtils.wrapArray(1L, -1L));
        tree.add(SequenceUtils.wrapArray(1L, 1L));
        tree.add(SequenceUtils.wrapArray(2L, 0L));
        Assert.assertEquals(4, tree.getSize());
    }

    @Test
    public void testNearest() {
        LongKDTree tree = new LongKDTree(2, (long) 2e18);
        tree.add(SequenceUtils.wrapArray(100L, 0L));
        tree.add(SequenceUtils.wrapArray(1L, -100L));
        tree.add(SequenceUtils.wrapArray(1L, 20L));
        tree.add(SequenceUtils.wrapArray(0L, 0L));

        List<LongKDTree.Node> list = new ArrayList<>();
        ToLongFunction<long[]> function = new ToLongFunction<long[]>() {
            @Override
            public long apply(long[] delta) {
                long sum = 0;
                for (long x : delta) {
                    sum += x * x;
                }
                return sum;
            }
        };
        tree.searchNearest(SequenceUtils.wrapArray(0L, 0L), function, 2, list);
        list.sort((a, b) -> Long.compare(a.dist, b.dist));
        Assert.assertTrue(list.size() == 2);
        Assert.assertArrayEquals(SequenceUtils.wrapArray(0L, 0L), list.get(0).coordinates);
        Assert.assertArrayEquals(SequenceUtils.wrapArray(1L, 20L), list.get(1).coordinates);
    }

}