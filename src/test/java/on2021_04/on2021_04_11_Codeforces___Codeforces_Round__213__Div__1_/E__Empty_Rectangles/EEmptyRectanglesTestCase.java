package on2021_04.on2021_04_11_Codeforces___Codeforces_Round__213__Div__1_.E__Empty_Rectangles;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.primitve.generated.datastructure.IntegerPreSum2D;
import template.primitve.generated.datastructure.LongPreSum2D;
import template.rand.RandomWrapper;

public class EEmptyRectanglesTestCase {
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
        int n = random.nextInt(1, 10);
        int m = random.nextInt(1, 10);
        int k = random.nextInt(1, 6);
        int[][] mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = random.nextInt(0, 1);
            }
        }
        long ans = solve(mat, k);
        StringBuilder in = new StringBuilder();
        printLine(in, n, m, k);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                in.append(mat[i][j]);
            }
            printLine(in);
        }
        return new Test(in.toString(), "" + ans);
    }

    public long solve(int[][] mat, int k) {
        int n = mat.length;
        int m = mat[0].length;
        IntegerPreSum2D ps = new IntegerPreSum2D((i, j) -> mat[i][j], n, m);
        long ans = 0;
        for (int b = 0; b < n; b++) {
            for (int t = b; t < n; t++) {
                for (int l = 0; l < m; l++) {
                    for (int r = l; r < m; r++) {
                        if (ps.rect(b, t, l, r) == k) {
                            ans++;
                        }
                    }
                }
            }
        }
        return ans;
    }
}
