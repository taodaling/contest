package on2021_11.on2021_11_09_AtCoder___AtCoder_Beginner_Contest_226.G___The_baggage;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Comparator;
import java.util.PriorityQueue;

public class GTheBaggage {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long[] A = new long[6];
        for (int i = 1; i <= 5; i++) {
            A[i] -= in.rl();
        }
        for (int i = 1; i <= 5; i++) {
            A[i] += in.rl();
        }
        PriorityQueue<Item> pq = new PriorityQueue<>(Comparator.comparingInt(x -> -x.size));
        for (int i = 5; i >= 1; i--) {
            if (A[i] > 0) {
                pq.add(new Item(i, A[i]));
                A[i] = 0;
            }
            if (A[i] < 0) {
                while (A[i] < 0 && !pq.isEmpty() && pq.peek().size >= i) {
                    Item item = pq.remove();
                    long use = Math.min(item.remain, -A[i]);
                    A[i] += use;
                    split(pq, item, i, use);
                }
                if (A[i] < 0) {
                    out.println("No");
                    return;
                }
            }
        }
        out.println("Yes");
    }

    public void split(PriorityQueue<Item> pq, Item item, int sub, long num) {
        Item a = new Item(item.size, item.remain - num);
        Item b = new Item(item.size - sub, num);
        add(pq, a);
        add(pq, b);
    }

    public void add(PriorityQueue<Item> pq, Item item) {
        if (item.size == 0 || item.remain == 0) {
            return;
        }
        pq.add(item);
    }
}

class Item {
    long remain;
    int size;

    public Item(int size, long remain) {
        this.remain = remain;
        this.size = size;
    }
}
