package contest;

import template.CompareUtils;
import template.FastInput;
import template.FastOutput;
import template.SparseTable;

import java.util.Comparator;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Integer[] monster = new Integer[n];

        for (int i = 0; i < n; i++) {
            monster[i] = in.readInt();
        }
        SparseTable<Integer> st = new SparseTable<>(monster, n, (a, b) -> a > b ? a : b);
        int m = in.readInt();
        int[] s = new int[n + 1];
        for (int i = 0; i < m; i++) {
            int p = in.readInt();
            int live = in.readInt();
            s[live] = Math.max(s[live], p);
        }
        for (int i = n - 1; i >= 1; i--) {
            s[i] = Math.max(s[i], s[i + 1]);
        }

        if (st.query(0, n - 1) > s[1]) {
            out.println(-1);
            return;
        }

        int day = 0;
        int pos = 0;

        while (pos < n) {
            int l = pos;
            int r = n - 1;
            while (l < r) {
                int mid = (l + r + 1) / 2;
                if (st.query(l, mid) > s[mid - l + 1]) {
                    r = mid - 1;
                } else {
                    l = mid;
                }
            }

            day++;
            pos = l + 1;
        }

        out.println(day);
    }
}
