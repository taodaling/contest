package contest;

import template.graph.PruferCode;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.List;

public class PrferCode {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] pf = new int[n - 2];
        in.populate(pf);
        for(int i = 0; i < n - 2; i++){
            pf[i]--;
        }
        List<Integer>[] g = PruferCode.pruferCode2Tree(pf);
        for (int i = 0; i < n; i++) {
            for (int x : g[i]) {
                if (x < i) {
                    out.append(x + 1).append(' ').append(i + 1).println();
                }
            }
        }
    }
}
