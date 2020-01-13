package on2020_01.on2020_01_13_Bubble_Cup_11___Finals__Online_Mirror__Div__1_.H__Self_exploration;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class HSelfExplorationTestCase {
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
        int l = random.nextInt(16, 31);
        int r = random.nextInt(16, 31);
        if (l > r) {
            int tmp = l;
            l = r;
            r = tmp;
        }
        int x = random.nextInt(l, r);
        int ans = ask(l, r, x);
        StringBuilder in = new StringBuilder();
        in.append(Integer.toBinaryString(l)).append('\n');
        in.append(Integer.toBinaryString(r)).append('\n');
        int[][] s = summary(x);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                in.append(s[i][j]).append(' ');
            }
        }
        return new Test(in.toString(), Integer.toString(ans));
    }

    public int[][] summary(int x) {
        String s = Integer.toBinaryString(x);
        int[][] ans = new int[2][2];
        for (int i = 1; i < s.length(); i++) {
            ans[s.charAt(i - 1) - '0'][s.charAt(i) - '0']++;
        }
        return ans;
    }

    public int ask(int l, int r, int x) {
        int[][] target = summary(x);
        int ans = 0;
        for (int i = l; i <= r; i++) {
            int[][] s = summary(i);
            boolean equal = true;
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    equal = equal && s[j][k] == target[j][k];
                }
            }
            if (equal) {
                ans++;
            }
        }
        return ans;
    }
}
