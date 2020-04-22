package on2020_04.on2020_04_22_Codeforces___Codeforces_Round__449__Div__1_.E__Welcome_home__Chtholly;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

public class EWelcomeHomeChthollyTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, Object... vals) {
        for (Object val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int M = 5;
        int n = random.nextInt(1, 5);
        int m = random.nextInt(1, n);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(1, M);
        }
        int[][] qs = new int[m][4];
        for (int i = 0; i < m; i++) {
            qs[i][0] = random.nextInt(1, 2);
            qs[i][1] = random.nextInt(1, n);
            qs[i][2] = random.nextInt(1, n);
            if (qs[i][1] > qs[i][2]) {
                SequenceUtils.swap(qs[i], 1, 2);
            }
            qs[i][3] = random.nextInt(1, n);
        }

        int[] ans = solve(a.clone(), qs.clone());

        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        printLine(in, n, m);
        for (int i = 0; i < n; i++) {
            in.append(a[i]).append(' ');
        }
        in.append('\n');
        for (int i = 0; i < m; i++) {
            printLine(in, qs[i][0], qs[i][1], qs[i][2], qs[i][3]);
        }

        for (int x : ans) {
            out.append(x).append(' ');
        }

        return new Test(in.toString(), out.toString());
    }

    int[] solve(int[] a, int[][] qs) {
        List<Integer> list = new ArrayList<>();
        for (int[] q : qs) {
            int l = q[1] - 1;
            int r = q[2] - 1;
            int x = q[3];
            if (q[0] == 1) {
                for (int i = l; i <= r; i++) {
                    if (a[i] > x) {
                        a[i] -= x;
                    }
                }
            } else {
                int ans = 0;
                for (int i = l; i <= r; i++) {
                    if (a[i] == x) {
                        ans++;
                    }
                }
                list.add(ans);
            }
        }
        return list.stream().mapToInt(Integer::intValue).toArray();
    }
}
