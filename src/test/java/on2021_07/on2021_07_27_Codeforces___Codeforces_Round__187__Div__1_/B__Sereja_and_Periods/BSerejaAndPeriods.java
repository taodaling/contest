package on2021_07.on2021_07_27_Codeforces___Codeforces_Round__187__Div__1_.B__Sereja_and_Periods;



import template.io.FastInput;
import template.io.FastOutput;

public class BSerejaAndPeriods {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int b = in.ri();
        int d = in.ri();
        char[] s = in.rs().toCharArray();
        char[] t = in.rs().toCharArray();
        long[][] matchInfo = new long[t.length][];
        int state = 0;
        long score = 0;
        boolean skiped = false;
        for (int i = 0; i < b; i++) {
            if (matchInfo[state] != null) {
                if (!skiped) {
                    skiped = true;

                    //find loop
                    long step = i - matchInfo[state][0];
                    long got = score - matchInfo[state][1];

                    //fast
                    long remain = b - i;
                    long skip = remain / step;
                    i += skip * step;
                    score += got * skip;
                    if (i >= b) {
                        break;
                    }
                }
            } else {
                matchInfo[state] = new long[]{i, score};
            }
            for (char c : s) {
                if (t[state] == c) {
                    state++;
                    if (state == t.length) {
                        state = 0;
                        score++;
                    }
                }
            }
        }

        long ans = score / d;
        out.println(ans);
    }
}
