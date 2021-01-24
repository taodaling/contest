package on2021_01.on2021_01_16_AtCoder___KEYENCE_Programming_Contest_2021.B___Mex_Boxes;



import template.io.FastInput;
import template.io.FastOutput;

public class BMexBoxes {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[] cnts = new int[n + 1];
        for (int i = 0; i < n; i++) {
            cnts[in.ri()]++;
        }
        long sum = 0;
        int remain = k;
        for (int i = 0; i <= n; i++) {
            int next = Math.min(remain, cnts[i]);
            sum += (long) i * (remain - next);
            remain = next;
        }
        out.println(sum);
    }
}
