package on2021_07.on2021_07_22_Codeforces___Codeforces_Round__552__Div__3_.G__Minimum_Possible_LCM;



import chelper.AbstractChecker;
import net.egork.chelper.tester.Verdict;
import template.io.FastInput;
import template.math.LCMs;

public class TokenChecker extends AbstractChecker {
    public TokenChecker(String parameters) {
        super(parameters);
    }

    @Override
    public Verdict check(FastInput stdin, FastInput expect, FastInput actual) {
        int n = stdin.ri();
        int[] a = stdin.ri(n);
        int[] ex = expect.ri(2);
        int[] ac = actual.ri(2);
        return LCMs.lcm(a[ex[0] - 1], a[ex[1] - 1]) ==
                LCMs.lcm(a[ac[0] - 1], a[ac[1] - 1]) ? Verdict.OK : Verdict.WA;
    }
}