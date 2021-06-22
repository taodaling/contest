package on2021_06.on2021_06_20_Codeforces___Codeforces_LATOKEN_Round_1__Div__1___Div__2_.F1__Falling_Sand__Easy_Version_;



import chelper.AbstractChecker;
import net.egork.chelper.tester.Verdict;
import template.io.FastInput;

public class TokenChecker extends AbstractChecker {
    public TokenChecker(String parameters) {
        super(parameters);
    }

    @Override
    public Verdict check(FastInput stdin, FastInput expect, FastInput actual) {
        return expect.ri() >= actual.ri() ? Verdict.OK : Verdict.WA;
    }
}