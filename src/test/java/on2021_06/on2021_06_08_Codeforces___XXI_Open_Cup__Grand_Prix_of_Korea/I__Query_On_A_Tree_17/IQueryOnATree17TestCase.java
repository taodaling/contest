package on2021_06.on2021_06_08_Codeforces___XXI_Open_Cup__Grand_Prix_of_Korea.I__Query_On_A_Tree_17;



import java.util.*;
import java.util.concurrent.*;

import chelper.ExternalExecutor;
import framework.concurrent.RunByHandExecutor;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.graph.Graph;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.rand.RandomWrapper;

public class IQueryOnATree17TestCase {
    @TestCase
    public Collection<Test> createTests() {
        //parallel gen
//        ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2,
//                r -> {
//                    Thread ans = new Thread(r);
//                    ans.setUncaughtExceptionHandler((t, e) -> {
//                        e.printStackTrace();
//                    });
//                    return ans;
//                });
        Test[] tests = new Test[0];
        for (int i = 0; i < tests.length; i++) {
            int finalI = i;
            tests[finalI] = new TaskGenerator().create(finalI);
            System.out.println("generated " + finalI + "-th test");
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
            int n = random.nextInt(1, 5);
            int[][] edges = new int[n - 1][];
            for (int i = 2; i <= n; i++) {
                edges[i - 2] = new int[]{random.nextInt(1, i - 1), i};
            }
            int q = random.nextInt(1, 5);
            int[][] qs = new int[q][];
            for (int i = 0; i < q; i++) {
                int t = random.nextInt(1, 2);
                if (t == 1) {
                    qs[i] = new int[]{t, random.nextInt(1, n)};
                } else {
                    qs[i] = new int[]{t, random.nextInt(1, n), random.nextInt(1, n)};
                }
            }

            int[] ans = solve(n, edges, qs);
            StringBuilder in = new StringBuilder();
            StringBuilder out = new StringBuilder();
            printLine(in, n);
            for (int[] e : edges) {
                printLine(in, e);
            }
            printLine(in, q);
            for (int[] query : qs) {
                printLine(in, query);
            }
            printLine(out, ans);
            return new Test(in.toString(), out.toString());
        }

        int[] sum;
        int[] w;
        List<Integer>[] g;
        List<Integer> cand;
        int[] depth;

        void dfsForSummary(int root, int p) {
            depth[root] = p == -1 ? 0 : depth[p] + 1;
            sum[root] = w[root];
            for (int node : g[root]) {
                if (node == p) {
                    continue;
                }
                dfsForSummary(node, root);
                sum[root] += sum[node];
            }
        }

        void dfsForCentroid(int root, int p) {
            long max = 0;
            for (int node : g[root]) {
                if (node == p) {
                    max = Math.max(sum[0] - sum[root], max);
                } else {
                    max = Math.max(sum[node], max);
                    dfsForCentroid(node, root);
                }
            }
            if (max * 2 <= sum[0]) {
                cand.add(root);
            }
        }

        void updateSubtree(int root, int p, int v) {
            for (int node : g[root]) {
                if (node == p) {
                    continue;
                }
                updateSubtree(node, root, v);
            }
            w[root] += v;
        }

        boolean updatePath(int root, int p, int target, int v) {
            if (root == target) {
                w[root] += v;
                return true;
            }
            for (int node : g[root]) {
                if (node == p) {
                    continue;
                }
                if (updatePath(node, root, target, v)) {
                    w[root] += v;
                    return true;
                }
            }
            return false;
        }

        int[] solve(int n, int[][] edges, int[][] qs) {
            sum = new int[n];
            w = new int[n];
            depth = new int[n];
            g = Graph.createGraph(n);
            for (int[] e : edges) {
                g[e[0] - 1].add(e[1] - 1);
                g[e[1] - 1].add(e[0] - 1);
            }
            IntegerArrayList ans = new IntegerArrayList();
            for (int[] q : qs) {
                cand = new ArrayList<>();
                if (q[0] == 1) {
                    updateSubtree(q[1] - 1, -1, 1);
                } else {
                    updatePath(q[1] - 1, -1, q[2] - 1, 1);
                }
                dfsForSummary(0, -1);
                dfsForCentroid(0, -1);
                cand.sort(Comparator.comparingInt(x -> depth[x]));
                ans.add(cand.get(0) + 1);
            }
            return ans.toArray();
        }
    }
}
