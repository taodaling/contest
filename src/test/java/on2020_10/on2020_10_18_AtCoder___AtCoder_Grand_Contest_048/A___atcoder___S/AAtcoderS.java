package on2020_10.on2020_10_18_AtCoder___AtCoder_Grand_Contest_048.A___atcoder___S;



import template.io.FastInput;
import template.utils.SequenceUtils;

import java.io.PrintWriter;

public class AAtcoderS {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        String atcoder = "atcoder";
        char[] seq = in.readString().toCharArray();
        int inf = (int) 1e9;
        int ans = inf;
        int now = 0;
        for (int i = 0; i < atcoder.length() && i < seq.length; i++) {
            for (int j = i; j < seq.length; j++) {
                if (seq[j] > atcoder.charAt(i)) {
                    ans = Math.min(ans, now + j - i);
                    break;
                }
            }
            for (int j = i; j < seq.length; j++) {
                if (seq[j] == atcoder.charAt(i)) {
                    SequenceUtils.rotate(seq, i, j, i + 1);
                    now += j - i;
                    break;
                }
            }

            if (seq[i] != atcoder.charAt(i)) {
                break;
            }
            if (i == atcoder.length() - 1 && seq.length > atcoder.length()) {
                ans = Math.min(ans, now);
            }
        }
        out.println(ans == inf ? -1 : ans);
    }
}
