package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.NumberTheory;
import template.RandomWrapper;

public class LUOGU4705TestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            tests.add(create(i));
        }
        return tests;
    }

    RandomWrapper random = new RandomWrapper(new Random(0));
    public Test create(int testNum){
        int n = random.nextInt(1, 5);
        int m = random.nextInt(1, 5);
        int t = random.nextInt(1, 10);
        int[] a = new int[n];
        int[] b = new int[m];
        for(int i = 0; i < n; i++){
            a[i] = random.nextInt(0, 10);
        }

        for(int i = 0; i < m; i++){
            b[i] = random.nextInt(0, 10);
        }

        int[] ans = solve(a, b, t);

        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        in.append(n).append(' ').append(m).append('\n');
        for(int i = 0; i < n; i++){
            in.append(a[i]).append(' ');
        }
        in.append('\n');
        for(int i = 0; i < m; i++){
            in.append(b[i]).append(' ');
        }
        in.append('\n');
        in.append(t);

        for(int i = 1; i <= t; i++){
            out.append(ans[i]).append('\n');
        }

        return new Test(in.toString(), out.toString());
    }

    public int[] solve(int[] a, int[] b, int t){
        NumberTheory.Modular mod = new NumberTheory.Modular(998244353);
        NumberTheory.Power pow = new NumberTheory.Power(mod);

        int[] ans = new int[t + 1];
        for(int i = 1; i <= t; i++){
            for(int j = 0; j < a.length; j++){
                for(int k = 0; k < b.length; k++){
                    ans[i] = mod.plus(ans[i], pow.pow(mod.plus(a[j], b[k]), i));
                }
            }
            ans[i] = mod.mul(ans[i], pow.inverse(mod.mul(a.length, b.length)));
        }

        return ans;
    }
}
