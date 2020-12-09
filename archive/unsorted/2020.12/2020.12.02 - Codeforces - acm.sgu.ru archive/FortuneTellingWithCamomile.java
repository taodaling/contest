package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class FortuneTellingWithCamomile {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri() - 1;
        int m = in.ri();
        String[] phrase = new String[m];
        for (int i = 0; i < m; i++) {
            phrase[i] = in.rs();
        }
        n %= m;
        out.println(phrase[n]);
    }
}
