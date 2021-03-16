package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntMath;
import template.utils.Debug;

import java.util.Arrays;

public class DWorldOfDarkraft2 {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();

        int m = 1000;
        double[] prevProb = new double[m];
        double[] nextProb = new double[m];
        double[] prevExp = new double[m];
        double[] nextExp = new double[m];

        long[] sums = new long[m];
        for(int i = 0; i < m; i++){
            sums[i] = i;
            if(i > 0){
                sums[i] += sums[i - 1];
            }
        }

        double hit = 1d / k;
        prevProb[1] = 1;
        for (int i = 0; i < n; i++) {
            debug.debug("prevProb", prevProb);
            debug.debug("prevExp", prevExp);
            Arrays.fill(nextProb, 0);
            Arrays.fill(nextExp, 0);

            for (int j = 1; j < m; j++) {
                if(prevProb[j] < 1e-15 && prevExp[j] < 1e-15){
                    continue;
                }

                double prob = hit / (j + 1);
                nextProb[j] += (1 - prob) * prevProb[j];
                nextExp[j] += prob * (prevExp[j] * j + sums[j] * prevProb[j]) +
                        (1 - hit) * prevExp[j];
                if (j + 1 < m) {
                    nextProb[j + 1] += prob * prevProb[j];
                    nextExp[j + 1] += prob * (prevExp[j] + prevProb[j] * j);
                }
            }
            double[] tmp = prevProb;
            prevProb = nextProb;
            nextProb = tmp;
            tmp = prevExp;
            prevExp = nextExp;
            nextExp = tmp;
        }
        debug.debug("prevProb", prevProb);
        debug.debug("prevExp", prevExp);
        double sum = 0;
        double sumOfProb = 0;
        for (int i = 0; i < m; i++) {
            sum += prevExp[i];
            sumOfProb += prevProb[i];
        }
        debug.debug("sumOfProb", sumOfProb);
        sum *= k;
        out.println(sum);
    }
}
