package on2021_09.on2021_09_09_DMOJ___DMOPC__21_Contest_1.CHTTest;



import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.primitve.generated.datastructure.DoubleArrayList;
import template.rand.RandomWrapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CHTTestTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
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

    private <T> void printLineObj(StringBuilder builder, T... vals) {
        for (T val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }


    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt(1, 1000000);
        int m = random.nextInt(1, 1000000);
        int[][] line = new int[n][2];
        int[][] pts = new int[m][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                line[i][j] = random.nextInt(1, 10000);
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 2; j++) {
                pts[i][j] = random.nextInt(1, 10000);
            }
        }
//        double[] ans = solve(line, pts);
        StringBuilder in = new StringBuilder();
        printLine(in, n, m);
        for(int i = 0; i < n; i++){
            printLine(in, line[i]);
        }
        for(int i = 0; i < m; i++){
            printLine(in, pts[i]);
        }
//        StringBuilder out = new StringBuilder();
//        for(double x : ans){
//            printLineObj(out, new BigDecimal(x).toPlainString());
//        }
        return new Test(in.toString());
    }

    public double[] solve(int[][] lines, int[][] pts) {
        DoubleArrayList ans = new DoubleArrayList();
        for (int[] pt : pts) {
            double best = 0;
            for (int[] line : lines) {
                double res = ((double) pt[0] + line[0]) / (pt[1] + line[1]);
                best = Math.max(best, res);
            }
            ans.add(best);
        }
        return ans.toArray();
    }
}
