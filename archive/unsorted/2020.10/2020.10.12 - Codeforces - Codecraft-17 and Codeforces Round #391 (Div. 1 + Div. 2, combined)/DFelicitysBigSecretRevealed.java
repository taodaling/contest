package contest;

import template.binary.Log2;
import template.io.FastInput;
import template.math.DigitUtils;
import template.utils.SequenceUtils;

import java.io.PrintWriter;
import java.util.Arrays;

public class DFelicitysBigSecretRevealed {
    int mod = (int) (1e9 + 7);
    int inf = (int) 1e8;

    public int eval(String s) {
        int val = 0;
        for (int i = 0; i < s.length() && val < inf; i++) {
            val = val * 2 + s.charAt(i) - '0';
        }
        return Math.min(val, inf);
    }

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        String s = in.readString();

        int limit = 20;
        int[][] last = new int[12][1 << limit];
        int[][] next = new int[12][1 << limit];

        int mask = 1 << limit;
        last[0][0]++;
        for (int i = 0; i < n; i++) {
            //cut or not
            for (int j = 0; j < 12; j++) {
                Arrays.fill(next[j], 0);
            }
            //remove next
            int vi = s.charAt(i) - '0';
            next[0][0]++;
            for (int j = 0; j <= 11; j++) {
                for (int k = 0; k < mask; k++) {
                    //cut next
                    int state = j + j + vi;
                    int bits = k | (1 << state - 1);
                    if (state > 0 && state <= limit) {
                        next[0][bits] += last[j][k];
                        if (next[0][bits] >= mod) {
                            next[0][bits] -= mod;
                        }
                    }
                    //not cut
                    int go = Math.min(j + j + vi, 11);
                    next[go][k] += last[j][k];
                    if (next[go][k] >= mod) {
                        next[go][k] -= mod;
                    }
                }
            }

            int[][] tmp = last;
            last = next;
            next = tmp;
        }

        long ans = 0;

        for (int j = 1; j < 1 << limit; j++) {
            int log = Log2.floorLog(j);
            if (j != (1 << log + 1) - 1) {
                continue;
            }
            for (int i = 0; i <= 11; i++) {
                ans += last[i][j];
            }
        }

        ans %= mod;
        out.println(ans);
    }

    public int calc(int x) {
        int len = 0;
        for (int i = 1; i <= x; i++) {
            len += Log2.floorLog(i) + 1;
        }
        return len;
    }
}
