package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.CachedPow;
import template.polynomial.NumberTheoryTransform;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.rand.RandomWrapper;

public class P6800ChirpZTransformTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            System.out.println("build  testcase " + i);
            tests.add(create(i));
        }
        return tests;
    }


    private void printLine(StringBuilder builder, int... vals) {
        for (int val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    private void printLine(StringBuilder builder, long... vals) {
        for (long val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = (int) 1e5;
        int m = n;
        int c = random.nextInt(0, mod - 1);
        StringBuilder in = new StringBuilder();
        printLine(in, n, c, m);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            in.append(a[i] = random.nextInt(0, mod - 1)).append(' ');
        }
        StringBuilder out = new StringBuilder();
        printLine(out, solve(a, n, m, c));
        return new Test(in.toString(), out.toString());
    }

    int mod = 998244353;

    public int apply(int[] a, int x) {
        long prod = 1;
        long sum = 0;
        for (int i = 0; i < a.length; i++, prod = prod * x % mod) {
            sum += a[i] * prod % mod;
        }
        return (int) (sum % mod);
    }


    public long choose2(long n) {
        return n * (n - 1) / 2;
    }

    public int[] solve(int[] b, int n, int m, int c) {
        int mod = (int) (998244353);
        CachedPow pow = new CachedPow(c, mod);
        int[] a = new int[m + n];
        for (int i = 0; i < n; i++) {
            b[i] = (int) ((long) b[i] * pow.inverse(choose2(i)) % mod);
        }
        for (int i = 0; i < m + n; i++) {
            a[i] = pow.pow(choose2(i));
        }
        IntegerArrayList alist = new IntegerArrayList();
        alist.addAll(a);
        IntegerArrayList blist = new IntegerArrayList();
        blist.addAll(b);
        IntegerArrayList clist = new IntegerArrayList();
        new NumberTheoryTransform(mod).deltaNTT(alist, blist, clist);
        clist.expandWith(0, m);

        int[] ans = new int[m];
        for (int i = 0; i < m; i++) {
            long x = (long) clist.get(i) * pow.inverse(choose2(i)) % mod;
            ans[i] = (int) x;
        }
        return ans;
    }
}
