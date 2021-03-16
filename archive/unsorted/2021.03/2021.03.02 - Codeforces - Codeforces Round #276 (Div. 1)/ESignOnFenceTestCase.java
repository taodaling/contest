package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerComparator;
import template.primitve.generated.datastructure.IntegerMinQueue;
import template.rand.RandomWrapper;

public class ESignOnFenceTestCase {
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
        int n = random.nextInt(1, 3);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(1, 10);
        }
        int m = random.nextInt(1, 3);
        int[][] qs = new int[m][3];
        for (int i = 0; i < m; i++) {
            int l = random.nextInt(1, n);
            int r = random.nextInt(1, n);
            if (l > r) {
                l ^= r;
                r ^= l;
                l ^= r;
            }
            int w = random.nextInt(1, r - l + 1);
            qs[i][0] = l;
            qs[i][1] = r;
            qs[i][2] = w;
        }
        int[] ans = solve(a, qs);
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        printLine(in, n);
        printLine(in, a);
        printLine(in, m);
        for(int[] q : qs){
            printLine(in, q);
        }
        printLine(out, ans);
        return new Test(in.toString(), out.toString());
    }

    public int[] solve(int[] a, int[][] qs) {
        IntegerArrayList ans = new IntegerArrayList();
        IntegerMinQueue queue = new IntegerMinQueue(a.length, IntegerComparator.NATURE_ORDER);
        for (int[] query : qs) {
            int l = query[0] - 1;
            int r = query[1] - 1;
            int w = query[2];
            int best = 0;
            queue.clear();
            for (int i = l; i <= r; i++) {
                queue.addLast(a[i]);
                if (queue.size() > w) {
                    queue.removeFirst();
                }
                if(queue.size() == w) {
                    best = Math.max(best, queue.min());
                }
            }
            ans.add(best);
        }
        return ans.toArray();
    }
}
