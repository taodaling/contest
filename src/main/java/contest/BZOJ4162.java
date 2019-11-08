package contest;

import template.FastInput;
import template.FastOutput;
import template.NumberTheory;

public class BZOJ4162 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        NumberTheory.Modular mod = new NumberTheory.Modular(1e9 + 7);
        char[] n = new char[20000];
        int len = in.readString(n, 0);
        for (int i = 0; i < len; i++) {
            n[i] -= '0';
        }
        while (len > 0 && n[len - 1] == 0) {
            len--;
        }
        int k = in.readInt();

    }
}
