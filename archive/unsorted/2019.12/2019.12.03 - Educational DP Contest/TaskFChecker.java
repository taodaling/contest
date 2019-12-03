package contest;

import net.egork.chelper.tester.StringInputStream;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.checkers.Checker;
import template.io.FastInput;

public class TaskFChecker implements Checker {
    public TaskFChecker(String parameters) {
    }

    public Verdict check(String input, String expectedOutput, String actualOutput) {
        expectedOutput = expectedOutput.trim();
        actualOutput = actualOutput.trim();
        if(expectedOutput.length() != actualOutput.length()){
            return Verdict.WA;
        }
        FastInput in = new FastInput(new StringInputStream(input));
        String s = in.readString();
        String t = in.readString();

        int cnt1 = 0;
        for(int i = 0; i < s.length() && cnt1 < actualOutput.length(); i++){
            if(s.charAt(i) == actualOutput.charAt(cnt1)){
                cnt1++;
            }
        }
        int cnt2 = 0;
        for(int i = 0; i < t.length() && cnt2 < actualOutput.length(); i++){
            if(t.charAt(i) == actualOutput.charAt(cnt2)){
                cnt2++;
            }
        }

        if(cnt1 < actualOutput.length() || cnt2 < actualOutput.length()){
            return Verdict.WA;
        }
        return Verdict.OK;
    }
}
