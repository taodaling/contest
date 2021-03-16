package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class CBearAndUpDown {
    int[] a;
    int invalid;

    private int consider(int i) {
        if (i < 0 || i + 1 >= a.length) {
            return 0;
        }
        if (i % 2 == 0) {
            if (a[i] >= a[i + 1]) {
                return 1;
            }
        } else {
            if (a[i] <= a[i + 1]) {
                return 1;
            }
        }
        return 0;
    }

    private void discard(int i) {
        invalid -= consider(i - 1) + consider(i);
    }

    private void add(int i) {
        invalid += consider(i - 1) + consider(i);
    }

    public void swap(int i, int j) {
        if (i > j) {
            i ^= j;
            j ^= i;
            i ^= j;
        }
        if (i + 1 == j) {
            invalid -= consider(i - 1) + consider(i) + consider(i + 1);
            SequenceUtils.swap(a, i, j);
            invalid += consider(i - 1) + consider(i) + consider(i + 1);
            return;
        }
        discard(i);
        discard(j);
        SequenceUtils.swap(a, i, j);
        add(i);
        add(j);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        a = in.ri(n);
        int index = 0;
        for (int i = 0; i + 1 < n; i++) {
            int contrib = consider(i);
            invalid += contrib;
            if (contrib == 1) {
                index = i;
            }
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (i == index) {
                continue;
            }
            swap(i, index);
            if (invalid == 0) {
                ans++;
            }
            swap(i, index);
        }
        for (int i = 0; i < n; i++) {
            if (i == index + 1) {
                continue;
            }
            swap(i, index + 1);
            if (invalid == 0) {
                ans++;
            }
            swap(i, index + 1);
        }
        swap(index, index + 1);
        if (invalid == 0) {
            ans--;
        }
        swap(index, index + 1);
        out.println(ans);
    }
}
