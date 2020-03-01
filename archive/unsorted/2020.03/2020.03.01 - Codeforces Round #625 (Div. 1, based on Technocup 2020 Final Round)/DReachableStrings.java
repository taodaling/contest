package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.rand.Hash;

public class DReachableStrings {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] s = new char[n];
        in.readString(s, 0);
        int[] indexes = new int[n];
        StringBuilder reorder = new StringBuilder(n);
        IntegerDeque dq = new IntegerDequeImpl(n);
        for (int i = 0; i < n; i++) {
            if (s[i] == '0') {
                while (!dq.isEmpty()) {
                    int tail = dq.removeLast();
                    reorder.append(s[tail]);
                    indexes[tail] = reorder.length() - 1;
                }
                reorder.append(s[i]);
                indexes[i] = reorder.length() - 1;
            } else {
                if (dq.isEmpty()) {
                    dq.addLast(i);
                } else {
                    while (!dq.isEmpty()) {
                        int tail = dq.removeLast();
                        indexes[tail] = -1;
                    }
                    indexes[i] = -1;
                }
            }
        }
        if (!dq.isEmpty()) {
            reorder.append('1');
            indexes[dq.removeLast()] = reorder.length() - 1;
        }
        int[] newS = reorder.chars().toArray();
        Hash h31 = new Hash(newS.length, 31);
        Hash h41 = new Hash(newS.length, 41);
        h31.populate(i -> newS[i], newS.length);
        h41.populate(i -> newS[i], newS.length);
        int[] next = new int[n];
        int[] last = new int[n];
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                last[i] = -1;
            } else {
                last[i] = last[i - 1];
            }
            if (s[i] == '0') {
                last[i] = i;
            }
        }

        for (int i = n - 1; i >= 0; i--) {
            if (i == n - 1) {
                next[i] = n;
            } else {
                next[i] = next[i + 1];
            }
            if (s[i] == '0') {
                next[i] = i;
            }
        }

        int q = in.readInt();
        for (int i = 0; i < q; i++) {
            int l1 = in.readInt() - 1;
            int l2 = in.readInt() - 1;
            int len = in.readInt();
            int r1 = l1 + len - 1;
            int r2 = l2 + len - 1;
            boolean contain1 = next[l1] <= r1;
            boolean contain2 = next[l2] <= r2;
            if (contain1 != contain2) {
                out.println("No");
                continue;
            }
            if (!contain1 && !contain2) {
                out.println("Yes");
                continue;
            }
            if (parity(l1, next[l1]) != parity(l2, next[l2]) ||
                    parity(r1, last[r1]) != parity(r2, last[r2])) {
                out.println("No");
                continue;
            }
            l1 = indexes[next[l1]];
            r1 = indexes[last[r1]];
            l2 = indexes[next[l2]];
            r2 = indexes[last[r2]];
            if (h31.hashVerbose(l1, r1) == h31.hashVerbose(l2, r2) &&
                    h41.hashVerbose(l1, r1) == h41.hashVerbose(l2, r2)) {
                out.println("Yes");
                continue;
            }
            out.println("No");
        }
    }

    public int parity(int a, int b) {
        return (b - a) & 1;
    }
}
