package contest;

import chelper.AbstractChecker;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.checkers.Checker;
import template.io.FastInput;

public class FTokenChecker extends AbstractChecker {
    public FTokenChecker(String parameters) {
        super(parameters);
    }

    @Override
    public Verdict check(FastInput stdin, FastInput expect, FastInput actual) {
        int n = stdin.readInt();
        int[] s = new int[n];
        int[] t = new int[n];
        int[] u = new int[n];
        int[] v = new int[n];
        stdin.populate(s);
        stdin.populate(t);
        stdin.populate(u);
        stdin.populate(v);

        long first = actual.readInt();
        if (first == -1) {
            return Verdict.WA;
        }

        return Verdict.OK;
    }
}
