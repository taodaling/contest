package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.readInt();
        Recorder recorder = new Recorder();
        int[] d = new int[m];
        long[] c = new long[m];
        for (int i = 0; i < m; i++) {
            d[i] = in.readInt();
            c[i] = in.readLong();
        }
        for (int i = m - 1; i >= 0; i--) {
            recorder.handle(d[i], c[i]);
        }
        out.println(recorder.cnt);
    }

}

class Recorder {
    int remainder = 0;
    long cnt = -1;

    public void handle(int x, long t) {
        if (x >= 10) {
            cnt += t;
            x = x / 10 + x % 10;
            handle(x, t);
            return;
        }
        if (t == 1) {
            cnt++;
            remainder += x;
            if (remainder >= 10) {
                remainder = remainder / 10 + remainder % 10;
                cnt++;
            }
            return;
        }
        handle(x + x, t / 2);
        cnt += t / 2;
        if (t % 2 == 1) {
            handle(x, 1);
        }
    }
}
