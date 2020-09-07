package contest;

import chelper.AbstractChecker;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.checkers.Checker;
import template.io.FastInput;
import template.math.Modular;
import template.math.Power;

public class KthRootTokenChecker extends AbstractChecker {
    public KthRootTokenChecker(String parameters) {
        super(parameters);
    }

    @Override
    public Verdict check(FastInput stdin, FastInput expect, FastInput actual) {
        int t = stdin.readInt();
        while (t-- > 0) {
            int k = stdin.readInt();
            int y = stdin.readInt();
            int p = stdin.readInt();

            int x = actual.readInt();
            int exp = expect.readInt();
            if (x < -1 || x > p - 1) {
                return Verdict.WA;
            }

            if (x == -1) {
                if (exp != -1) {
                    return Verdict.WA;
                }
                continue;
            }
            int pow = new Power(new Modular(p)).pow(x, k);
            return pow == y ? Verdict.OK : Verdict.WA;
        }
        return Verdict.OK;
    }
}
