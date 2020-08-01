package on2020_07.on2020_07_31_Codeforces___Codeforces_Round__381__Div__1_.E__Gosha_is_hunting2;



import net.egork.chelper.tester.Verdict;
import net.egork.chelper.checkers.Checker;

public class ETokenChecker implements Checker {
    public ETokenChecker(String parameters) {
    }

    public Verdict check(String input, String expectedOutput, String actualOutput) {
        return Math.abs(Double.parseDouble(expectedOutput) - Double.parseDouble(actualOutput)) < 1e-4 ?
                Verdict.OK : Verdict.WA;
    }
}
