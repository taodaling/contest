package on2020_05.on2020_05_26_AtCoder___AtCoder_Regular_Contest_088.C___Multiple_Gift;



import template.io.FastInput;
import template.io.FastOutput;

public class CMultipleGift {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long x = in.readLong();
        long y = in.readLong();
        int cnt = 1;
        while (x * 2 <= y) {
            x *= 2;
            cnt++;
        }
        out.println(cnt);
    }
}
