package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.Modular;
import template.rand.RandomWrapper;

public class TaskTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            tests.add(create(i));
        }
        return tests;
    }

    RandomWrapper random = new RandomWrapper(0);
    public Test create(int testNum){
        int n = random.nextInt(1, 10000);
        int mod = random.nextInt(1, 10000);
        if(random.nextInt(0, 1) == 1){
            mod = 998244353;
        }
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        in.append(n).append(' ').append(mod);
        int[] ans = solve(n, mod);
        for(int i = 0; i <= n; i++){
            out.append(ans[i]).append('\n');
        }
        return new Test(in.toString(), out.toString());
    }

    public int[] solve(int n, int mod){
        int[][] dp = new int[n + 1][n + 1];
        dp[0][0] = 1;
        Modular modular = new Modular(mod);
        for(int i = 1; i <= n; i++){
            for(int j = 0; j <= n; j++){
                dp[i][j] = dp[i - 1][j];
                if(j - i >= 0){
                    dp[i][j] = modular.plus(dp[i][j], dp[i][j - i]);
                }
            }
        }
        return dp[n];
    }
}
