package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class BinarySearch {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int x = in.readInt();
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = in.readInt();
        }
        int ans = binarySearch(data, x);
        out.println(ans);
    }

    public int binarySearch(int[] data, int x) {
        int l = 0;
        int r = data.length - 1;
        while (l < r) {
            int m = (l + r) / 2;
            if (data[m] <= x) {
                l = m;
            } else {
                r = m;
            }
        }
        return l;
    }
}
