package on2020_07.on2020_07_30_Codeforces___Codeforces_Round__381__Div__1_.A__Alyona_and_mex;



import template.io.FastInput;
import template.io.FastOutput;

public class AAlyonaAndMex {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int max = n;
        for(int i = 0; i < m; i++){
            int l = in.readInt();
            int r = in.readInt();
            max = Math.min(max, r - l + 1);
        }
        out.println(max);
        for(int i = 0; i < n; i++){
            out.append(i % max).append(' ');
        }

    }
}
