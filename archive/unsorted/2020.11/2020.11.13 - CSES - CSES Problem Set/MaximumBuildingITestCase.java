package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.primitve.generated.datastructure.IntegerPreSum2D;
import template.rand.RandomWrapper;

public class MaximumBuildingITestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            System.out.println("build  testcase " + i);
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, int... vals) {
        for (int val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    private void printLine(StringBuilder builder, long... vals) {
        for (long val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int n = random.nextInt(1, 10);
        int m = random.nextInt(1, 10);
        char[][] mat = new char[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = random.range('*', '.');
            }
        }
        int ans = area(mat);
        StringBuilder in = new StringBuilder();
        printLine(in, n, m);
        for(char[] s : mat) {
            in.append(String.valueOf(s)).append('\n');
        }
        return new Test(in.toString(), "" + ans);
    }

    public int area(char[][] mat) {
        int ans = 0;
        int n = mat.length;
        int m = mat[0].length;
        IntegerPreSum2D ps = new IntegerPreSum2D(n, m);
        ps.init((i, j) -> mat[i][j] == '*' ? 1 : 0, n, m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = i; k < n; k++) {
                    for (int t = j; t < m; t++) {
                        if(ps.rect(i, k, j, t) == 0){
                            ans = Math.max(ans, (k - i + 1) * (t - j + 1));
                        }
                    }
                }
            }
        }
        return ans;
    }

}
