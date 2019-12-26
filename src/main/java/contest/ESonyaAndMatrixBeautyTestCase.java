package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

public class ESonyaAndMatrixBeautyTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 0; i++) {
            tests.add(create(i));
        }
        return tests;
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int n = random.nextInt(1, 5);
        int m = random.nextInt(1, 5);
        int[][] mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = random.nextInt('a', 'z');
            }
        }

        int ans = solve(mat, n, m);

        StringBuilder in = new StringBuilder();
        in.append(n).append(' ').append(m).append('\n');
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                in.append((char) mat[i][j]);
            }
            in.append('\n');
        }

        return new Test(in.toString(), Integer.toString(ans));
    }

    public int[] summary(int[] row, int l, int r) {
        int[] cnt = new int['z' - 'a' + 1];
        for (int i = l; i <= r; i++) {
            cnt[row[i] - 'a']++;
        }
        return cnt;
    }

    public boolean isP(int[][] mat, int l, int r, int b, int t) {
        for (int i = b; i <= t; i++) {
            int[] s = summary(mat[i], l, r);
            int xor = 0;
            for (int x : s) {
                xor += x & 1;
            }
            if (xor > 1) {
                return false;
            }
        }

        while (b < t) {
            if (!SequenceUtils.equal(summary(mat[b], l, r), 0, 'z' - 'a',
                    summary(mat[t], l, r), 0, 'z' - 'a')) {
                return false;
            }
            b++;
            t--;
        }

        return true;
    }

    public int solve(int[][] mat, int n, int m) {
        int ans = 0;
        for (int b = 0; b < n; b++) {
            for (int t = b; t < n; t++) {
                for (int l = 0; l < m; l++) {
                    for (int r = l; r < m; r++) {
                        if (isP(mat, l, r, b, t)) {
                            ans++;
                        }
                    }
                }
            }
        }
        return ans;
    }
}
