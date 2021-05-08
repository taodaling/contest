package on2021_04.on2021_04_29_Codeforces___Codeforces_Round__192__Div__1_.C__Graph_Reconstruction;



import template.datastructure.LinkedListBeta;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongArrayList;
import template.primitve.generated.datastructure.LongHashMap;
import template.primitve.generated.datastructure.LongHashSet;
import template.rand.Randomized;
import template.utils.SequenceUtils;

public class CGraphReconstruction {
    LongHashSet E = new LongHashSet((int) 2e5, false);

    long id(int i, int j) {
        if (i > j) {
            int tmp = i;
            i = j;
            j = tmp;
        }
        return DigitUtils.asLong(i, j);
    }

    LinkedListBeta.Node<Integer>[] nodes;
    LongArrayList cand = new LongArrayList((int) 2e5);
    int n;

    boolean calc(int m) {
        LinkedListBeta<Integer> list = new LinkedListBeta<>();
        Randomized.shuffle(nodes);
        for (int i = 0; i < n; i++) {
            list.addLast(nodes[i]);
        }
        cand.clear();
        while (cand.size() < m && !list.isEmpty()) {
            LinkedListBeta.Node<Integer> head, tail;
            head = tail = list.begin();
            list.remove(head);
            int size = 1;

            while (cand.size() < m && !list.isEmpty()) {
                boolean updated = false;
                for (LinkedListBeta.Node<Integer> iter = list.begin(); iter != list.end(); iter = iter.next) {
                    if (E.contain(id(tail.val, iter.val))) {
                        continue;
                    }
                    cand.add(id(tail.val, iter.val));
                    tail = iter;
                    iter = iter.prev;
                    list.remove(iter.next);
                    size++;
                    updated = true;
                    break;
                }
                if (!updated) {
                    break;
                }
            }

            if (size >= 3 && !E.contain(id(head.val, tail.val))) {
                cand.add(id(head.val, tail.val));
            }
        }

        cand.retain(m);
        return cand.size() >= m;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        nodes = new LinkedListBeta.Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new LinkedListBeta.Node<>(i);
        }
        int m = in.ri();
        for (int i = 0; i < m; i++) {
            int u = in.ri() - 1;
            int v = in.ri() - 1;
            E.add(id(u, v));
        }

        long threshold = System.currentTimeMillis() + 1000;
        while (System.currentTimeMillis() <= threshold) {
            if (calc(m)) {
                for (long v : cand.toArray()) {
                    assert !E.contain(v);
                    out.append(1 + DigitUtils.highBit(v)).append(' ').append(1 + DigitUtils.lowBit(v)).println();
                }
                return;
            }
        }

        out.println(-1);
    }
}
