package contest;



import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class TaskTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for(int i = 0; i < 10000; i++){
            System.out.println("build  testcase " + i);
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, int...vals){
        for(int val : vals){
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    private void printLine(StringBuilder builder, long...vals){
        for(long val : vals){
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
        int n = random.nextInt(1, 10);
        int m = random.nextInt(1, 10);
        int q = random.nextInt(1, 5);
        int[][] mat = new int[n][m];
        int x = random.nextInt(0, n - 1);
        int y = random.nextInt(0, m - 1);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if(i == x || j == y){
                    mat[i][j] = random.nextInt(1, n);
                }else {
                    mat[i][j] = random.nextInt(1, n);
                }
            }
        }
        StringBuilder in = new StringBuilder();
        printLine(in, n, m, q);
        for (int[] row : mat) {
            printLine(in, row);
        }
        return new Test(in.toString(), solve(mat, q));
    }

    public String solve(int[][] mat, int k) {
        int n = mat.length;
        int m = mat[0].length;
        int[][] ans = new int[n][m];
        for (int h = 0; h < n; h++) {
            for (int b = h; b < n; b++) {
                for (int l = 0; l < m; l++) {
                    for (int r = l; r < m; r++) {
                        if (r - l != b - h) {
                            continue;
                        }
                        Set<Integer> set = new HashSet<>();
                        for (int i = h; i <= b && set.size() <= k; i++) {
                            for (int j = l; j <= r && set.size() <= k; j++) {
                                set.add(mat[i][j]);
                            }
                        }
                        if (set.size() > k) {
                            continue;
                        }
                        ans[h][l] = Math.max(ans[h][l], r - l + 1);
                    }
                }
            }
        }
        StringBuilder in = new StringBuilder();
        for(int[] row : ans){
            printLine(in, row);
        }
        return in.toString();
    }
}
