package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.DigitUtils;
import template.RandomWrapper;

public class AntsonaCircleTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            tests.add(create(i));
        }
        return tests;
    }

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt(1, 4);
        int l = random.nextInt(n, 8);
        int t = random.nextInt(1, 3);
        int[][] xw = new int[n][2];
        for (int i = 0; i < n; i++) {
            xw[i][0] = i;
            xw[i][1] = random.nextInt(1, 2);
        }

        int[] ans = solve(n, l, t, xw);
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();

        in.append(n).append(' ').append(l).append(' ').append(t).append('\n');
        for (int i = 0; i < n; i++) {
            in.append(xw[i][0]).append(' ').append(xw[i][1]).append('\n');
        }

        for (int i = 0; i < n; i++) {
            out.append(ans[i]).append('\n');
        }

        return new Test(in.toString(), out.toString());
    }

    public int[] solve(int n, int l, int t, int[][] xw) {
        int[][] status = new int[n][2];
        for (int i = 0; i < n; i++) {
            status[i][0] = xw[i][0] * 2;
            status[i][1] = xw[i][1] == 1 ? 1 : -1;
        }

        for (int i = 0; i < 2 * t; i++) {
            for (int j = 0; j < n; j++) {
                status[j][0] += status[j][1];
                status[j][0] = DigitUtils.mod(status[j][0], l * 2);
            }
            if(n > 1) {
                for (int j = 0; j < n; j++) {
                    if (status[j][1] > 0 && status[DigitUtils.mod(j + 1, n)][0] ==
                            status[j][0]) {
                        status[j][1] = -status[j][1];
                    } else if (status[j][1] < 0 && status[DigitUtils.mod(j - 1, n)][0] ==
                            status[j][0]) {
                        status[j][1] = -status[j][1];
                    }
                }
            }
        }

        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[i] = status[i][0] / 2;
        }
        return ans;
    }
}
