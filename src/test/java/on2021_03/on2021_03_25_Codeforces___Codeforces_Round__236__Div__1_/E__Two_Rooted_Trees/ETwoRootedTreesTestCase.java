package on2021_03.on2021_03_25_Codeforces___Codeforces_Round__236__Div__1_.E__Two_Rooted_Trees;



import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.graph.DirectedEdge;
import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.rand.RandomWrapper;

public class ETwoRootedTreesTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 0; i++) {
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
        int n = random.nextInt(2, 10);
        int[] p1 = new int[n - 1];
        int[] p2 = new int[n - 1];
        for (int i = 0; i < n - 1; i++) {
            p1[i] = random.nextInt(0, i) + 1;
            p2[i] = random.nextInt(0, i) + 1;
        }
        int del = random.nextInt(1, n - 1);
        String ans = solve(p1, p2, del, n);
        StringBuilder in = new StringBuilder();
        printLine(in, n);
        printLine(in, p1);
        printLine(in, p2);
        printLine(in, del);
        return new Test(in.toString(), ans);
    }

    void search(List<UndirectedEdge>[] g, int root, int p, Set<Integer> col) {
        col.add(root);
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            search(g, e.to, root, col);
        }
    }


    public String solve(int[] blueP, int[] redP, int del, int n) {
        StringBuilder in = new StringBuilder();
        UndirectedEdge[] blueE = new UndirectedEdge[n - 1];
        List<UndirectedEdge>[] blueT = Graph.createGraph(n);
        UndirectedEdge[] redE = new UndirectedEdge[n - 1];
        List<UndirectedEdge>[] redT = Graph.createGraph(n);
        for (int i = 0; i < n - 1; i++) {
            blueE[i] = Graph.addUndirectedEdge(blueT, i + 1, blueP[i] - 1);
        }
        for (int i = 0; i < n - 1; i++) {
            redE[i] = Graph.addUndirectedEdge(redT, i + 1, redP[i] - 1);
        }
        Set<Integer> delBlue = new HashSet<>();
        Set<Integer> delRed = new HashSet<>();
        List<Integer> ids = new ArrayList<>();
        delBlue.add(del - 1);
        ids.add(del - 1);
        List<Integer> buf = new ArrayList<>();
        boolean blue = true;
        while (!ids.isEmpty()) {
            buf.clear();
            ids.sort(Comparator.naturalOrder());
            if (blue) {
                for (int e : ids) {
                    Set<Integer> A = new HashSet<>(n);
                    search(blueT, blueE[e].to, blueE[e].rev.to, A);
                    for (int i = 0; i < n - 1; i++) {
                        if (delRed.contains(i)) {
                            continue;
                        }
                        if (A.contains(redE[i].to) != A.contains(redE[i].rev.to)) {
                            //ok
                            delRed.add(i);
                            buf.add(i);
                        }
                    }
                }
                printLineObj(in, "Blue");
            } else {
                for (int e : ids) {
                    Set<Integer> A = new HashSet<>(n);
                    search(redT, redE[e].to, redE[e].rev.to, A);
                    for (int i = 0; i < n - 1; i++) {
                        if (delBlue.contains(i)) {
                            continue;
                        }
                        if (A.contains(blueE[i].to) != A.contains(blueE[i].rev.to)) {
                            //ok
                            delBlue.add(i);
                            buf.add(i);
                        }
                    }
                }
                printLineObj(in, "Red");
            }
            for (int id : ids) {
                in.append(id + 1).append(' ');
            }
            printLine(in);

            List<Integer> tmp = ids;
            ids = buf;
            buf = tmp;

            blue = !blue;
        }

        return in.toString();
    }
}
