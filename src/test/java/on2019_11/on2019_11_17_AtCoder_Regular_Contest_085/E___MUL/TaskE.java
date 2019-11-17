package on2019_11.on2019_11_17_AtCoder_Regular_Contest_085.E___MUL;





import template.*;

import java.util.Arrays;
import java.util.Random;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] data = new int[n];
        for(int i = 0; i < n; i++){
            data[i] = in.readInt();
        }
        long sum = 0;
        for(int i = 0; i < n; i++){
            sum += data[i];
        }

        double[] dDouble = Arrays.stream(data).mapToDouble(x -> -x).toArray();
        MinimumCloseSubGraph mcsg = new MinimumCloseSubGraph(dDouble);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if ((j + 1) % (i + 1) == 0) {
                    mcsg.addDependency(i, j);
                }
            }
        }

        long ans = sum + DigitUtils.round(mcsg.solve());
        out.println(ans);
    }
}
