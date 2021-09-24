package on2021_07.on2021_07_23_Codeforces___Harbour_Space_Scholarship_Contest_2021_2022__open_for_everyone__rated__Div__1___Div__2_.G__Common_Divisor_Graph;



import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.GCDs;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.rand.RandomWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class GCommonDivisorGraphTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 0; i++) {
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
        int n = random.nextInt(5);
        int q = n * n;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(1, 100);
        }
        int[][] qs = new int[q][2];
        int wpos = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                qs[wpos][0] = i + 1;
                qs[wpos][1] = j + 1;
                wpos++;
            }
        }
        int[] ans = solve(a, qs);
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        printLine(in, n, q);
        printLine(in, a);
        for (int[] query : qs) {
            printLine(in, query);
        }
        printLine(out, ans);
        return new Test(in.toString(), out.toString());
    }

    public int[] solve(int[] a, int[][] qs) {
        IntegerArrayList ans = new IntegerArrayList();
        int[] extra = new int[200];
        Arrays.fill(extra, 1);
        for (int x : a) {
            extra[x] = 0;
        }
        int[] dist = new int[200];
        boolean[] handled = new boolean[200];
        int inf = (int) 1e9;
        int n = a.length;
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist, inf);
            dist[a[i]] = 0;
            Arrays.fill(handled, false);
            while (true) {
                int best = -1;
                for (int j = 0; j < dist.length; j++) {
                    if (handled[j]) {
                        continue;
                    }
                    if (best == -1 || dist[best] > dist[j]) {
                        best = j;
                    }
                }
                if (best == -1) {
                    break;
                }
                handled[best] = true;
                if (best + 1 < dist.length && dist[best + 1] > dist[best] + 1) {
                    dist[best + 1] = dist[best] + 1;
                }
                for (int j = 2; j < dist.length; j++) {
                    if (GCDs.gcd(j, best) > 1) {
                        dist[j] = Math.min(dist[j], dist[best]);
                    }
                }
            }
            for (int x : a) {
                ans.add(dist[x]);
            }
        }
        return ans.toArray();
    }

}
