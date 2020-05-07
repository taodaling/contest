package chelper;

import net.egork.chelper.checkers.Checker;
import net.egork.chelper.tester.StringInputStream;
import net.egork.chelper.tester.Verdict;
import template.io.FastInput;

public abstract class AbstractChecker implements Checker {
    public AbstractChecker(String parameters) {
    }

    public Verdict check(String input, String expectedOutput, String actualOutput) {
        return check(new FastInput(new StringInputStream(input)),
                new FastInput(new StringInputStream(expectedOutput)),
                new FastInput(new StringInputStream(actualOutput)));
    }

    public abstract Verdict check(FastInput stdin, FastInput expect, FastInput actual);
}