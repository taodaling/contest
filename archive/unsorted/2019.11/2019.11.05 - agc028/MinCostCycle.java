package contest;

import template.FastInput;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class MinCostCycle {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();

        PriorityQueue<Node> pqA = new PriorityQueue<>(n, (a, b) -> a.a - b.a);
        PriorityQueue<Node> pqB = new PriorityQueue<>(n, (a, b) -> a.b - b.b);

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            Node node = new Node();
            nodes[i] = node;
            node.a = in.readInt();
            node.b = in.readInt();

            pqA.add(node);
            pqB.add(node);
        }

        List<Node> aList = new ArrayList<>(n);
        List<Node> bList = new ArrayList<>(n);

        while (pqA.size() + pqB.size() > n) {
            if (pqA.peek().a <= pqB.peek().b) {
                aList.add(pqA.poll());
            } else {
                bList.add(pqB.poll());
            }
        }

        long total = 0;
        for (Node node : aList) {
            total += node.a;
        }
        for (Node node : bList) {
            total += node.b;
        }

        for (Node node : aList) {
            node.inList = true;
        }
        for (Node node : bList) {
            node.inList = true;
        }

        boolean hasIgnore = false;
        for (Node node : nodes) {
            if (node.inList == false) {
                hasIgnore = true;
                break;
            }
        }

        if (aList.isEmpty() || bList.isEmpty() || hasIgnore) {
            out.println(total);
            return;
        }

        long ans = (long) 1e18;
        for (int i = 0; i < aList.size(); i++) {
            long bonus = aList.get(i).b;
            long punish = bList.get(bList.size() - 1).b;
            if (i == aList.size() - 1) {
                if (i > 0) {
                    punish = Math.max(punish, aList.get(i - 1).a);
                }
            }else{
                punish = Math.max(punish, aList.get(aList.size() - 1).a);
            }
            ans = Math.min(ans, total + bonus - punish);
        }
        for (int i = 0; i < bList.size(); i++) {
            long bonus = bList.get(i).a;
            long punish = aList.get(aList.size() - 1).a;
            if (i == bList.size() - 1) {
                if (i > 0) {
                    punish = Math.max(punish, bList.get(i - 1).b);
                }
            }else{
                punish = Math.max(punish, bList.get(bList.size() - 1).b);
            }
            ans = Math.min(ans, total + bonus - punish);
        }


        out.println(ans);
    }
}

class Node {
    int a;
    int b;

    boolean inList;
}