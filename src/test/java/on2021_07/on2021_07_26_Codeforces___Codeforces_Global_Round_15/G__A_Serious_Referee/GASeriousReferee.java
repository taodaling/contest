package on2021_07.on2021_07_26_Codeforces___Codeforces_Global_Round_15.G__A_Serious_Referee;



import template.io.FastInput;
import template.io.FastOutput;
import template.rand.RandomWrapper;

public class GASeriousReferee {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[][] step = new int[k][];
        for (int i = 0; i < k; i++) {
            int q = in.ri();
            step[i] = in.ri(q);
            for (int j = 0; j < q; j++) {
                step[i][j]--;
            }
        }
        long end = System.currentTimeMillis() + 800;
        while (System.currentTimeMillis() < end) {
            long random = RandomWrapper.INSTANCE.nextLong(1L << n);
            
        }
    }
}
