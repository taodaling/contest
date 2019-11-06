package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.RandomWrapper;
import template.SequenceUtils;

public class TaskETestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            tests.add(create(i));
        }
        return tests;
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int h = random.nextInt(300, 300);
        int w = random.nextInt(300, 300);
        char[][] g = new char[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                g[i][j] = random.nextInt(0, 2) != 2 ? '#' : '.';
            }
        }

        long ans = 0;
        //long ans = solve(g);

        StringBuilder builder = new StringBuilder();
        builder.append(h).append(' ').append(w).append('\n');
        for (int i = 0; i < g.length; i++) {
            for (int j = 0; j < g[i].length; j++) {
                builder.append(g[i][j]);
            }
            builder.append('\n');
        }

        return new Test(builder.toString(), Long.toString(ans));
    }

    public long solve(char[][] g) {
        List<int[]> pts = new ArrayList<>();
        for (int i = 0; i < g.length; i++) {
            for (int j = 0; j < g[i].length; j++) {
                if (g[i][j] == '#') {
                    pts.add(SequenceUtils.wrapArray(i, j));
                }
            }
        }


        long ans = 0;
        for (int i = 0; i < pts.size(); i++) {
            for (int j = i + 1; j < pts.size(); j++) {
                for (int k = j + 1; k < pts.size(); k++) {
                    int[] p1 = pts.get(i);
                    int[] p2 = pts.get(j);
                    int[] p3 = pts.get(k);
                    int d1 = Math.abs(p1[0] - p2[0]) + Math.abs(p1[1] - p2[1]);
                    int d2 = Math.abs(p1[0] - p3[0]) + Math.abs(p1[1] - p3[1]);
                    int d3 = Math.abs(p3[0] - p2[0]) + Math.abs(p3[1] - p2[1]);
                    if (d1 == d2 && d2 == d3) {
                        ans++;
                    }
                }
            }
        }

        return ans;
    }
}
