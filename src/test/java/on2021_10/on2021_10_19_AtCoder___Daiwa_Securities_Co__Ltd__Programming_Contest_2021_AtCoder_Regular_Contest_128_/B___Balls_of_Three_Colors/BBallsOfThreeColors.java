package on2021_10.on2021_10_19_AtCoder___Daiwa_Securities_Co__Ltd__Programming_Contest_2021_AtCoder_Regular_Contest_128_.B___Balls_of_Three_Colors;



import template.io.FastInput;
import template.io.FastOutput;

public class BBallsOfThreeColors {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int r = in.ri();
        int g = in.ri();
        int b = in.ri();

        int sum = r + g + b;
        if(r == sum || g == sum || b == sum){
            out.println(0);
            return;
        }

        long ans = inf;
        ans = Math.min(ans, solve(r, g, b));
        ans = Math.min(ans, solve(r, b, g));
        ans = Math.min(ans, solve(g, b, r));
        if(ans == inf){
            out.println(-1);
        }else{
            out.println(ans);
        }
    }

    long inf = (long)1e18;
    public long solve(int r, int g, int b){
        if(r < g){
            return solve(g, r, b);
        }
        if((r - g) % 3 != 0){
            return inf;
        }
        return r;
    }
}
