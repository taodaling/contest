package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.RandomWrapper;

public class TaskETestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            tests.add(create(i));
        }
        return tests;
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int n = random.nextInt(1, 5);
        int d = random.nextInt(1, 1000000000);
        int[] a = new int[1 + n];
        for (int i = 1; i <= n; i++) {
            a[i] = random.nextInt(1, 1000000000);
        }
        long ans = solve(n, d, a);

        StringBuilder in = new StringBuilder();
        in.append(n).append(' ').append(d).append('\n');
        for(int i = 1; i <= n; i++){
            in.append(a[i]).append(' ');
        }
        return new Test(in.toString(), Long.toString(ans));
    }

    public long solve(int n, long d, int[] a){
        Node[] nodes = new Node[n + 1];
        for(int i = 1; i <= n; i++){
            nodes[i] = new Node();
        }
        List<Edge> es = new ArrayList<>();
        for(int i = 1; i <= n; i++){
            for(int j = i + 1; j <= n; j++){
                Edge e = new Edge();
                e.a = nodes[i];
                e.b = nodes[j];
                e.cost = a[i] + a[j] + (j - i) * d;
                es.add(e);
            }
        }
        es.sort((x, y) -> Long.compare(x.cost, y.cost));

        long cost = 0;
        for(Edge e : es){
            if(e.a.find() != e.b.find()){
                cost += e.cost;
                Node.merge(e.a, e.b);
            }
        }

        return cost;
    }

    private static class Edge{
        Node a;
        Node b;
        long cost;
    }


    private static class Node {
        Node p = this;
        int rank = 0;

        Node find() {
            return p.p == p ? p : (p = p.find());
        }

        static void merge(Node a, Node b) {
            a = a.find();
            b = b.find();
            if (a == b) {
                return;
            }
            if (a.rank == b.rank) {
                a.rank++;
            }
            if (a.rank > b.rank) {
                b.p = a;
            } else {
                a.p = b;
            }
        }
    }
}
