package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class DKarenAndCardsTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
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
        int n = random.nextInt(1, 5);
        int p = 10;
        int q = p;
        int t = p;

        int[][] cards = new int[n][3];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                cards[i][j] = random.nextInt(1, p);
            }
        }

        int ans = solve(p, q, t, cards);
        StringBuilder in = new StringBuilder();
        printLine(in, n, p, q, t);
        for(int[] c : cards){
            printLine(in, c[0], c[1], c[2]);
        }
        return new Test(in.toString(), "" + ans);
    }

    public int solve(int p, int q, int t, int[][] cards) {
        int ans = 0;
        for (int i = 1; i <= p; i++) {
            for (int j = 1; j <= q; j++) {
                for (int k = 1; k <= t; k++) {
                    boolean valid = true;
                    for (int[] c : cards) {
                        int val = 0;
                        val += i > c[0] ? 1 : 0;
                        val += j > c[1] ? 1 : 0;
                        val += k > c[2] ? 1 : 0;
                        valid = valid && val >= 2;
                    }
                    if (valid) {
                        ans++;
                    }
                }
            }
        }
        return ans;
    }
}
