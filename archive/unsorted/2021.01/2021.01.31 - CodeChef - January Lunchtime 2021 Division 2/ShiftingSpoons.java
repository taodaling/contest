package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;

public class ShiftingSpoons {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(in.rl(), i);
        }
        wait = new ArrayDeque<>(n);
        seq = new ArrayList<>(n * 2);
        int[] invOrder = new int[n];
        Arrays.sort(items, 1, n, Comparator.comparingLong(x -> x.size));
        for (int i = 1; i < n; i++) {
            invOrder[items[i].id] = i;
            if (items[i].size >= items[0].size) {
                reduce(items[i], items[i].size - items[0].size);
            }
            while (items[i].size < items[0].size && !wait.isEmpty()) {
                Item head = wait.removeFirst();
                long move = Math.min(items[0].size - items[i].size, head.size);
                op(head, items[i], move);
                if (head.size > 0) {
                    wait.addFirst(head);
                }
            }
            op(items[i], items[0], items[i].size);
        }
        if (!wait.isEmpty()) {
            out.println(-1);
            return;
        }
        out.println(seq.size());
        seq.sort(Comparator.<Op>comparingInt(x -> invOrder[x.i])
                .thenComparingInt(x -> -x.j));
        for (Op x : seq) {
            out.append(x.i + 1).append(' ').append(x.j + 1).append(' ').append(x.x).println();
        }
    }

    List<Op> seq;
    Deque<Item> wait;

    void op(Item a, Item b, long x) {
        assert a.size <= b.size;
        assert a.size >= x;
        a.size -= x;
        b.size += x;
        seq.add(new Op(a.id, b.id, x));
    }

    void reduce(Item x, long d) {
        if (d == 0) {
            return;
        }
        assert x.size >= d;
        x.size -= d;
        wait.add(new Item(d, x.id));
    }
}

class Op {
    int i, j;
    long x;

    public Op(int i, int j, long x) {
        this.i = i;
        this.j = j;
        this.x = x;
    }
}

class Item {
    long size;
    int id;


    public Item(long size, int id) {
        this.size = size;
        this.id = id;
    }
}
