package on2020_02.on2020_02_26_Codeforces_Round__492__Div__1___Thanks__uDebug__.A__Tesla;



import net.egork.chelper.tester.StringInputStream;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.checkers.Checker;
import template.io.FastInput;

import java.util.Arrays;

public class TokenCheckerA implements Checker {
    public TokenCheckerA(String parameters) {

    }

    public Verdict check(String input, String expectedOutput, String actualOutput) {
        FastInput in = new FastInput(new StringInputStream(input));
        FastInput ai = new FastInput(new StringInputStream(actualOutput));

        int n = in.readInt();
        int k = in.readInt();
        int[][] cars = new int[k + 1][5];
        int[][] mat = new int[5][n + 1];

        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= n; j++) {
                mat[i][j] = in.readInt();
                if (mat[i][j] == 0) {
                    continue;
                }
                if (i == 1 || i == 4) {
                    cars[mat[i][j]][3] = i;
                    cars[mat[i][j]][4] = j;
                } else {
                    cars[mat[i][j]][1] = i;
                    cars[mat[i][j]][2] = j;
                }
            }
        }
        Arrays.fill(mat[1], 0);
        Arrays.fill(mat[4], 0);

        int m = ai.readInt();
        if (m == -1) {
            return Verdict.UNDECIDED;
        }

        for (int i = 0; i < m; i++) {
            int c = ai.readInt();
            int x = ai.readInt();
            int y = ai.readInt();
            if (x <= 0 || x > 4 || y <= 0 || y > n) {
                return Verdict.WA;
            }
            if (Math.abs(x - cars[c][1]) + Math.abs(y - cars[c][2]) != 1 ||
                    mat[x][y] != 0) {
                return Verdict.WA;
            }
            mat[x][y] = c;
            mat[cars[c][1]][cars[c][2]] = 0;
            cars[c][1] = x;
            cars[c][2] = y;
        }

        for(int i = 1; i <= k; i++){
            if(cars[i][1] != cars[i][3] || cars[i][2] != cars[i][4]){
                return Verdict.WA;
            }
        }
        return Verdict.OK;
    }
}
