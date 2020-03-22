package on2020_03.on2020_03_21_AtCoder_Grand_Contest_043.B___123_Triangle;



import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class B123Triangle {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readChar() - '0';
        }
        int time = n - 1;
        time -= 1;
        debug.debug("a", a);
        int[] cnt = new int[3];
        cast(a);

        debug.debug("a", a);
        for(int i = 0; i < n - 1; i++){
            cnt[a[i]]++;
        }
        if (cnt[1] > 0) {
            for (int i = 0; i < n; i++) {
                a[i] &= 1;
            }
        } else {
            for (int i = 0; i < n; i++) {
                a[i] >>= 1;
            }
        }

        cast(a, time);
        if (cnt[1] == 0) {
            for (int i = 0; i < n; i++) {
                a[i] <<= 1;
            }
        }

        out.println(a[0]);
    }

    public void cast(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            a[i] = Math.abs(a[i] - a[i + 1]);
        }
    }

    public void cast(int[] a, int time) {
        debug.debug("time", time);
        debug.debug("a", a);
        if (time == 0) {
            return;
        }
        int n = a.length;
        int floor = 1 << Log2.floorLog(time);
        for (int i = 0; i + floor < n; i++) {
            a[i] ^= a[i + floor];
        }
        cast(a, time - floor);
    }
}
