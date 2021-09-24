package on2021_07.on2021_07_24_DMOJ___CPC__21_Contest_1.P6___AQT_s_Break_Time_is_Over;



import chelper.ExternalExecutor;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class P6AQTsBreakTimeIsOverTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
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
    int L = 100;

    public Test create(int testNum) {
        int n = random.nextInt(1, 3);
        int[][] a = new int[n][3];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                a[i][j] = random.nextInt(0, L);
            }
        }
//        int[] ans = solve(a);
        StringBuilder in = new StringBuilder();
        printLine(in, n);
        for(int[] pt : a){
            printLine(in, pt);
        }
//        StringBuilder out = new StringBuilder();
//        printLine(out, ans);
        return new Test(in.toString(), new ExternalExecutor("F:\\geany\\main.exe").invoke(in.toString()));//out.toString());
    }

    public int[] solve(int[][] a){
        int[] best = new int[]{L, L, L};
        for(int i = 0; i <= L; i++){
            for(int j = 0; j <= L; j++){
                for(int k = 0; k <= L; k++){
                    int match = 0;
                    for(int[] pt : a){
                        if(pt[0] <= i || pt[1] <= j || pt[2] <= k){
                            match++;
                        }
                    }
                    if(match == a.length){
                        if(best[0] + best[1] + best[2] > i + j + k){
                            best = new int[]{i, j, k};
                        }
                    }
                }
            }
        }
        return best;
    }
}
