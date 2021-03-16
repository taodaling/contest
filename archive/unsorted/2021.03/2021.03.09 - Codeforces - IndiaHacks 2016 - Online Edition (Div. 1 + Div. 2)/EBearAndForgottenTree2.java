package contest;

import template.datastructure.LinkedListBeta;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongHashSet;

import java.util.*;

public class EBearAndForgottenTree2 {
    LongHashSet set = new LongHashSet((int) 1e6, false);

    public long id(LinkedListBeta.Node<Integer> a, LinkedListBeta.Node<Integer> b) {
        return id(a.val, b.val);
    }

    public long id(int a, int b) {
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        return DigitUtils.asLong(a, b);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int k = in.ri();
        LinkedListBeta.Node<Integer>[] nodes = new LinkedListBeta.Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new LinkedListBeta.Node<>(i);
        }
        for (int i = 0; i < m; i++) {
            set.add(id(in.ri() - 1, in.ri() - 1));
        }
        LinkedListBeta<Integer> list = new LinkedListBeta<>();
        for (int i = 1; i < n; i++) {
            list.addLast(nodes[i]);
        }
        Deque<LinkedListBeta.Node<Integer>> dq = new ArrayDeque<>();
        List<LinkedListBeta.Node<Integer>> seq = new ArrayList<>();
        boolean[] withZero = new boolean[n];
        int scc = 0;
        while (!list.isEmpty()) {
            seq.clear();
            dq.clear();
            dq.addLast(list.begin());
            list.remove(list.begin());
            while (!dq.isEmpty()) {
                LinkedListBeta.Node<Integer> head = dq.removeFirst();
                seq.add(head);
                for (LinkedListBeta.Node<Integer> node = list.begin(), next; node != list.end(); node = next) {
                    next = node.next;
                    if (set.contain(id(head, node))) {
                        continue;
                    }
                    dq.addLast(node);
                    list.remove(node);
                }
            }
            boolean added = false;
            for (LinkedListBeta.Node<Integer> node : seq) {
                if (set.contain(id(node, nodes[0]))) {
                    continue;
                }
                added = true;
                withZero[node.val] = true;
                break;
            }
            if (!added) {
                out.println("impossible");
                return;
            }

            scc++;
        }

        int totalEdge = 0;
        for (int i = 1; i < n; i++) {
            if (set.contain(id(nodes[0], nodes[i]))) {
                continue;
            }
            totalEdge++;
        }
        if(k < scc || totalEdge < k){
            out.println("impossible");
            return;
        }

        out.println("possible");

    }
}
