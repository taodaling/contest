package on2021_09.on2021_09_11_CS_Academy___Virtual_Round__35.Min_Max_Sum0;



import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.binary.Bits;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.rand.RandomWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MinMaxSumTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
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

    private <T> void printLineObj(StringBuilder builder, T... vals) {
        for (T val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt((int)1, (int)3);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(1, (int)3);
        }
        long ans = solve(a);
        StringBuilder in = new StringBuilder();
        printLine(in, n);
        printLine(in, a);
        return new Test(in.toString(), "" + ans);
    }

    int mod = (int) 1e9 + 7;

    public long process(IntegerArrayList list) {
        int min = (int) 1e9;
        int max = 0;
        for (int x : list.toArray()) {
            min = Math.min(min, x);
            max = Math.max(max, x);
        }
        return (long) min * max % mod;
    }

    public long solve(int[] a) {
        long ans = 0;
        int n = a.length;
        IntegerArrayList list = new IntegerArrayList(n);
        for (int i = 0; i < 1 << n - 1; i++) {
            list.clear();
            for (int j = 0; j < n - 1; j++) {
                list.add(a[j]);
                if (Bits.get(i, j) == 1) {
                    ans += process(list);
                    list.clear();
                }
            }
            list.add(a[n - 1]);
            ans += process(list);
        }
        return ans % mod;
    }
}
