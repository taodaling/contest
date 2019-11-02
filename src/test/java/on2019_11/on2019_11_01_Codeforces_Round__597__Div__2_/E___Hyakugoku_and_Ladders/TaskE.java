package on2019_11.on2019_11_01_Codeforces_Round__597__Div__2_.E___Hyakugoku_and_Ladders;



import template.FastInput;
import template.FastOutput;
import template.SequenceUtils;

public class TaskE {
    int[][] boards = new int[10][10];
    double[][][] exp = new double[10][10][2];

    public void solve(int testNumber, FastInput in, FastOutput out) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                boards[i][j] = in.readInt();
            }
        }

        SequenceUtils.deepFill(exp, -1D);
        exp[0][0][0] = 0;
        double ans = exp(9, 0, 0);
        out.printf("%.10f", ans);
    }

    public double exp(int i, int j, int t) {
        if (exp[i][j][t] < 0) {
            exp[i][j][t] = 0;
            if (t == 1 && boards[i][j] == 0) {
                return exp[i][j][t] = 1e50;
            }

            if (t == 0) {
                double wayExp = 0;
                double stayProb = 0;
                for (int k = 1; k <= 6; k++) {
                    int dir = i % 2 == 1 ? 1 : -1;
                    int nj = j + dir * k;
                    int ni = i;
                    if (nj < 0 || nj >= 10) {
                        if (i == 0) {
                            stayProb += 1.0D / 6;
                            continue;
                        } else {
                            ni--;
                            if (nj < 0) {
                                nj = -nj - 1;
                            } else {
                                nj = 9 - (nj - 10);
                            }
                        }
                    }
                    wayExp += (Math.min(exp(ni, nj, 0), exp(ni, nj, 1)) + 1) / 6;
                }

                exp[i][j][t] = (wayExp + stayProb) / (1 - stayProb);
            } else {
                exp[i][j][t] = exp(i - boards[i][j], j, 0);
            }
        }

        return exp[i][j][t];
    }
}
