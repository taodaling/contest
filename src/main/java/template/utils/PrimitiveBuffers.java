package template.utils;

import template.binary.Log2;

import java.util.Arrays;

public class PrimitiveBuffers {
    public static Buffer<int[]>[] intPow2Bufs = new Buffer[30];
    public static Buffer<double[]>[] doublePow2Bufs = new Buffer[30];

    public static void check() {
        for (int i = 0; i < 30; i++) {
            try {
                intPow2Bufs[i].check();
                doublePow2Bufs[i].check();
            } catch (IllegalStateException e) {
                throw new IllegalArgumentException("Alloc more element of size " + i, e);
            }
        }
    }

    static {
        for (int i = 0; i < 30; i++) {
            int finalI = i;
            intPow2Bufs[i] = new Buffer<>(() -> new int[1 << finalI], x -> Arrays.fill(x, 0));
            doublePow2Bufs[i] = new Buffer<>(() -> new double[1 << finalI], x -> Arrays.fill(x, 0));
        }
    }

    public static int[] allocIntPow2(int n) {
        return intPow2Bufs[Log2.ceilLog(n)].alloc();
    }

    public static int[] allocIntPow2(int[] data) {
        int[] ans = allocIntPow2(data.length);
        System.arraycopy(data, 0, ans, 0, data.length);
        return ans;
    }

    public static int[] resize(int[] data, int want) {
        int log = Log2.ceilLog(want);
        if (data.length == 1 << log) {
            return data;
        }
        return replace(allocIntPow2(data, want), data);
    }

    public static double[] resize(double[] data, int want) {
        int log = Log2.ceilLog(want);
        if (data.length == 1 << log) {
            return data;
        }
        return replace(allocDoublePow2(data, want), data);
    }

    public static int[] allocIntPow2(int[] data, int newLen) {
        int[] ans = allocIntPow2(newLen);
        System.arraycopy(data, 0, ans, 0, Math.min(data.length, newLen));
        return ans;
    }

    public static int[] allocIntPow2(int[] data, int prefix, int newLen) {
        int[] ans = allocIntPow2(newLen);
        System.arraycopy(data, 0, ans, 0, Math.min(data.length, prefix));
        return ans;
    }

    public static void release(int[] data) {
        intPow2Bufs[Log2.ceilLog(data.length)].release(data);
    }

    public static void release(int[] a, int[] b) {
        release(a);
        release(b);
    }


    public static void release(int[] a, int[] b, int[] c) {
        release(a);
        release(b);
        release(c);
    }

    public static double[] allocDoublePow2(int n) {
        return doublePow2Bufs[Log2.ceilLog(n)].alloc();
    }

    public static double[] allocDoublePow2(double[] data, int newLen) {
        double[] ans = allocDoublePow2(newLen);
        System.arraycopy(data, 0, ans, 0, Math.min(data.length, newLen));
        return ans;
    }

    public static double[][] allocDoublePow2Array(int n, int arraySize) {
        double[][] ans = new double[arraySize][];
        for (int i = 0; i < arraySize; i++) {
            ans[i] = allocDoublePow2(n);
        }
        return ans;
    }

    public static double[][] allocDoublePow2Array(double[][] data, int n) {
        double[][] ans = new double[data.length][];
        for (int i = 0; i < data.length; i++) {
            ans[i] = allocDoublePow2(data[i], n);
        }
        return ans;
    }

    public static double[] allocDoublePow2(double[] data) {
        double[] ans = allocDoublePow2(data.length);
        System.arraycopy(data, 0, ans, 0, data.length);
        return ans;
    }

    public static void release(double[] data) {
        doublePow2Bufs[Log2.ceilLog(data.length)].release(data);
    }

    public static void release(double[]... data) {
        for (double[] x : data) {
            release(x);
        }
    }

    public static int[] replace(int[] a, int[] b) {
        release(b);
        return a;
    }

    public static double[] replace(double[] a, double[] b) {
        release(b);
        return a;
    }

    public static double[] replace(double[] a, double[]... b) {
        for (double[] x : b) {
            release(x);
        }
        return a;
    }

    public static int[] replace(int[] a, int[]... b) {
        for (int[] x : b) {
            release(x);
        }
        return a;
    }

    public static int[] replace(int[] a, int[] b0, int[] b1) {
        release(b0);
        release(b1);
        return a;
    }

    public static int[] replace(int[] a, int[] b0, int[] b1, int[] b2) {
        release(b0);
        release(b1);
        release(b2);
        return a;
    }

    public static double[][] replace(double[][] a, double[][]... b) {
        for (double[][] x : b) {
            release(x);
        }
        return a;
    }

    public static int[][] allocIntPow2Array(int n, int arraySize) {
        int[][] ans = new int[arraySize][];
        for (int i = 0; i < arraySize; i++) {
            ans[i] = allocIntPow2(n);
        }
        return ans;
    }

    public static void release(int[]... data) {
        for (int[] x : data) {
            release(x);
        }
    }

}
