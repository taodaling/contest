package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.Modular;
import template.rand.RandomWrapper;

public class DBannedXTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 0; i++) {
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
        int n = random.nextInt(1, 10);
        int k = random.nextInt(1, 2 * n);
        int ans = solve(n, k);
        return new Test("" + n + " " + k, "" + ans);
    }

    int type1 = 0;
    int type2 = 0;
    int type3 = 0;

    public boolean check(int[] data, int k) {
        int r = -1;
        int sum = 0;
        for (int i = 0; i < data.length; i++) {
            while (r + 1 < data.length && sum < k) {
                r++;
                sum += data[r];
            }
            if (sum == k) {
                return true;
            }
            sum -= data[i];
        }
        return false;
    }

    public int dfs(int[] data, int k, int i) {
        if (i == data.length) {
            int cnt = 0;
            for (int x : data) {
                if (x == 1) {
                    cnt++;
                }
            }
            if (check(data, k)) {
                return 0;
            }
            if (cnt == 0) {
                type1++;
            } else if (cnt == 1) {
                type2++;
            } else {
                type3++;
            }
            return 1;
        }
        int ans = 0;
        for (int j = 0; j <= 2; j++) {
            data[i] = j;
            ans += dfs(data, k, i + 1);
        }
        return ans;
    }

    public int solve(int n, int k) {
        type1 = type2 = type3 = 0;
        return dfs(new int[n], k, 0);
    }
}
