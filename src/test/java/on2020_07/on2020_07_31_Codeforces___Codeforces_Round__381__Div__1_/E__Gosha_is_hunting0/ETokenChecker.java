package on2020_07.on2020_07_31_Codeforces___Codeforces_Round__381__Div__1_.E__Gosha_is_hunting0;



import chelper.AbstractChecker;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.checkers.Checker;
import template.io.FastInput;

public class ETokenChecker extends AbstractChecker {

    public ETokenChecker(String parameters) {
        super(parameters);
    }

    @Override
    public Verdict check(FastInput stdin, FastInput expect, FastInput actual) {
        double a = expect.readDouble();
        double b = actual.readDouble();
        return Math.abs(a - b) < 1e-4 ? Verdict.OK : Verdict.WA;
    }
}
