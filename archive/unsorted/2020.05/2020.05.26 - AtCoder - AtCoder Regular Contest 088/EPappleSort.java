package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.primitve.generated.datastructure.IntegerMultiWayDeque;

public class EPappleSort {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] s = new int[(int) 2e5 + 1];
        int n = in.readString(s, 1);
        for (int i = 1; i <= n; i++) {
            s[i] -= 'a';
        }

        int bits = 0;
        for (int i = 1; i <= n; i++) {
            bits ^= 1 << s[i];
        }

        if (Integer.bitCount(bits) > 1) {
            out.println(-1);
            return;
        }

        IntegerDequeImpl[] dqs = new IntegerDequeImpl['z' - 'a' + 1];
        for (int i = 0; i < dqs.length; i++) {
            dqs[i] = new IntegerDequeImpl(n);
        }
        for (int i = 1; i <= n; i++) {
            dqs[s[i]].addLast(i);
        }

        boolean[] used = new boolean[n + 1];
        IntegerBIT bit = new IntegerBIT(n);
        for (int i = 1; i <= n; i++) {
            bit.update(i, 1);
        }

        long ans = 0;
        int l = 1;
        int r = n;
        while (l < r) {
            if (used[l]) {
                l++;
                continue;
            }
            if (used[r]) {
                r--;
                continue;
            }

            int val = dqs[s[l]].size() > 1 ? s[l] : s[r];
            int head = dqs[val].removeFirst();
            int tail = dqs[val].removeLast();
            used[head] = true;
            used[tail] = true;
            bit.update(head, -1);
            bit.update(tail, -1);
            ans += bit.query(1, head);
            ans += bit.query(tail, n);
        }

        out.println(ans);
    }
}
