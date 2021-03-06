package contest;



import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.Permutations;
import template.Randomized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TaskETestCase {

    @TestCase
    public Collection<Test> createTests() {
        List<Test> list = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            list.add(createSingleTask());
        }
        return list;
    }

    Random random = new Random(0);
    public Test createSingleTask() {
        int n = 3;//random.nextInt(3) + 1;
        int[] num = new int[n];
        for (int i = 0; i < n; i++) {
            num[i] = i + 1;
        }
        Randomized.randomizedArray(num, 0, n);
        for (int i = 0; i < n; i++) {
            if (random.nextBoolean()) {
                num[i] = 0;
            }
        }

        int ans = solve(num);

        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        in.append(n).append('\n');
        for(int i = 0; i < n; i++){
            in.append(num[i]).append(' ');
        }
        out.append(ans);

        Test t = new Test(in.toString(), out.toString());
        return t;
    }

    public int solve(int[] num) {
        int n = num.length;
        boolean[] used = new boolean[n + 1];
        for (int i = 0; i < n; i++) {
            used[num[i]] = true;
        }
        return dfs(num, used, 0);
    }


    Modular mod = new Modular(1e9 + 7);

    public int dfs(int[] num, boolean[] used, int i) {
        int n = num.length;
        if (i == n) {
            for(int j = 0; j < i; j++){
                num[j]--;
            }
            int ans = mod.valueOf(Permutations.rankOf(num));
            for(int j = 0; j < i; j++){
                num[j]++;
            }
            return ans;
        }
        if (num[i] > 0) {
            return dfs(num, used, i + 1);
        }
        int ans = 0;
        for (int j = 1; j <= n; j++) {
            if (used[j]) {
                continue;
            }
            used[j] = true;
            num[i] = j;
            ans = mod.plus(ans, dfs(num, used, i + 1));
            used[j] = false;
        }
        num[i] = 0;
        return ans;
    }
}
