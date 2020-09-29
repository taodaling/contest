package contest;

import chelper.AbstractChecker;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.checkers.Checker;
import template.io.FastInput;
import template.utils.CompareUtils;
import template.utils.Debug;

public class ETokenChecker extends AbstractChecker {
    public ETokenChecker(String parameters) {
        super(parameters);
    }

    Debug debug = new Debug(true);

    @Override
    public Verdict check(FastInput stdin, FastInput expect, FastInput actual) {
        long a = stdin.readInt();
        long b = stdin.readInt();
        int n = actual.readInt();
        if (n > 2e5 || n < 0) {
            return Verdict.WA;
        }
        long[] data = new long[(int) 2e5];
        data[0] = a;
        data[1] = b;
        for (int i = 0; i < n; i++) {
            char c = actual.readChar();

            if (c == 'd') {
                debug.debug("op", i);
                int x = actual.readInt();
                debug.debug("a[" + x + "]", data[x]);
                continue;
            }

            int l = actual.readInt();
            int r = actual.readInt();
            int to = actual.readInt();

            if (CompareUtils.minOf(l, r, to) < 0 ||
                    CompareUtils.maxOf(l, r, to) >= 2e5) {
                return Verdict.WA;
            }

            if (c == '+') {
                data[to] = data[l] + data[r];
            } else {
                data[to] = data[l] < data[r] ? 1 : 0;
            }
        }

        return data[0] * data[1] == data[2] ? Verdict.OK : Verdict.WA;
    }
}
