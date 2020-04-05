package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ADreamoonLikesColoring {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] l = new int[m];
        for (int i = 0; i < m; i++) {
            l[i] = in.readInt();
        }
        int[] intervals = new int[m];
        for (int i = 0; i < m; i++) {
            intervals[i] = i;
        }
        for (int i = 0; i < m; i++) {
            if (intervals[i] + l[i] - 1 >= n) {
                out.println(-1);
                return;
            }
        }
        int last = n;
        for (int i = m - 1; i >= 0; i--) {
            int left = Math.max(intervals[i], last - l[i]);
            intervals[i] = left;
            last = intervals[i];
        }

        if(intervals[0] != 0){
            out.println(-1);
            return;
        }

        for(int x : intervals){
            out.append(x + 1).append(' ');
        }
    }
}
