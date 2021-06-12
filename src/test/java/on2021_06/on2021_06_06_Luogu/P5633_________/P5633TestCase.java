package on2021_06.on2021_06_06_Luogu.P5633_________;



import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import chelper.ExternalExecutor;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class P5633TestCase {
    @TestCase
    public Collection<Test> createTests() {
        //parallel gen
        ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        Test[] tests = new Test[1000];
        for (int i = 0; i < tests.length; i++) {
            int finalI = i;
            es.submit(() -> {
                tests[finalI] = new TaskGenerator().create(finalI);
                System.out.println("generated " + finalI + "-th test");
            });
        }
        es.shutdown();
        try {
            es.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Arrays.asList(tests);
    }

    static class TaskGenerator {

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

        public Test create(int testNum) {
            RandomWrapper random = new RandomWrapper(testNum);
            int n = random.nextInt(1, 20);
            int m = random.nextInt(1, 30) + n;
            List<int[]> edges = new ArrayList<>();
            int deg = 0;
            for (int i = 2; i <= n; i++) {
                int p = random.nextInt(1, i - 1);
                int w = random.nextInt(1, 10000);
                edges.add(new int[]{i, p, w});
                if (i == 1 || p == 1) {
                    deg++;
                }
            }
            for (int i = n - 1; i < m; i++) {
                int a = random.nextInt(1, n);
                int b = random.nextInt(1, n);
                int w = random.nextInt(1, 10000);
                if (a == 1 || b == 1) {
                    deg++;
                }
                edges.add(new int[]{a, b, w});
            }
            int k = random.nextInt(0, Math.min(n - 1, deg));
            StringBuilder in = new StringBuilder();
            printLine(in, n, m, 1, k);
            for (int[] e : edges) {
                printLine(in, e);
            }
            String out = new ExternalExecutor("F:\\geany\\main.exe").invoke(in.toString());
            return new Test(in.toString(), out);
        }
    }
}
