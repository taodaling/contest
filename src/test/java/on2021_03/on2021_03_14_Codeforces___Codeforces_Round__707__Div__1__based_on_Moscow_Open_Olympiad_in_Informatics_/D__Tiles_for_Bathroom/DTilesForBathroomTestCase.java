package on2021_03.on2021_03_14_Codeforces___Codeforces_Round__707__Div__1__based_on_Moscow_Open_Olympiad_in_Informatics_.D__Tiles_for_Bathroom;





import java.util.*;

import chelper.ExternalExecutor;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class DTilesForBathroomTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(1);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
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

    RandomWrapper random = new RandomWrapper(1);

    public Test create(int testNum) {
        int n = random.nextInt(1, 20);
        int m = random.nextInt(1, 10);
        int[][] mat = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = random.nextInt(1, n * n);//m + (i < n / 2 && j < n / 2 ? 1 : 0));
            }
        }
        StringBuilder in = new StringBuilder();
        printLine(in, n, m);
        for (int[] row : mat) {
            printLine(in, row);
        }
        ExternalExecutor executor = new ExternalExecutor("F:\\geany\\main.exe");
        return new Test(in.toString(), solve(mat, m)); //executor.invoke(in.toString()));//
    }

    public String solve(int[][] mat, int k) {
        int n = mat.length;
        int[] ans = new int[n];
        for (int h = 0; h < n; h++) {
            for (int b = h; b < n; b++) {
                for (int l = 0; l < n; l++) {
                    for (int r = l; r < n; r++) {
                        if (r - l != b - h) {
                            continue;
                        }
                        Set<Integer> set = new HashSet<>();
                        for (int i = h; i <= b; i++) {
                            for (int j = l; j <= r; j++) {
                                set.add(mat[i][j]);
                            }
                        }
                        if (set.size() > k) {
                            continue;
                        }
                        ans[r - l]++;
                    }
                }
            }
        }
        StringBuilder in = new StringBuilder();
        printLine(in, ans);
        return in.toString();
    }
}
