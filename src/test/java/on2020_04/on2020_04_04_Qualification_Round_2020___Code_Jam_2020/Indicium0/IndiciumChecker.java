package on2020_04.on2020_04_04_Qualification_Round_2020___Code_Jam_2020.Indicium0;





import net.egork.chelper.tester.StringInputStream;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.checkers.Checker;
import template.io.FastInput;

import java.util.HashSet;
import java.util.Set;

public class IndiciumChecker implements Checker {
    public IndiciumChecker(String parameters) {
    }

    public Verdict check(String input, String expectedOutput, String actualOutput) {
        FastInput stdin = new FastInput(new StringInputStream(input));
        FastInput sol = new FastInput(new StringInputStream(actualOutput.substring("Case #1: ".length())));
        FastInput exp = new FastInput(new StringInputStream(expectedOutput));
        stdin.readInt();
        int n = stdin.readInt();
        int k = stdin.readInt();
        int[][] mat = new int[n][n];
        String possible = sol.readString();
        if (possible.equals("IMPOSSIBLE")) {
            return "IMPOSSIBLE".equals(exp.readString()) ? Verdict.UNDECIDED : Verdict.WA;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = sol.readInt();
            }
        }
        int sum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] <= 0 || mat[i][j] > n) {
                    return Verdict.WA;
                }
            }
        }
        for (int i = 0; i < n; i++) {
            sum += mat[i][i];
        }

        if (sum != k) {
            return Verdict.WA;
        }

        for (int i = 0; i < n; i++) {
            Set<Integer> set = new HashSet<>();
            for (int j = 0; j < n; j++) {
                if (set.contains(mat[i][j])) {
                    return Verdict.WA;
                }
                set.add(mat[i][j]);
            }
        }

        for (int i = 0; i < n; i++) {
            Set<Integer> set = new HashSet<>();
            for (int j = 0; j < n; j++) {
                if (set.contains(mat[j][i])) {
                    return Verdict.WA;
                }
                set.add(mat[j][i]);
            }
        }

        return Verdict.OK;
    }
}
