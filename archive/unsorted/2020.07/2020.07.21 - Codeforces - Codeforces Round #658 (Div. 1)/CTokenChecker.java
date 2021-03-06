package contest;

import chelper.AbstractChecker;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.checkers.Checker;
import template.io.FastInput;

public class CTokenChecker extends AbstractChecker {
    public CTokenChecker(String parameters) {
        super(parameters);
    }

    @Override
    public Verdict check(FastInput stdin, FastInput expect, FastInput actual) {
        String ans = actual.readString();
        return ans.equalsIgnoreCase("YES") ? Verdict.OK : Verdict.WA;
    }
}
