package on2021_03.on2021_03_21_Codeforces___Codeforces_Round__709__Div__1__based_on_Technocup_2021_Final_Round_.E__Vabank;



import template.io.FastInput;
import template.io.FastOutput;

public class EVabank {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long remain = 1;
        out.printf("? %d", 1).println().flush();
        int cnt = 0;
        cnt++;
        assert cnt <= 105;
        String res = in.readString();
        if (res.equals("Fraudster!")) {
            out.println("! 0").flush();
            return;
        }
        remain++;
        long lo = 1;
        long hi = (long) 1e14;
        while (lo < hi) {
            long nextGuess = Math.min(remain, hi);
            cnt++;
            assert cnt <= 105;
            out.printf("? %d", nextGuess).println().flush();
            res = in.readString();
            if (res.equals("Fraudster!")) {
                hi = nextGuess - 1;
                remain -= nextGuess;
                break;
            } else {
                remain += nextGuess;
                lo = nextGuess;
            }
        }

        while (lo < hi) {
            long mid = (lo + hi + 1) / 2;
            while (remain < mid) {
                cnt++;
                assert cnt <= 105;
                out.printf("? %d", lo).println().flush();
                in.rs();
                remain += lo;
            }
            out.printf("? %d", mid).println().flush();
            cnt++;
            assert cnt <= 105;
            res = in.readString();
            if (res.equals("Fraudster!")) {
                remain -= mid;
                hi = mid - 1;
            } else {
                remain += mid;
                lo = mid;
            }
        }

        out.printf("! %d", lo).println().flush();
    }
}
