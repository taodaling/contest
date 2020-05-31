package on2020_05.on2020_05_31_AtCoder___NOMURA_Programming_Competition_2020.D___Urban_Planning;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.Modular;
import template.rand.RandomWrapper;

public class DUrbanPlanningTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
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

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int n = random.nextInt(2, 3);
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            if (random.nextInt(0, 1) == 0) {
                p[i] = -1;
            } else {
                do {
                    p[i] = random.nextInt(1, n);
                } while (p[i] == i);
            }
        }

        int ans = solve(p);

        StringBuilder in = new StringBuilder();
        printLine(in, n);
        for (int i = 0; i < n; i++) {
            in.append(p[i]).append(' ');
        }

        return new Test(in.toString(), "" + ans);
    }

    public int need(int[] p) {
        int n = p.length;
        DSU dsu = new DSU(n);
        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) != dsu.find(p[i])) {
                ans++;
                dsu.merge(i, p[i]);
            }
        }
        return ans;
    }

    Modular mod = new Modular(1e9 + 7);

    public int dfs(int[] p, int i) {
        if (i < 0) {
            return need(p);
        }
        if (p[i] != -1) {
            return dfs(p, i - 1);
        }
        int ans = 0;
        for (int j = 0; j < p.length; j++) {
            if (j == i) {
                continue;
            }
            p[i] = j;
            ans = mod.plus(ans, dfs(p, i - 1));
        }
        p[i] = -1;
        return ans;
    }


    public int solve(int[] p) {
        p = p.clone();
        for (int i = 0; i < p.length; i++) {
            if (p[i] == -1) {
                continue;
            }
            p[i] -= 1;
        }
        return dfs(p, p.length - 1);
    }
}
