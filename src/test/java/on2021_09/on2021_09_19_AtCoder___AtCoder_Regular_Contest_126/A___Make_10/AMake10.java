package on2021_09.on2021_09_19_AtCoder___AtCoder_Regular_Contest_126.A___Make_10;



import template.io.FastInput;
import template.io.FastOutput;

public class AMake10 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long a = in.rl();
        long b = in.rl();
        long c = in.rl();
        long six = b / 2;
        long ans = 0;

        //4 3 3
        long t1 = Math.min(c, six);
        c -= t1;
        six -= t1;
        ans += t1;

        //2 2 3 3
        long t2 = Math.min(a / 2, six);
        a -= t2 * 2;
        six -= t2;
        ans += t2;

        //4 4 2
        long t3 = Math.min(c / 2, a);
        c -= t3 * 2;
        a -= t3;
        ans += t3;

        //4 2 2 2
        long t4 = Math.min(c, a / 3);
        c -= t4;
        a -= t4 * 3;
        ans += t4;

        //2 2 2 2 2
        long t5 = a / 5;
        ans += t5;

        out.println(ans);
    }
}
