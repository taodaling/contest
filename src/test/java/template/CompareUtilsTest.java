package template;

import java.util.Arrays;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import template.primitve.generated.datastructure.IntegerComparator;
import template.primitve.generated.datastructure.LongComparator;
import template.utils.CompareUtils;
import template.rand.RandomWrapper;

public class CompareUtilsTest {

    int n = 10000000;

    @Test
    public void quickSort() {
        RandomWrapper rw = new RandomWrapper(0);
        long[] data = new long[n];
        for (int i = 0; i < n; i++) {
            data[i] = rw.nextInt(0, 1000000000);
        }

        Arrays.sort(data, 0, n);
        Assert.assertTrue(CompareUtils.notStrictAscending(data, 0, n - 1));
    }

    @Test
    public void customQuickSort() {
        RandomWrapper rw = new RandomWrapper(0);
        long[] data = new long[n];
        for (int i = 0; i < n; i++) {
            data[i] = rw.nextInt(0, 1000000000);
        }

        CompareUtils.quickSort(data, LongComparator.NATURE_ORDER, 0, n);
        Assert.assertTrue(CompareUtils.notStrictAscending(data, 0, n - 1));
    }


    @Test
    public void radixSortTest() {
        RandomWrapper rw = new RandomWrapper(0);
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


    @Test
    public void radixSortIntTest() {
        RandomWrapper rw = new RandomWrapper(0);
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = rw.nextInt(0, 1000000000);
        }
        // int[] clone = data.clone();
        // Arrays.sort(clone, 0, n);
        CompareUtils.radixSort(data, 0, n - 1);
        Assert.assertTrue(CompareUtils.notStrictAscending(data, 0, n - 1));
        // Assert.assertArrayEquals(clone, data);
    }

    @Test
    public void radixSortIntFetchTest() {
        RandomWrapper rw = new RandomWrapper(0);
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = rw.nextInt(0, 1000000000);
        }
        // int[] clone = data.clone();
        // Arrays.sort(clone, 0, n);
        CompareUtils.radixSort(data, 0, n - 1, i -> i);
        Assert.assertTrue(CompareUtils.notStrictAscending(data, 0, n - 1));
        // Assert.assertArrayEquals(clone, data);
    }


    @Test
    public void radixSortIntFetchRangeTest() {
        RandomWrapper rw = new RandomWrapper(0);
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = rw.nextInt(0, 1000000000);
        }
        // int[] clone = data.clone();
        // Arrays.sort(clone, 0, n);
        CompareUtils.radixSort(data, 100, n - 100, i -> i);
        Assert.assertTrue(CompareUtils.notStrictAscending(data, 100, n - 100));
        // Assert.assertArrayEquals(clone, data);
    }

}
