package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;

public class EProductSimulation {
    int A = 0;
    int B = 1;
    int C = 2;

    int log2 = 30;

    long[] data;
    int ONE = alloc(1);
    int ZERO = alloc(1);

    static int alloc = 3;

    List<String> ops = new ArrayList<>();

    public void clear(int i) {
        plus(ZERO, ZERO, i);
    }

    public void copy(int src, int dst) {
        if (src == dst) {
            return;
        }
        plus(ZERO, src, dst);
    }

    Debug debug = new Debug(true);

    public void debug(int x) {
       // debug.debug("a[" + x + "]", data[x]);
    }

    /**
     * a[x]=a[x]*2^i
     */
    void mulPow(int x, int i) {
        while (i-- > 0) {
            plus(x, x, x);
        }
    }

    static int alloc(int n) {
        alloc += n;
        return alloc - n;
    }

    public void bothOne(int a, int b, int c) {
        plus(a, b, c);
        compare(ONE, c, c);
    }

    /**
     * If a+b<=y, then a+=2^i
     */
    public void plusIfNotExceed(int a, int b, int i, int y, boolean clear, int one) {
        int abSum = alloc(1);
        int y1Sum = alloc(1);
        int p2 = alloc(1);

        plus(a, b, abSum);
        plus(y, ONE, y1Sum);
        compare(abSum, y1Sum, p2);
        bothOne(p2, one, p2);
        while (i-- > 0) {
            plus(p2, p2, p2);
        }

        if (clear) {
            copy(ZERO, a);
        }
        plus(a, p2, a);
    }

    /**
     * if a[x]==1, then make a[x]=a[y]
     */
    public void makeEqualIfOne(int x, int y) {
        int p2 = alloc(log2 + 1);
        copy(x, p2);
        copy(ZERO, x);
        for (int i = 1; i <= log2; i++) {
            plus(p2 + i - 1, p2 + i - 1, p2 + i);
        }
        for (int i = log2; i >= 0; i--) {
            plusIfNotExceed(x, p2 + i, i, y, false, p2);
        }
    }

    /**
     * a[k]=a[i]+a[j]
     */
    public void plus(int i, int j, int k) {
        ops.add(new StringBuilder().append("+ ").append(i).append(' ').append(j).append(' ').append(k)
                .toString());
        //data[k] = data[i] + data[j];
    }

    /**
     * a[k]=a[i]<a[j]
     */
    public void compare(int i, int j, int k) {
        ops.add(new StringBuilder().append("< ").append(i).append(' ').append(j).append(' ').append(k).toString());
        //data[k] = data[i] < data[j] ? 1 : 0;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {

//        data = new long[(int) 2e5];
//        data[0] = 1;
//        data[1] = 2;
        int tmp = alloc(2);
        compare(ZERO, A, tmp);
        compare(ZERO, B, tmp + 1);
        plus(tmp, tmp + 1, tmp);

        compare(ZERO, tmp, ONE);
        debug(ZERO);
        debug(ONE);
        int exactPow2 = alloc(log2 + 1);
        int cur = alloc(1);
        copy(ONE, exactPow2);
        for (int i = 1; i <= log2; i++) {
            plus(exactPow2 + i - 1, exactPow2 + i - 1, exactPow2 + i);
        }

        debug(exactPow2 + log2);
        for (int i = log2; i >= 0; i--) {
            int notExceed = alloc(1);
            copy(cur, notExceed);

            plusIfNotExceed(notExceed, exactPow2 + i, 0, B, true, ONE);
            int x = alloc(1);
            int y = alloc(1);

            debug(notExceed);
            copy(notExceed, x);
            copy(notExceed, y);
            mulPow(x, i);
            makeEqualIfOne(y, A);
            debug(y);
            mulPow(y, i);
            debug(x);
            debug(y);

            plus(C, y, C);
            plus(cur, x, cur);

            debug(2);
        }

        out.println(ops.size());
        for (String s : ops) {
            out.println(s);
        }
    }
}
