package on2019_12.on2019_12_29_Good_Bye_2019.C__Make_Good;



import template.io.FastInput;
import template.io.FastOutput;

public class CMakeGood {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        long sum = 0;
        long xor = 0;
        for (int i = 0; i < n; i++) {
            sum += a[i];
            xor ^= a[i];
        }
        long[] ans = new long[3];
        if (sum % 2 == 1) {
            ans[0] = 1;
            sum += 1;
            xor ^= 1;
        }
        ans[1] = xor;
        sum += ans[1];
        xor = 0;

        ans[2] = sum;

        out.append(3).append('\n');
        for(int i = 0; i < 3; i++){
            out.append(ans[i]).append(' ');
        }
        out.println();
    }
}
