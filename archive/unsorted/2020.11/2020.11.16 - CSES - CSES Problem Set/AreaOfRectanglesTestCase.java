package contest;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import framework.io.FileUtils;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class AreaOfRectanglesTestCase {
    @TestCase
    public Collection<Test> createTests() {
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
        if (testNum == 0) {
            File dir = new File("/home/dalt/下载");
            return new Test(FileUtils.readFile(dir, "test_input (4).txt"),
                    FileUtils.readFile(dir, "test_output (4).txt"));
        }
        int n = random.nextInt(1, 3);
        Rect[] rects = new Rect[n];
        for (int i = 0; i < n; i++) {
            rects[i] = new Rect();
            rects[i].x1 = random.nextInt(1, 5);
            rects[i].x2 = random.nextInt(1, 5);
            rects[i].y1 = random.nextInt(1, 5);
            rects[i].y2 = random.nextInt(1, 5);
            if (rects[i].x1 > rects[i].x2) {
                int tmp = rects[i].x1;
                rects[i].x1 = rects[i].x2;
                rects[i].x2 = tmp;
            }
            if (rects[i].y1 > rects[i].y2) {
                int tmp = rects[i].y1;
                rects[i].y1 = rects[i].y2;
                rects[i].y2 = tmp;
            }
            rects[i].x2++;
            rects[i].y2++;
        }

        long ans = solve(rects);
        StringBuilder in = new StringBuilder();
        printLine(in, n);
        for (Rect r : rects) {
            printLine(in, r.x1, r.y1, r.x2, r.y2);
        }

        return new Test(in.toString(), "" + ans);
    }

    public long solve(Rect[] rects) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (Rect r : rects) {
            minX = Math.min(minX, r.x1);
            minY = Math.min(minY, r.y1);
            maxX = Math.max(maxX, r.x2);
            maxY = Math.max(maxY, r.y2);
        }
        int[][] mat = new int[maxX - minX + 1][maxY - minY + 1];
        for (Rect rect : rects) {
            int l = rect.x1 - minX;
            int r = rect.x2 - minX;
            int u = rect.y2 - minY;
            int d = rect.y1 - minY;
            for (int i = l; i < r; i++) {
                for (int j = d; j < u; j++) {
                    mat[i][j] = 1;
                }
            }
        }

        int sum = 0;
        for (int[] row : mat) {
            sum += Arrays.stream(row).sum();
        }
        return sum;
    }

    static class Rect {
        int x1;
        int y1;
        int x2;
        int y2;
    }
}
