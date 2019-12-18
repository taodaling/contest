package contest;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class DDecreasingDebts {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        long[] remain = new long[n];
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            int d = in.readInt();
            remain[a] -= d;
            remain[b] += d;
        }

        int negIter = 0;
        int posIter = 0;

        List<long[]> ans = new ArrayList<>(n + n);
        while (posIter < n && negIter < n) {
            if (remain[posIter] <= 0) {
                posIter++;
                continue;
            }
            if (remain[negIter] >= 0) {
                negIter++;
                continue;
            }
            long used = Math.min(remain[posIter], -remain[negIter]);
            remain[posIter] -= used;
            remain[negIter] += used;
            ans.add(SequenceUtils.wrapArray(negIter + 1, posIter + 1, used));
        }

        out.println(ans.size());
        for(long[] e : ans){
            out.append(e[0]).append(' ').append(e[1]).append(' ').append(e[2]).append('\n');
        }
    }
}
