package on2021_05.on2021_05_23_AtCoder___AtCoder_Regular_Contest_120.B___Uniformly_Distributed;



import template.io.FastInput;
import template.io.FastOutput;

public class BUniformlyDistributed {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int h = in.ri();
        int w = in.ri();
        char[][] mat = new char[h][w];
        boolean[][] occur = new boolean[h + w][128];
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                mat[i][j] = in.rc();
                occur[i + j][mat[i][j]] = true;
            }
        }
        int mod = 998244353;
        long prod = 1;
        for(int i = 0; i < h + w; i++){
            if(occur[i]['R'] && occur[i]['B']){
                out.println(0);
                return;
            }
            if(occur[i]['R'] || occur[i]['B'] || !occur[i]['.']){
                continue;
            }
            prod = prod * 2 % mod;
        }
        out.println(prod);
    }
}
