package on2020_08.on2020_08_19_Luogu.P5431_________2;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.math.Power;

public class P54312 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int p = in.readInt();
        int k = in.readInt();

        Modular mod = new Modular(p);
        Power pow = new Power(mod);

        long sum = 0;
        long top = 1;
        int[] a = new int[n + 1];
        int[] pre = new int[n + 2];
        int[] suf = new int[n + 2];

        for (int i = 1; i <= n; i++) {
            a[i] = in.readInt();
        }
        pre[0] = 1;
        for (int i = 1; i <= n; i++) {
            pre[i] = (int) ((long) pre[i - 1] * a[i] % p);
        }
        suf[n + 1] = 1;
        for (int i = n; i >= 1; i--) {
            suf[i] = (int) ((long) suf[i + 1] * a[i] % p);
        }
        for (int i = 1; i <= n; i++) {
            top = top * k % p;
            sum += top * pre[i - 1] % p * suf[i + 1] % p;
        }
        out.println(sum % p * pow.inverse(pre[n]) % p);
    }
}
