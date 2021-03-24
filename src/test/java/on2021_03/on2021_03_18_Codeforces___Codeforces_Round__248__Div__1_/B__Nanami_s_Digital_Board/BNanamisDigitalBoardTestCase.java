package on2021_03.on2021_03_18_Codeforces___Codeforces_Round__248__Div__1_.B__Nanami_s_Digital_Board;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerPreSum2D;
import template.rand.RandomWrapper;

public class BNanamisDigitalBoardTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
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
        int m = random.nextInt(1, 3);
        int[][] mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = random.nextInt(0, 1);
            }
        }

        int q = random.nextInt(1, 10);
        int[][] qs = new int[q][3];
        for (int i = 0; i < q; i++) {
            qs[i][0] = random.nextInt(1, 2);
            qs[i][1] = random.nextInt(1, n);
            qs[i][2] = random.nextInt(1, m);
        }
        StringBuilder in = new StringBuilder();
        printLine(in, n, m, q);
        for (int[] row : mat) {
            printLine(in, row);
        }
        for (int[] query : qs) {
            printLine(in, query);
        }

        StringBuilder out = new StringBuilder();
        int[] ans = solve(mat, qs);
        printLine(out, ans);
        return new Test(in.toString(), out.toString());
    }

    public int[] solve(int[][] mat, int[][] qs) {
        IntegerArrayList ans = new IntegerArrayList();
        int n = mat.length;
        int m = mat[0].length;
        for (int[] q : qs) {
            int t = q[0];
            int x = q[1] - 1;
            int y = q[2] - 1;
            int area = 0;

            if (t == 1) {
                mat[x][y] ^= 1;
                continue;
            }

            IntegerPreSum2D ps = new IntegerPreSum2D(n, m);
            ps.init((i, j) -> mat[i][j], n, m);
            for (int i = 0; i <= x; i++) {
                for (int j = x; j < n; j++) {
                    for (int l = 0; l <= y; l++) {
                        for (int r = y; r < m; r++) {
                            if (!(i == x || j == x || l == y || r == y)) {
                                continue;
                            }
                            int exp = (j - i + 1) * (r - l + 1);
                            if (ps.rect(i, j, l, r) != exp) {
                                continue;
                            }
                            area = Math.max(area, exp);
                        }
                    }
                }
            }
            ans.add(area);
        }
        return ans.toArray();
    }

}
