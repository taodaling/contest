package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.math.BigInteger;
import java.util.Arrays;

public class Triangle {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        BigInteger[] data = new BigInteger[n];
        for (int i = 0; i < n; i++) {
            data[i] = new BigInteger(in.readString());
        }
        debug.debugArray("data", data);
        Arrays.sort(data);
        for (int i = 2; i < n; i++) {
            if(data[i - 1].add(data[i - 2]).compareTo(data[i]) > 0){
                for(int j = i - 2; j <= i; j++){
                    out.append(data[j]).append(' ');
                }
                return;
            }
        }
        out.println("0 0 0");
    }
}
