package on2021_06.on2021_06_29_Library_Checker.Static_RMQ;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.datastructure.RMQBeta;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

public class StaticRMQTestCase {
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
        int q = random.nextInt(1, 10);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(1, 100);
        }
        int[][] qs = new int[q][2];
        for (int i = 0; i < q; i++) {
            qs[i][0] = random.nextInt(0, n - 1);
            qs[i][1] = random.nextInt(0, n - 1);
            if (qs[i][0] > qs[i][1]) {
                SequenceUtils.swap(qs[i], 0, 1);
            }
            qs[i][1]++;
        }
        int[] ans = solve(a, qs);
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        printLine(in, n, q);
        printLine(in, a);
        for(int[] query : qs){
            printLine(in, query);
        }
        printLine(out, ans);
        return new Test(in.toString(), out.toString());

    }

    public int[] solve(int[] a, int[][] qs) {
        RMQBeta rmq = new RMQBeta(a.length, (i, j) -> Integer.compare(a[i], a[j]));
        IntegerArrayList res = new IntegerArrayList();
        for (int[] q : qs) {
            int index = rmq.query(q[0], q[1] - 1);
            res.add(a[index]);
        }
        return res.toArray();
    }
}
