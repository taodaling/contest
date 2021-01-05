package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.ModMatrix;
import template.math.Modular;
import template.rand.RandomWrapper;

public class MatrixExpTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
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

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt(100, 100);
        int[][] mat = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = random.nextInt(0, (int) (1e9 + 6));
            }
        }

        int[] vec = new int[n];
        for (int j = 0; j < n; j++) {
            vec[j] = random.nextInt(0, (int) (1e9 + 6));
        }

        long k = random.nextLong(0, (long) 1e18);

        StringBuilder in = new StringBuilder();
        printLine(in, n);
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                in.append(mat[i][j]).append(' ');
            }
            printLine(in);
        }


        for(int j = 0; j < n; j++){
            in.append(vec[j]).append(' ');
        }
        printLine(in);
        printLine(in, k);

        String ans = solve(mat, vec, k);
        return new Test(in.toString(), ans);
    }

    public String solve(int[][] mat, int[] vec, long k) {
        Modular mod = new Modular(1e9 + 7);
        ModMatrix matrix = new ModMatrix(mat);
        matrix = ModMatrix.pow(matrix, k, mod);
        ModMatrix v = ModMatrix.transpose(new ModMatrix(new int[][]{vec}));
        ModMatrix u = ModMatrix.mul(matrix, v, mod);

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < mat.length; i++) {
            out.append(u.get(i, 0)).append(' ');
        }

        return out.toString();
    }


}
