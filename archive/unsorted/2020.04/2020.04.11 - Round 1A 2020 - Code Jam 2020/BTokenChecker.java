package contest;

import net.egork.chelper.tester.StringInputStream;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.checkers.Checker;
import template.io.FastInput;

import java.util.Set;
import java.util.TreeSet;

public class BTokenChecker implements Checker {
    public BTokenChecker(String parameters) {
    }

    public Verdict check(String input, String expectedOutput, String actualOutput) {
        FastInput stdin = new FastInput(new StringInputStream(input));
        FastInput sol = new FastInput(new StringInputStream(actualOutput));

        stdin.readInt();

        for (int i = 0; i < "Case#4:".length(); i++) {
            sol.readChar();
        }
        int n = stdin.readInt();

        int s = sol.readInt();
        if (s > 500) {
            return Verdict.WA;
        }
        long sum = 0;
        TreeSet<int[]> set = new TreeSet<>((a, b) -> a[0] == b[0] ? Integer.compare(a[1], b[1]) : Integer.compare(a[0], b[0]));

        int lastX = 1;
        int lastY = 1;
        for (int i = 0; i < s; i++) {
            int x = sol.readInt();
            int y = sol.readInt();
            sum += get(x, y);
            if (i == 0 && !(x == 1 && y == 1) || set.contains(new int[]{x, y})) {
                return Verdict.WA;
            }
            if (i > 0 && (Math.abs(lastX - x) > 1 || Math.abs(lastY - y) > 1)) {
                return Verdict.WA;
            }
            if (i > 0 && lastX + 1 == x && lastY - 1 == y) {
                return Verdict.WA;
            }
            lastX = x;
            lastY = y;

            set.add(new int[]{x, y});
        }

        if (sum != n) {
            return Verdict.WA;
        }

        return Verdict.OK;
    }

    public long get(int i, int j) {
        return comp(i - 1, j - 1);
    }

    public long comp(long n, long m) {
        if (m > n) {
            return 0;
        }
        if (m == 0) {
            return 1;
        }
        return comp(n - 1, m - 1) * n / m;
    }
}
