package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.ModMatrix;
import template.NumberTheory;
import template.RandomWrapper;

public class BZOJ4162TestCase {
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
        long n = random.nextLong(0, (long) 1e18);
        int k = random.nextInt(1, 50);
        int[][] mat = new int[k][k];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                mat[i][j] = random.nextInt(0, (int) 1e9);
            }
        }

        ModMatrix matrix = new ModMatrix(mat);
        ModMatrix ans = ModMatrix.pow(matrix, n, new NumberTheory.Modular(1e9 + 7));

        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();

        in.append(Long.toString(n, 2)).append('\n');
        in.append(k);
        in.append('\n');
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                in.append(mat[i][j]).append(' ');
            }
            in.append('\n');
        }

        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                out.append(ans.get(i, j)).append(' ');
            }
            out.append('\n');
        }

        return new Test(in.toString(), out.toString());
    }
}
