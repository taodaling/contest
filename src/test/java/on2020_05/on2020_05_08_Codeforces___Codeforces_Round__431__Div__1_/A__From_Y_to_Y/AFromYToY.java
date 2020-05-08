package on2020_05.on2020_05_08_Codeforces___Codeforces_Round__431__Div__1_.A__From_Y_to_Y;



import template.io.FastInput;
import template.io.FastOutput;

public class AFromYToY {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();

        if (n == 0) {
            out.append('a');
            return;
        }

        for (int i = 'a'; i <= 'z' && n > 0; i++) {
            int cnt = 0;
            while (pick2(cnt + 1) <= n) {
                cnt++;
            }
            n -= pick2(cnt);
            for (int j = 0; j < cnt; j++) {
                out.append((char) i);
            }
        }


    }

    public long pick2(long n) {
        return n * (n - 1) / 2;
    }
}
