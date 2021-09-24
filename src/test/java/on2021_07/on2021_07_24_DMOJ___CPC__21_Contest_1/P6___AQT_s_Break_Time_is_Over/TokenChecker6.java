package on2021_07.on2021_07_24_DMOJ___CPC__21_Contest_1.P6___AQT_s_Break_Time_is_Over;



import chelper.AbstractChecker;
import net.egork.chelper.tester.Verdict;
import template.io.FastInput;

import java.util.Arrays;

public class TokenChecker6 extends AbstractChecker {
    public TokenChecker6(String parameters) {
        super(parameters);
    }

    @Override
    public Verdict check(FastInput stdin, FastInput expect, FastInput actual) {
        int n = stdin.ri();
        int[][] a = new int[n][];
        for (int i = 0; i < n; i++) {
            a[i] = stdin.ri(3);
        }
        int[] exp = expect.ri(3);
        int[] act = exp;//actual.ri(3);
        if (Arrays.stream(exp).sum() != Arrays.stream(act).sum()) {
            return Verdict.WA;
        }
        for (int[] pt : a) {
            if (pt[0] > act[0] && pt[1] > act[1] && pt[2] > act[2]) {
                return Verdict.WA;
            }
        }
        return Verdict.OK;
    }
}