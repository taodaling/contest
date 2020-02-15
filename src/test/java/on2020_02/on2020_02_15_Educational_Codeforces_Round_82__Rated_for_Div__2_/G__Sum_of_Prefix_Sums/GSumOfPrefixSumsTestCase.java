package on2020_02.on2020_02_15_Educational_Codeforces_Round_82__Rated_for_Div__2_.G__Sum_of_Prefix_Sums;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class GSumOfPrefixSumsTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, Object... vals) {
        for (Object val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int n = random.nextInt(1, 7);
        int[][] edges = new int[n - 1][2];
        for (int i = 0; i < n - 1; i++) {
            edges[i][0] = i + 2;
            edges[i][1] = random.nextInt(1, i + 1);
        }
        int[] a = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            a[i] = random.nextInt(1, 5);
        }

        StringBuilder in = new StringBuilder();
        printLine(in, n);
        for (int[] e : edges) {
            in.append(e[0]).append(' ').append(e[1]).append('\n');
        }
        for (int i = 1; i <= n; i++) {
            in.append(a[i]).append('\n');
        }

        long ans = solve(n, edges, a);
        return new Test(in.toString(), "" + ans);
    }


    public long solve(int n, int[][] edges, int[] a) {
        Node[] nodes = new Node[n + 1];
        for(int i = 1; i <= n; i++){
            nodes[i] = new Node();
        }
        for(int[] e : edges){
            nodes[e[0]].next.add(nodes[e[1]]);
            nodes[e[1]].next.add(nodes[e[0]]);
        }
        for(int i = 1; i <= n; i++){
            nodes[i].w = a[i];
        }
        long ans = 0;
        for(int i = 1; i <= n; i++){
            ans = Math.max(ans, dfs(nodes[i], null));
        }
        return ans;
    }

    public long dfs(Node root, Node p){
        if(p == null){
            root.a = root.s = root.w;
        }else{
            root.a = p.a + p.s + root.w;
            root.s = p.s + root.w;
        }
        long ans = root.a;
        for(Node node : root.next){
            if(node == p){
                continue;
            }
            ans = Math.max(dfs(node, root), ans);
        }
        return ans;
    }

    static class Node {
        List<Node> next = new ArrayList<>();
        long w;
        long a;
        long s;
    }
}
