package on2021_05.on2021_05_30_AtCoder___AtCoder_Beginner_Contest_187.F___Close_Group;



import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class FCloseGroup {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] adj = new int[n];
        for(int i = 0; i < n; i++){
            adj[i] |= 1 << i;
        }
        for(int i = 0; i < m; i++){
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            adj[a] |= 1 << b;
            adj[b] |= 1 << a;
        }
        int[] edgeAnd = new int[1 << n];
        edgeAnd[0] = (1 << n) - 1;
        for(int i = 1; i < 1 << n; i++){
            int floorLog = Log2.floorLog(i);
            edgeAnd[i] = edgeAnd[i - (1 << floorLog)] & adj[floorLog];
        }
        int[] minGroup = new int[1 << n];
        for(int i = 1; i < 1 << n; i++){
            minGroup[i] = n;
            for(int j = i + 1; j > 0; ){
                j = (j - 1) & i;
                if((edgeAnd[j] & j) == j){
                    minGroup[i] = Math.min(minGroup[i], minGroup[i - j] + 1);
                }
            }
        }

        debug.debug("minGroup", minGroup);
        int ans = minGroup[minGroup.length - 1];
        out.println(ans);
    }

    Debug debug = new Debug(false);
}
