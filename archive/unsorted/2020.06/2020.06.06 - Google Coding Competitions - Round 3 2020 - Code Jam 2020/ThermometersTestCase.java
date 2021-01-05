package contest;

import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.binary.Bits;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerList;
import template.rand.RandomWrapper;

public class ThermometersTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            System.out.println("build  testcase " + i);
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

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int k = random.nextInt(20, 20);
        int n = random.nextInt(1, 10);
        boolean[] occupied = new boolean[k];
        for (int i = 0; i < n; i++) {
            int r = random.nextInt(0, k - 1);
            if (occupied[r]) {
                i--;
                continue;
            }
            occupied[r] = true;
        }

        IntegerList xs = new IntegerList();
        for (int i = 0; i < k; i++) {
            if (!occupied[i]) {
                continue;
            }
            xs.add(i);
        }

        int[] x = xs.toArray();
        int[] t = new int[n];
        for (int i = 0; i < n; i++) {
            t[i] = random.nextInt(1, 5);
        }

        int ans = solve(k, x, t);
        StringBuilder in = new StringBuilder();
        printLine(in, 1);
        printLine(in, k, n);
        for (int v : x) {
            in.append(v).append(' ');
        }
        printLine(in);
        for (int v : t) {
            in.append(v).append(' ');
        }
        printLine(in);

        StringBuilder out = new StringBuilder("Case #1: ");
        printLine(out, ans);

        return new Test(in.toString(), out.toString());
    }

    public int solve(int d, int[] x, int[] t) {
        int n = x.length;
        int ans = d;
        for (int i = 1; i < 1 << d; i++) {
            int[] data = new int[d];
            Arrays.fill(data, -1);
            boolean valid = true;
            for (double j = 0; j < d && valid; j += 0.5) {
                int val = t[n - 1];
                for (int k = 1; k < n; k++) {
                    if (x[k - 1] <= j && j < x[k]) {
                        val = t[k - 1];
                    }
                }

                //find nearest clockwise
                int cw = -1;
                double cwD = -1;
                for (int k = 0; k < d; k++) {
                    int pos = (int)Math.ceil(j + k) % d;
                    if (Bits.bitAt(i, pos) == 1) {
                        cwD = Math.ceil(j + k) - j ;
                        cw = pos;
                        break;
                    }
                }

                //find nearest count
                int ccw = -1;
                double ccwD = -1;
                for (int k = d; k >= 1; k--) {
                    int pos = DigitUtils.mod((int)Math.floor(j + k - d), d);
                    if (Bits.bitAt(i, pos) == 1) {
                        ccwD = j - Math.floor(j + k - d);
                        ccw = pos;
                        break;
                    }
                }

                int target = cw;
                if (cwD > ccwD) {
                    target = ccw;
                }

                if (data[target] == -1) {
                    data[target] = val;
                }
                if (data[target] != val) {
                    valid = false;
                }
            }

            if (!valid) {
                continue;
            }

            ans = Math.min(ans, Integer.bitCount(i));
        }
        return ans;
    }
}
