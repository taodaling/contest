package contest;

import template.datastructure.MultiWayStack;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;

public class TwoStacksSorting {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Deque<Item>[] dqs = new Deque[2];
        int inf = (int) 1e9;
        for (int i = 0; i < 2; i++) {
            dqs[i] = new ArrayDeque<>(n + 1);
        }
        Item[] indexByV = new Item[n];
        Item[] indexByBorn = new Item[n];
        MultiWayStack<Item> stack = new MultiWayStack<>(n, n);
        int next = 0;

        for (int i = 0; i < n; i++) {
            int x = in.ri();
            Item item = new Item(i, x - 1);
            indexByBorn[item.born] = item;
            indexByV[item.v] = item;

            Item head = item;
            while (head != null && head.v == next) {
                next++;
                stack.addLast(i, head);
                if (next < n) {
                    head = indexByV[next];
                } else {
                    break;
                }
            }
        }
        List<Item> items = new ArrayList<>(n);
        for (int i = n - 1; i >= 0; i--) {
            items.clear();
            for (Item item : stack.getStack(i)) {
                items.add(item);
            }
            if (items.isEmpty()) {
                continue;
            }
            for (Deque<Item> dq : dqs) {
                while (!dq.isEmpty() && dq.peekLast().born > i) {
                    dq.removeLast();
                }
            }
            items.sort(Comparator.comparingInt(x -> x.born));
            int max0 = dqs[0].isEmpty() ? -1 : dqs[0].peekLast().born;
            int max1 = dqs[1].isEmpty() ? -1 : dqs[1].peekLast().born;
            String impossible = "IMPOSSIBLE";
            for (Item item : items) {
                if (item.born < max0 && item.born < max1) {
                    out.println(impossible);
                    return;
                }
                int choice = -1;
                if (item.born < max0) {
                    //1
                    if (!dqs[1].isEmpty() && dqs[1].peekLast().v < item.v) {
                    } else {
                        choice = 1;
                    }
                } else if (item.born < max1) {
                    //1
                    if (!dqs[0].isEmpty() && dqs[0].peekLast().v < item.v) {
                    } else {
                        choice = 0;
                    }
                } else {
                    int v0 = dqs[0].isEmpty() ? inf : dqs[0].peekLast().v;
                    int v1 = dqs[1].isEmpty() ? inf : dqs[1].peekLast().v;
                    if (item.v < v0 && v0 < v1) {
                        choice = 0;
                    } else if (item.v < v1) {
                        choice = 1;
                    } else if (item.v < v0) {
                        choice = 0;
                    }
                }
                if (choice == -1) {
                    out.println(impossible);
                    return;
                }
                item.type = choice;
                dqs[choice].addLast(item);
            }
        }

        for (Item item : indexByBorn) {
            out.append(item.type + 1).append(' ');
        }
    }

}

class Item {
    int born;
    int v;
    int type;

    public Item(int born, int v) {
        this.born = born;
        this.v = v;
    }

    @Override
    public String toString() {
        return "" + v;
    }
}