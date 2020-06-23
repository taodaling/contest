package on2020_06.on2020_06_23_Library_Checker.RandomMinPoly;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.ModMatrix;
import template.math.Modular;
import template.math.Power;
import template.polynomial.ModGravityLagrangeInterpolation;
import template.primitve.generated.datastructure.IntegerList;
import template.rand.RandomWrapper;

public class RandomMinPolyTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
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
        int n = random.nextInt(2, 2);
        int[][] mat = new int[n][n];
        for (int i = 0; i < 4; i++) {
            int x = random.nextInt(0, n - 1);
            int y = random.nextInt(0, n - 1);
            mat[x][y] = random.nextInt(1, (int) 10);
        }

        int m = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] != 0) {
                    m++;
                }
            }
        }

        StringBuilder in = new StringBuilder();
        printLine(in, n, m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] != 0) {
                    printLine(in, i, j, mat[i][j]);
                }
            }
        }

        String ans = solve(mat);
        return new Test(in.toString(), ans);
    }

    public String solve(int[][] mat) {
        Modular mod = new Modular(1e9 + 7);
        int[][] cloned = IntStream.range(0, mat.length).mapToObj(i -> mat[i].clone()).toArray(i -> new int[i][]);
        ModMatrix matrix = new ModMatrix(cloned);
        StringBuilder ans = new StringBuilder();
        ModGravityLagrangeInterpolation.Polynomial p = matrix.getCharacteristicPolynomial(new Power(mod));
        for (int i = 0; i <= p.getRank(); i++) {
            ans.append(p.getCoefficient(i)).append(' ');
        }
        return ans.toString();
    }
}
