package on2021_07.on2021_07_17_Codeforces___Codeforces_Round__733__Div__1___Div__2__based_on_VK_Cup_2021___Elimination__Engine__.E__Minimax;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.Deque;

public class EMinimax {
    char[] s = new char[(int) 2e5];

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.rs(s);
        int charset = 'z' - 'a' + 1;
        int[] cnts = new int[charset];
        for (int i = 0; i < n; i++) {
            cnts[s[i] - 'a']++;
        }
        int oneOccur = -1;
        for (int i = 0; i < charset; i++) {
            if (cnts[i] == 1) {
                oneOccur = i;
                break;
            }
        }
        if (oneOccur != -1) {
            out.append((char) (oneOccur + 'a'));
            for (int i = 0; i < charset; i++) {
                if (i == oneOccur) {
                    continue;
                }
                for (int j = 0; j < cnts[i]; j++) {
                    out.append((char) (i + 'a'));
                }
            }
            out.println();
            return;
        }
        Deque<Item> dq = new ArrayDeque<>(charset);
        for (int i = 0; i < charset; i++) {
            if (cnts[i] > 0) {
                dq.addLast(new Item(i, cnts[i]));
            }
        }
        if (dq.size() == 1) {
            for (int i = 0; i < n; i++) {
                out.append(s[i]);
            }
            out.println();
            return;
        }

        Item first = dq.removeFirst();
        ans = new int[n];
        wpos = 0;
        int consume = 2 + (n - first.cnt) >= first.cnt ? 2 : 1;
        consume = Math.min(consume, first.cnt);
        for (int i = 0; i < consume; i++) {
            add(first);
        }
        if (first.cnt == 0) {
            while (!dq.isEmpty()) {
                Item head = dq.removeFirst();
                addAll(head);
            }
        } else if (consume == 2) {
            while (first.cnt > 0) {
                Item cur = dq.peekFirst();
                if (cur.cnt == 0) {
                    dq.removeFirst();
                    continue;
                }
                add(cur);
                add(first);
            }
            while (!dq.isEmpty()) {
                Item head = dq.removeFirst();
                addAll(head);
            }
        } else {
            if (dq.size() == 1) {
                Item head = dq.removeFirst();
                addAll(head);
                addAll(first);
            } else {
                Item second = dq.removeFirst();
                add(second);
                addAll(first);
                add(dq.peekFirst());
                dq.addFirst(second);
                while (!dq.isEmpty()) {
                    Item head = dq.removeFirst();
                    addAll(head);
                }
            }
        }

        for (int i = 0; i < n; i++) {
            out.append((char) (ans[i] + 'a'));
        }
        out.println();
    }

    int[] ans;
    int wpos;

    void add(Item item) {
        ans[wpos++] = item.c;
        item.cnt--;
    }

    void addAll(Item item) {
        while (item.cnt > 0) {
            add(item);
        }
    }
}

class Item {
    int c;
    int cnt;

    public Item(int c, int cnt) {
        this.c = c;
        this.cnt = cnt;
    }
}
