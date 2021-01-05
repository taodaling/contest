package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.ModMatrix;
import template.RandomWrapper;

public class LUOGU4723TestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            tests.add(create(i));
        }
        return tests;
    }

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt(1, 1000000000);
        int k = random.nextInt(1, 50);
        int[] f = new int[k + 1];
        int[] a = new int[k];
        for (int i = 1; i <= k; i++) {
            f[i] = random.nextInt(-10, 10);
            a[i - 1] = random.nextInt(-10, 10);
        }

        int ans = solve(n, k, f, a);

        StringBuilder builder = new StringBuilder();
        builder.append(n).append(' ').append(k).append('\n');
        for(int i = 1; i <= k; i++){
            builder.append(f[i]).append(' ');
        }
        builder.append('\n');
        for(int i = 0; i < k; i++){
            builder.append(a[i]).append(' ');
        }

        return new Test(builder.toString(), Integer.toString(ans));
    }

    public int solve(int n, int k, int[] f, int[] a) {
        Modular mod = new Modular(998244353);
        ModMatrix matrix = new ModMatrix(k, k);
        for (int i = 1; i < k; i++) {
            matrix.set(i, i - 1, 1);
        }
        for (int i = 0; i < k; i++) {
            matrix.set(0, i, mod.valueOf(f[i + 1]));
        }
        ModMatrix vec = new ModMatrix(k, 1);
        for (int i = 0; i < k; i++) {
            vec.set(i, 0, mod.valueOf(a[k - 1 - i]));
        }

        ModMatrix transfer = ModMatrix.pow(matrix, n, mod);
        ModMatrix ans = ModMatrix.mul(transfer, vec, mod);
        return ans.get(k - 1, 0);
    }
}
