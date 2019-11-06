package contest;

import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.Set;

import net.egork.chelper.checkers.Checker;
import net.egork.chelper.tester.Verdict;
import template.FastInput;
import template.IntList;

public class TaskFChecker implements Checker {
    public TaskFChecker(String parameters) {}

    public Verdict check(String input, String expectedOutput, String actualOutput) {
        FastInput in = new FastInput(new ByteArrayInputStream(actualOutput.getBytes()));

        int n = in.readInt();
        int k = in.readInt();

        if (!(n >= 1000 && n <= 2000 && k >= 1)) {
            return Verdict.WA;
        }

        IntList[] cnts = new IntList[n + 1];
        for (int i = 1; i <= n; i++) {
            cnts[i] = new IntList();
        }
        Set<Integer>[] sets = new Set[n + 1];
        int[][] intersection = new int[n + 1][n + 1];

        for (int i = 1; i <= n; i++) {
            sets[i] = new HashSet<>(k);
            for (int j = 0; j < k; j++) {
                int v = in.readInt();
                if (v <= 0 || v > n) {
                    return Verdict.WA;
                }
                for (int t = 0; t < cnts[v].size(); t++) {
                    intersection[cnts[v].get(t)][i]++;
                    intersection[i][cnts[v].get(t)]++;
                }
                cnts[v].add(i);
                sets[i].add(v);
            }
        }

        for (int i = 1; i <= n; i++) {
            if (cnts[i].size() != k || sets[i].size() != k) {
                return Verdict.WA;
            }
        }



        for (int i = 1; i <= n; i++) {
            for (int j = i + 1; j <= n; j++) {
                if (intersection[i][j] != 1) {
                    return Verdict.WA;
                }
            }
        }

        return Verdict.OK;
    }
}
