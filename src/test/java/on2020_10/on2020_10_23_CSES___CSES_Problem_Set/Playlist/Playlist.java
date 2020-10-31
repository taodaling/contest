package on2020_10.on2020_10_23_CSES___CSES_Problem_Set.Playlist;



import template.io.FastInput;
import template.primitve.generated.datastructure.IntegerDiscreteMap;

import java.io.PrintWriter;

public class Playlist {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] ids = new int[n];
        in.populate(ids);
        IntegerDiscreteMap.discrete(ids);

        boolean[] exists = new boolean[n];
        int r = -1;
        int ans = 0;
        for (int i = 0; i < n; i++) {
            r = Math.max(i - 1, r);
            while (r + 1 < n && !exists[ids[r + 1]]) {
                r++;
                exists[ids[r]] = true;
            }
            ans = Math.max(ans, r - i + 1);
            exists[ids[i]] = false;
        }
        out.println(ans);
    }
}
