package on2020_05.on2020_05_28_Codeforces___Playrix_Codescapes_Cup__Codeforces_Round__413__rated__Div__1___Div__2_.C__Fountains;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class CFountainsTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
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
        int n = random.nextInt(1, 4);
        int d = random.nextInt(1, 10);
        int c = random.nextInt(1, 10);

        int[][] fs = new int[n][3];
        for (int i = 0; i < n; i++) {
            fs[i][0] = random.nextInt(1, 10);
            fs[i][1] = random.nextInt(1, 10);
            fs[i][2] = random.nextInt('C', 'D');
        }

        int ans = solve(fs, c, d);

        StringBuilder in = new StringBuilder();
        printLine(in, n, c, d);
        for (int i = 0; i < n; i++) {
            printLine(in, fs[i][0], fs[i][1], (char) fs[i][2]);
        }

        return new Test(in.toString(), "" + ans);
    }

    public int solve(int[][] fs, int c, int d) {
        int n = fs.length;
        int ans = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int rc = c;
                int rd = d;
                if (fs[i][2] == 'C') {
                    rc -= fs[i][1];
                } else {
                    rd -= fs[i][1];
                }
                if (fs[j][2] == 'C') {
                    rc -= fs[j][1];
                } else {
                    rd -= fs[j][1];
                }
                if (rc < 0 || rd < 0) {
                    continue;
                }
                ans = Math.max(ans, fs[i][0] + fs[j][0]);
            }
        }
        return ans;
    }
}
