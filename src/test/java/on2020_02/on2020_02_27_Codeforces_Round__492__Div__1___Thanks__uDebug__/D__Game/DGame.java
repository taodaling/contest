package on2020_02.on2020_02_27_Codeforces_Round__492__Div__1___Thanks__uDebug__.D__Game;



import template.io.FastInput;
import template.io.FastOutput;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class DGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int r = in.readInt();
        long sum = 0;
        int[] val = new int[1 << n];
        for (int i = 0; i < val.length; i++) {
            val[i] = in.readInt();
            sum += val[i];
        }
        output(out, sum, val.length);
        for(int i = 0; i < r; i++){
            int z = in.readInt();
            int g = in.readInt();
            sum -= val[z];
            val[z] = g;
            sum += val[z];
            output(out, sum, val.length);
        }
    }

    public void output(FastOutput out, long sum, long cnt) {
        out.println((double)sum / cnt);
    }
}
