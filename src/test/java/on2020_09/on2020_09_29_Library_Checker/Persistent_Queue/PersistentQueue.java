package on2020_09.on2020_09_29_Library_Checker.Persistent_Queue;



import template.datastructure.PersistentArray;
import template.io.FastInput;

import java.io.PrintWriter;

public class PersistentQueue {
    PersistentArray<Integer>[] nexts;
    PersistentArray<Integer> curNext;

    int headIdx = 0;
    int tailIdx = 1;

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int q = in.readInt();
        nexts = new PersistentArray[q + 1];
        nexts[0] = new PersistentArray<>(q + 10);
        int[] data = new int[q + 10];

        for (int i = 1; i <= q; i++) {
            int t = in.readInt();
            int v = in.readInt() + 1;
            curNext = nexts[v];

            if (t == 0) {
                int x = in.readInt();
                data[i + 2] = x;
                Integer tail = curNext.get(tailIdx);
                if (tail != null) {
                    curNext = curNext.set(tail, i + 2);
                    curNext = curNext.set(tailIdx, i + 2);
                } else {
                    curNext = curNext.fill(0, 1, i + 2);
                }
            } else {
                Integer head = curNext.get(headIdx);
                out.println(head == null ? -1 : data[head]);
                if (head != null) {
                    Integer next = curNext.get(head);
                    curNext = curNext.set(headIdx, next);
                    if (next == null) {
                        curNext = curNext.set(tailIdx, null);
                    }
                }

            }

            nexts[i] = curNext;
        }
    }
}
