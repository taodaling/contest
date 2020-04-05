package on2020_04.on2020_04_04_Qualification_Round_2020___Code_Jam_2020.Indicium0;





import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class IndiciumTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
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
        StringBuilder in = new StringBuilder();
        printLine(in, 1);
        int n = random.nextInt(2, 6);
        int k = random.nextInt(n, n * n);

        printLine(in, n, k);
        StringBuilder out = new StringBuilder();
        int[][] ans = solve(n, k);
        if (ans == null) {
            printLine(out, "IMPOSSIBLE");
        } else {
            printLine(out, "POSSIBLE");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    out.append(ans[i][j]).append(' ');
                }
                out.append('\n');
            }
        }
        return new Test(in.toString(), out.toString());
    }

    public boolean dfs(int[][] mat, boolean[][] row, boolean[][] col, int i, int j, int t) {
        if (j >= mat.length) {
            return dfs(mat, row, col, i + 1, 0, t);
        }
        if (i == mat.length) {
            int trace = 0;
            for (int k = 0; k < mat.length; k++) {
                trace += mat[k][k];
            }
            return trace == t;
        }
        for (int k = 1; k <= mat.length; k++) {
            if (row[i][k] || col[j][k]) {
                continue;
            }
            row[i][k] = col[j][k] = true;
            mat[i][j] = k;
            if (dfs(mat, row, col, i, j + 1, t)) {
                return true;
            }
            row[i][k] = col[j][k] = false;
        }
        return false;
    }

    private int[][] solve(int n, int k) {
        int[][] mat = new int[n][n];
        boolean[][] row = new boolean[n][n + 1];
        boolean[][] col = new boolean[n][n + 1];
        if (dfs(mat, row, col, 0, 0, k)) {
            return mat;
        }
        return null;
    }
}
