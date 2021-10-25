package on2021_10.on2021_10_19_AtCoder___Daiwa_Securities_Co__Ltd__Programming_Contest_2021_AtCoder_Regular_Contest_128_.A___Gold_and_Silver;



import template.io.FastInput;
import template.io.FastOutput;

public class AGoldAndSilver {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n + 2];
        a[0] = -(int) 2e9;
        a[n + 1] = (int) 2e9;
        for (int i = 1; i <= n; i++) {
            a[i] = in.ri();
        }
        int last = a[0];
        for (int i = 1; i <= n; i++) {
            if (a[i] == a[i + 1]) {
                out.append(0).append(' ');
                continue;
            }
            if (a[i] > last && a[i] > a[i + 1] || a[i] < last && a[i] < a[i + 1]) {
                out.append(1);
            } else {
                out.append(0);
            }
            last = a[i];
            out.append(' ');
        }
    }
}
