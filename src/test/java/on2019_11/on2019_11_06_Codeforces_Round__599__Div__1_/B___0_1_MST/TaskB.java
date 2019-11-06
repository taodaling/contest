package on2019_11.on2019_11_06_Codeforces_Round__599__Div__1_.B___0_1_MST;



import template.FastInput;
import template.FastOutput;

import java.util.*;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
        }

        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.set.add(b);
            b.set.add(a);
        }

        Deque<Node> pend = new ArrayDeque<>(n);
        Set<Node> wait = new LinkedHashSet<>(n);
        wait.addAll(Arrays.asList(nodes).subList(1, n + 1));

        wait.remove(nodes[1]);
        pend.add(nodes[1]);

        List<Node> take = new ArrayList<>();
        int fee = 0;
        while (!wait.isEmpty()) {
            if (pend.isEmpty()) {
                fee++;
                Node any = wait.iterator().next();
                wait.remove(any);
                pend.add(any);
                continue;
            }
            Node head = pend.removeFirst();
            take.clear();
            for (Node node : wait) {
                if (head.set.contains(node)) {
                    continue;
                }
                take.add(node);
            }
            wait.removeAll(take);
            pend.addAll(take);
        }

        out.println(fee);
    }

}

class Node {
    Set<Node> set = new HashSet<>();
    boolean visited;
}