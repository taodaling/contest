package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.rand.RandomWrapper;
import template.utils.SegmentUtils;
import template.utils.SequenceUtils;

public class DSeregaAndFunTestCase {
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
        int n = random.nextInt(1, 10);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(1, n);
        }
        int m = random.nextInt(1, 10);
        int[][] ops = new int[m][];
        for (int i = 0; i < m; i++) {
            int l = random.nextInt(1, n);
            int r = random.nextInt(1, n);
            if (l > r) {
                int tmp = l;
                l = r;
                r = tmp;
            }
            if (random.nextInt(1, 2) == 1) {
                ops[i] = new int[]{1, l, r};
            } else {
                ops[i] = new int[]{2, l, r, random.nextInt(1, n)};
            }
        }

        int[] ans = solve(a, ops);
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        printLine(in, n);
        printLine(in, a);
        printLine(in, m);
        for(int[] op : ops){
            printLine(in, op);
        }
        printLine(out, ans);
        return new Test(in.toString(), out.toString());
    }

    public int[] solve(int[] a, int[][] ops) {
        a = a.clone();
        IntegerArrayList ans = new IntegerArrayList();
        for (int[] op : ops) {
            int l = op[1] - 1;
            int r = op[2] - 1;
            if (op[0] == 1) {
                if (l < r) {
                    SequenceUtils.rotate(a, l, r, l + 1);
                }
            } else {
                int x = op[3];
                int cnt = 0;
                for(int i = l; i <= r; i++){
                    if(a[i] == x){
                        cnt++;
                    }
                }
                ans.add(cnt);
            }
        }
        return ans.toArray();
    }
}
