package on2020_01.on2020_01_18_Keyence_Programming_Contest_2020.C___Subarray_Sum;



import template.io.FastInput;
import template.io.FastOutput;

public class CSubarraySum {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int s = in.readInt();

        int[] row = new int[n];
        for (int i = 0; i < k; i++) {
            row[i] = s;
        }
        if (s > 1e6) {
            for (int i = k; i < n; i++) {
                row[i] = 1;
            }
        } else {
            for (int i = k; i < n; i++) {
                row[i] = (int) 1e9;
            }
        }

        for(int x : row){
            out.append(x).append(' ');
        }
    }
}
