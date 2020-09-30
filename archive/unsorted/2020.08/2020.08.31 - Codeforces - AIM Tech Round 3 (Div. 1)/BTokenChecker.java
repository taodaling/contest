package contest;

import chelper.AbstractChecker;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.checkers.Checker;
import template.io.FastInput;

import java.util.Arrays;

public class BTokenChecker extends AbstractChecker {
    public BTokenChecker(String parameters) {
        super(parameters);
    }

    public int[] count(String s) {
        int[] pre = new int[2];
        int[] ans = new int[4];
        for (char c : s.toCharArray()) {
            if (c == '0') {
                ans[0] += pre[0];
                ans[2] += pre[1];
            } else {
                ans[1] += pre[0];
                ans[3] += pre[1];
            }
            pre[c - '0']++;
        }
        return ans;
    }

    @Override
    public Verdict check(FastInput stdin, FastInput expect, FastInput actual) {
        int[] cnts = new int[4];
        stdin.populate(cnts);

        String resp = actual.readString();
        if(resp.equals("Impossible")){
            return Verdict.WA;
        }

        return Arrays.equals(cnts, count(resp)) ? Verdict.OK : Verdict.WA;
    }
}
