package template;

import java.util.Arrays;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import template.utils.CompareUtils;
import template.rand.RandomWrapper;

public class CompareUtilsTest {

    @Test
    public void radixSortTest2() {
        RandomWrapper rw = new RandomWrapper(new Random(0));
        int n = 10000000;
        long[] data = new long[n];
        for (int i = 0; i < n; i++) {
            data[i] = rw.nextInt(0, 1000000000);
        }

        Arrays.sort(data, 0, n);
        Assert.assertTrue(CompareUtils.notStrictAscending(data, 0, n - 1));
    }


    @Test
    public void radixSortTest() {
        RandomWrapper rw = new RandomWrapper(new Random(0));
        int n = 10000000;
        long[] data = new long[n];
        for (int i = 0; i < n; i++) {
            data[i] = rw.nextInt(0, 1000000000);
        }
        // int[] clone = data.clone();
        // Arrays.sort(clone, 0, n);
        CompareUtils.radixSort(data, 0, n - 1);
        Assert.assertTrue(CompareUtils.notStrictAscending(data, 0, n - 1));
        // Assert.assertArrayEquals(clone, data);
    }


}
