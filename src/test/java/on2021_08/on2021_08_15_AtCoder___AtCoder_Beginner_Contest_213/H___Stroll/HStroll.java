package on2021_08.on2021_08_15_AtCoder___AtCoder_Beginner_Contest_213.H___Stroll;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Power;
import template.math.PrimitiveRoot;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.polynomial.IntPolyNTT;
import template.utils.Debug;
import template.utils.PrimitiveBuffers;

public class HStroll {
    int mod = 998244353;
    IntPoly poly = new IntPolyFFT(mod);
    int n;
    Edge[] edges;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        int m = in.ri();
        int T = in.ri();
        edges = new Edge[m * 2];
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge();
            edges[i].a = in.ri() - 1;
            edges[i].b = in.ri() - 1;
            edges[i].way = new int[T + 1];
            for (int j = 1; j <= T; j++) {
                edges[i].way[j] = in.ri();
            }
            edges[i + m] = new Edge();
            edges[i + m].a = edges[i].b;
            edges[i + m].b = edges[i].a;
            edges[i + m].way = edges[i].way;
        }

        int[][] dp = new int[n][T + 1];
        dp[0][0] = 1;
        dac(dp, 0, T);
        int ans = dp[0][T];
        out.println(ans);
    }

    public void dac(int[][] dp, int l, int r) {
        if (l >= r) {
            return;
        }
        int m = (l + r) / 2;
        dac(dp, l, m);

        //contribute
        int max = r - l;
        for (Edge e : edges) {
            int[] b = PrimitiveBuffers.allocIntPow2(max + 1);
            for (int i = 0; i <= max; i++) {
                b[i] = e.way[i];
            }
            int[] a = PrimitiveBuffers.allocIntPow2(m - l + 1);
            for (int i = l; i <= m; i++) {
                a[i - l] = dp[e.a][i];
            }
            int[] conv = poly.convolution(a, b);
            for (int j = m + 1; j <= r && j - l < conv.length; j++) {
                dp[e.b][j] += conv[j - l];
                if(dp[e.b][j] >= mod){
                    dp[e.b][j] -= mod;
                }
            }
            PrimitiveBuffers.release(b, a, conv);
        }

        dac(dp, m + 1, r);
    }

    Debug debug = new Debug(false);
}

class Edge {
    int a;
    int b;
    int[] way;
}