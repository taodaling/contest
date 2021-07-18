package on2021_07.on2021_07_15_DMOJ___DMOPC__20_Contest_1.P4___Victor_Makes_Bank;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.ModMatrix;
import template.utils.Debug;

public class P4VictorMakesBank {
    int mod = (int) 1e9 + 7;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.rl();
        int k = in.ri();
        int T = in.ri();
        int c = in.ri();
        int[][] transfer = new int[T + 1][T + 1];
        for (int i = 0; i <= T; i++) {
            transfer[Math.min(i + 1, T)][i] = 1;
            if (i == T) {
                transfer[0][i] = k;
            }
        }
        ModMatrix transferMat = new ModMatrix((i, j) -> transfer[i][j], T + 1, T + 1);
        ModMatrix transferMatC = ModMatrix.pow(transferMat, n - 1, mod);
        ModMatrix initState = new ModMatrix(T + 1, 1);
        initState.set(T, 0, c);
        ModMatrix finalState = ModMatrix.mul(transferMatC, initState, mod);
        debug.debug("transferMat", transferMat);
        debug.debug("finalState", finalState);
        long sum = 0;
        for(int i = 0; i <= T; i++){
            long each = i < T ? 1 : 2;
            sum += each * finalState.get(i, 0);
        }
        sum = DigitUtils.mod(sum, mod);
        out.println(sum);
    }
    Debug debug = new Debug(false);
}
