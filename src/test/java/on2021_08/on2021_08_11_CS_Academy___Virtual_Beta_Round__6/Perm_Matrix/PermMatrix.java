package on2021_08.on2021_08_11_CS_Academy___Virtual_Beta_Round__6.Perm_Matrix;



import template.datastructure.MultiWayStack;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerComparator;
import template.primitve.generated.datastructure.IntegerPriorityQueue;

public class PermMatrix {
    int L = (int) 1e6;
    MultiWayStack<Item> stackA = new MultiWayStack<>(L, L);
    MultiWayStack<Item> stackB = new MultiWayStack<>(L, L);
    int[] cntA = new int[L];
    int[] cntB = new int[L];
    int[] match = new int[L];
    boolean[] visitedA = new boolean[L];
    boolean[] visitedB = new boolean[L];
    IntegerComparator comp = (a, b) -> {
        int ans = Integer.compare(match[a], match[b]);
        if (ans == 0) {
            ans = Integer.compare(a, b);
        }
        return ans;
    };
    IntegerPriorityQueue pqA = new IntegerPriorityQueue(L, comp);
    IntegerPriorityQueue pqB = new IntegerPriorityQueue(L, comp);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        Item[][] mat = new Item[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = new Item();
                mat[i][j].val = in.ri() - 1;
                if (i == 0) {
                    mat[i][j].index = j;
                }
            }
        }
        for (int i = 1; i < n; i++) {
            if (!match(mat[i - 1], mat[i])) {
                out.println(-1);
                return;
            }
            for (Item item : mat[i]) {
                assert item.fa != null;
            }
        }
        Item[] reorder = new Item[m];
        for (Item[] row : mat) {
            for (Item item : row) {
                reorder[index(item)] = item;
            }
            for (Item item : reorder) {
                out.append(item.val + 1).append(' ');
            }
            out.println();
        }
    }

    public int index(Item item) {
        if (item.index == -1) {
            assert item.fa != null;
            item.index = index(item.fa);
        }
        return item.index;
    }

    public boolean match(Item[] a, Item[] b) {
        pqA.clear();
        pqB.clear();
        for (Item item : a) {
            cntA[item.val]++;
            stackA.addLast(item.val, item);
        }
        for (Item item : b) {
            cntB[item.val]++;
            stackB.addLast(item.val, item);
        }

        for (Item item : a) {
            if (!visitedA[item.val]) {
                visitedA[item.val] = true;
                match[item.val] = a.length - cntA[item.val] - cntB[item.val];
                pqA.add(item.val);
            }
        }
        for (Item item : b) {
            if (!visitedB[item.val]) {
                visitedB[item.val] = true;
                match[item.val] = a.length - cntA[item.val] - cntB[item.val];
                pqB.add(item.val);
            }
        }
        int del = 0;
        boolean ok = match[pqA.peek()] >= del && match[pqB.peek()] >= del;
        if (ok) {
            while (!pqA.isEmpty()) {
                int h0 = pqA.pop();
                int h1 = pqB.pop();
                int sendBack0 = -1;
                int sendBack1 = -1;
                if (h1 == h0) {
                    sendBack1 = h1;
                    h1 = pqB.pop();
                }
                if (!pqA.isEmpty() && pqA.peek() == h1) {
                    sendBack0 = pqA.pop();
                }
                assert match[h0] >= del && match[h1] >= del;
                stackB.removeLast(h1).fa = stackA.removeLast(h0);
                del++;
                match[h0]++;
                match[h1]++;
                if (!stackA.isEmpty(h0)) {
                    pqA.add(h0);
                }
                if (sendBack0 != -1 && !stackA.isEmpty(sendBack0)) {
                    pqA.add(sendBack0);
                }
                if (!stackB.isEmpty(h1)) {
                    pqB.add(h1);
                }
                if (sendBack1 != -1 && !stackB.isEmpty(sendBack1)) {
                    pqB.add(sendBack1);
                }
            }
        }
        for (Item item : a) {
            cntA[item.val]--;
            visitedA[item.val] = false;
        }
        for (Item item : b) {
            cntB[item.val]--;
            visitedB[item.val] = false;
        }
        return ok;
    }
}

class Item {
    Item fa;
    int index = -1;
    int val;

    @Override
    public String toString() {
        return "" + (val + 1);
    }
}