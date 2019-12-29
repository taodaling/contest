package on2019_12.on2019_12_28_AtCoder_Grand_Contest_041.C___Domino_Quality;



import net.egork.chelper.tester.StringInputStream;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.checkers.Checker;
import template.io.FastInput;

public class TaskCChecker implements Checker {
    public TaskCChecker(String parameters) {
    }

    public Verdict check(String input, String expectedOutput, String actualOutput) {
        FastInput fi = new FastInput(new StringInputStream(input));
        FastInput ao = new FastInput(new StringInputStream(actualOutput));
        int n = fi.readInt();
        if(n == 2){
            return ao.readInt() == -1 ? Verdict.OK : Verdict.WA;
        }

        char[][] data = new char[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                data[i][j] = ao.readChar();
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (data[i][j] == '.') {
                    continue;
                }
                int cnt = 0;
                if (i > 0 && data[i - 1][j] == data[i][j]) {
                    cnt++;
                }
                if (i + 1 < n && data[i + 1][j] == data[i][j]) {
                    cnt++;
                }
                if (j > 0 && data[i][j - 1] == data[i][j]) {
                    cnt++;
                }
                if (j + 1 < n && data[i][j + 1] == data[i][j]) {
                    cnt++;
                }

                if (cnt != 1) {
                    return Verdict.WA;
                }
            }
        }

        int total = 0;
        for (int i = 0; i < n; i++) {
            if(data[i][0] != '.') {
                if (i == 0 || data[i][0] != data[i - 1][0]) {
                    total++;
                }
            }
        }

        for (int i = 0; i < n; i++) {
            int r = 0;
            int c = 0;
            for (int j = 0; j < n; j++) {
                if (data[i][j] != '.') {
                    if (j == 0 || data[i][j] != data[i][j - 1]) {
                        r++;
                    }
                }
                if (data[j][i] != '.') {
                    if (j == 0 || data[j][i] != data[j - 1][i]) {
                        c++;
                    }
                }
            }
            if (r != total || c != total) {
                return Verdict.WA;
            }
        }

        return Verdict.OK;
    }
}
