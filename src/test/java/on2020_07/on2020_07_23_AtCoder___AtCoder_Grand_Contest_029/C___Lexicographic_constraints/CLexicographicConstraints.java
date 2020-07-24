package on2020_07.on2020_07_23_AtCoder___AtCoder_Grand_Contest_029.C___Lexicographic_constraints;



import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.TreeMap;

public class CLexicographicConstraints {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        Deque<Tag> dq = new ArrayDeque<>(n);
        IntBinarySearch ibs = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                dq.clear();
                int length = 0;
                for (int x : a) {
                    if (length < x) {
                        length = x;
                        continue;
                    }
                    length = x;
                    if (mid == 1) {
                        return false;
                    }
                    while (!dq.isEmpty() && dq.peekLast().index >= length) {
                        dq.removeLast();
                    }
                    int cur = length - 1;
                    while (cur >= 0) {
                        Tag tail = dq.isEmpty() || dq.peekLast().index != cur ?
                                new Tag(cur, 0) : dq.removeLast();
                        if (tail.val + 1 == mid) {
                            cur--;
                        } else {
                            tail.val++;
                            dq.addLast(tail);
                            break;
                        }
                    }
                    if (cur < 0) {
                        return false;
                    }
                }
                return true;
            }
        };

        int ans = ibs.binarySearch(1, n);
        out.println(ans);
    }
}

class Tag {
    int index;
    int val;

    public Tag(int index, int val) {
        this.index = index;
        this.val = val;
    }
}

