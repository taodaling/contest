package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.RandomWrapper;

public class TaskETestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            tests.add(create());
        }
        return tests;
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create() {
        int n = random.nextInt(1, 3);
        int[][] balls = new int[n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                balls[i][j] = random.nextInt(1, (int) 3);
            }
        }
        long ans = solve(balls, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE,
                Integer.MAX_VALUE, 0);

        StringBuilder builder = new StringBuilder();
        builder.append(n).append('\n');
        for (int[] ball : balls) {
            builder.append(ball[0]).append(' ').append(ball[1]).append('\n');
        }

        return new Test(builder.toString(), Long.toString(ans));
    }

    public long solve(int[][] balls, int rMax, int rMin, int bMax, int bMin, int i) {
        if (i == balls.length) {
            return (long) (rMax - rMin) * (bMax - bMin);
        }
        long ans = Long.MAX_VALUE;
        for (int j = 0; j < 2; j++) {
            ans = Math.min(solve(balls, Math.max(rMax, balls[i][j]),
                    Math.min(rMin, balls[i][j]),
                    Math.max(bMax, balls[i][1 - j]),
                    Math.min(bMin, balls[i][1 - j]),
                    i + 1), ans);
        }
        return ans;
    }
}
