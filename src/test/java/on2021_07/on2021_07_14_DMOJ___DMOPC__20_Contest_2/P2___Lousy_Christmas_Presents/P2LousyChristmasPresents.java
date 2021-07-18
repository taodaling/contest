package on2021_07.on2021_07_14_DMOJ___DMOPC__20_Contest_2.P2___Lousy_Christmas_Presents;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class P2LousyChristmasPresents {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] c = in.ri(n);
        int L = (int)1e6;
        int[] first = new int[L + 1];
        int[] last = new int[L + 1];
        Arrays.fill(first, n);
        Arrays.fill(last, -1);
        for(int i = 0; i < n; i++){
            first[c[i]] = Math.min(first[c[i]], i);
            last[c[i]] = Math.max(last[c[i]], i);
        }
        int best = 0;
        for(int i = 0; i < m; i++){
            int a = in.ri();
            int b = in.ri();
            if(first[a] > last[b]){
                continue;
            }
            best = Math.max(best, last[b] - first[a] + 1);
        }
        out.println(best);
    }
}
