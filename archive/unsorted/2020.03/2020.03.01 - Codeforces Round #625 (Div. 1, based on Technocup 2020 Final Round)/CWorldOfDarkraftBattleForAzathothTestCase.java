package contest;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class CWorldOfDarkraftBattleForAzathothTestCase {
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
        int n = random.nextInt(1, 3);
        int m = random.nextInt(1, 3);
        int p = random.nextInt(1, 3);
        int[][] weapon = new int[n][2];
        int[][] armor = new int[m][2];
        int[][] monster = new int[p][3];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                weapon[i][j] = random.nextInt(1, 20);
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 2; j++) {
                armor[i][j] = random.nextInt(1, 20);
            }
        }
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < 3; j++) {
                monster[i][j] = random.nextInt(1, 20);
            }
        }

        long ans = solve(weapon, armor, monster);
        StringBuilder in = new StringBuilder();
        printLine(in, n, m, p);
        for(int i = 0; i < n; i++){
            printLine(in, weapon[i][0], weapon[i][1]);
        }
        for(int i = 0; i < m; i++){
            printLine(in, armor[i][0], armor[i][1]);
        }
        for(int i = 0; i < p; i++){
            printLine(in, monster[i][0], monster[i][1], monster[i][2]);
        }
        return new Test(in.toString(), "" + ans);
    }

    public long solve(int[][] weapon, int[][] armor, int[][] monster) {
        long ans = -weapon[0][1] - armor[0][1];
        for (int[] w : weapon) {
            for (int[] a : armor) {
                long local = -w[1] - a[1];
                for (int[] m : monster) {
                    if (m[0] < w[0] && m[1] < a[0]) {
                        local += m[2];
                    }
                }
                ans = Math.max(ans, local);
            }
        }
        return ans;
    }
}
