package contest;

import sun.rmi.runtime.Log;
import template.datastructure.Array2DequeAdapter;
import template.datastructure.IntList;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Log2;

import java.util.Arrays;

public class FIvanAndBurgers {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] c = new int[n];
        for (int i = 0; i < n; i++) {
            c[i] = in.readInt();
        }
        int qCnt = in.readInt();
        Query[] qs = new Query[qCnt];
        for (int i = 0; i < qCnt; i++) {
            qs[i] = new Query();
            qs[i].l = in.readInt() - 1;
            qs[i].r = in.readInt() - 1;
        }
        Query[] qsSorted = qs.clone();
        Arrays.sort(qsSorted, (a, b) -> a.l == b.l ? a.r - b.r : b.l - a.l);
        SimplifiedDeque<Query> deque = new Array2DequeAdapter<>(qsSorted);
        LinearBasis basis = new LinearBasis();
        IntList last = new IntList();
        IntList next = new IntList();
        for (int i = n - 1; i >= 0; i--) {
            basis.clear();
            next.clear();
            basis.add(c[i]);
            next.add(i);
            int j = 0;
            while (!deque.isEmpty() && deque.peekFirst().l == i) {
                Query q = deque.removeFirst();
                while (j < last.size() && last.get(j) <= q.r) {
                    next.add(last.get(j++));
                    if(!basis.add(c[next.tail()])){
                        next.pop();
                    }
                }
                q.ans = basis.theMaximumNumberXor(0);
            }
            while (j < last.size()){
                next.add(last.get(j++));
                if(!basis.add(c[next.tail()])){
                    next.pop();
                }
            }
            IntList tmp = last;
            last = next;
            next = tmp;
        }

        for(Query q : qs){
            out.println(q.ans);
        }
    }
}

class Query {
    int l;
    int r;
    int ans;
}


class LinearBasis {
    private int[] map = new int[20];
    private int size;
    private Log2 log2 = new Log2();

    public int size() {
        return size;
    }

    public void clear() {
        size = 0;
        Arrays.fill(map, 0);
    }

    private void afterAddBit(int bit) {
        for (int i = 19; i >= 0; i--) {
            if (i == bit || map[i] == 0) {
                continue;
            }
            if (bitAt(map[i], bit) == 1) {
                map[i] ^= map[bit];
            }
        }
    }

    public boolean add(int val) {
        for (int i = 19; i >= 0 && val != 0; i--) {
            if (bitAt(val, i) == 0) {
                continue;
            }
            val ^= map[i];
        }
        if (val != 0) {
            int log = log2.floorLog(val);
            map[log] = val;
            size++;
            afterAddBit(log);
            return true;
        }
        return false;
    }

    private long bitAt(int val, int i) {
        return (val >>> i) & 1;
    }

    /**
     * Find the maximun value x ^ v where v is generated
     */
    public int theMaximumNumberXor(int x) {
        for (int i = 0; i < 20; i++) {
            if (map[i] == 0) {
                continue;
            }
            if (bitAt(x, i) == 0) {
                x ^= map[i];
            }
        }
        return x;
    }
}
