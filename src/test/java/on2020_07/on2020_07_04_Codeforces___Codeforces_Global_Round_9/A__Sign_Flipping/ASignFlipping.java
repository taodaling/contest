package on2020_07.on2020_07_04_Codeforces___Codeforces_Global_Round_9.A__Sign_Flipping;



import template.io.FastInput;
import template.io.FastOutput;

public class ASignFlipping {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                a[i] = Math.abs(a[i]);
            } else {
                a[i] = -Math.abs(a[i]);
            }
        }

        for(int x : a){
            out.append(x).append(' ');
        }
        out.println();
    }
}
