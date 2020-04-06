package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Composite;
import template.math.Modular;
import template.math.Power;
import template.utils.Debug;

import java.util.Arrays;

public class Sequences {
    Modular mod = new Modular(1e9 + 7);
    Power power = new Power(mod);
    Composite comp = new Composite(500000, mod);
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[500000];
        int n = in.readString(s, 0);

        int[] cnts = new int[128];
        int contrib1 = 0;
        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (s[i] == '0') {
                contrib1 = mod.plus(contrib1, cnts['1']);
            }
            cnts[s[i]]++;
        }

        int mark = cnts['?'];
        contrib1 = mod.mul(contrib1, power.pow(2, mark));

        int contrib2 = 0;
        if (mark > 0) {
            int factor = power.pow(2, mark - 1);
            Arrays.fill(cnts, 0);
            for (int i = 0; i < n; i++) {
                if (s[i] == '?') {
                    contrib2 = mod.plus(contrib2, mod.mul(cnts['1'], factor));
                }
                cnts[s[i]]++;
            }
            Arrays.fill(cnts, 0);
            for (int i = n - 1; i >= 0; i--) {
                if (s[i] == '?') {
                    contrib2 = mod.plus(contrib2, mod.mul(cnts['0'], factor));
                }
                cnts[s[i]]++;
            }
        }

        int contrib3 = 0;
        if (mark >= 2) {
            contrib3 = mod.mul(comp.composite(mark, 2), power.pow(2, mark - 2));
        }

        debug.debug("contrib1", contrib1);

        debug.debug("contrib2", contrib2);

        debug.debug("contrib3", contrib3);
        ans = mod.plus(contrib1, mod.plus(contrib2, contrib3));


        out.println(ans);
    }
}
