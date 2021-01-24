package contest;

import chelper.AbstractChecker;
import net.egork.chelper.tester.Verdict;
import template.io.FastInput;

public class GridPathConstructionTokenChecker extends AbstractChecker {
    public GridPathConstructionTokenChecker(String parameters) {
        super(parameters);
    }

    @Override
    public Verdict check(FastInput stdin, FastInput expect, FastInput actual) {
        int t = stdin.ri();
        for (int test = 0; test < t; test++) {
            int n = stdin.ri();
            int m = stdin.ri();
            int[] a = new int[2];
            int[] b = new int[2];
            boolean[][] visited = new boolean[n][m];
            for (int i = 0; i < 2; i++) {
                a[i] = stdin.ri() - 1;
            }
            for (int i = 0; i < 2; i++) {
                b[i] = stdin.ri() - 1;
            }
            if (actual.rs().equals("NO")) {
                continue;
            }
            visited[a[0]][a[1]] = true;
            for (char c : actual.rs().toCharArray()) {
                if (c == 'R') {
                    a[1]++;
                } else if (c == 'L') {
                    a[1]--;
                } else if (c == 'U') {
                    a[0]--;
                } else {
                    a[0]++;
                }
                if (a[0] < 0 || a[0] >= n || a[1] < 0 || a[1] > m || visited[a[0]][a[1]]) {
                    return Verdict.WA;
                }
                visited[a[0]][a[1]] = true;
            }
            if (a[0] != b[0] || a[1] != b[1]) {
                return Verdict.WA;
            }
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (!visited[i][j]) {
                        return Verdict.WA;
                    }
                }
            }
            continue;
        }
        return Verdict.OK;
    }
}