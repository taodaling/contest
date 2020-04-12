package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.GuassianElimination;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.Debug;

public class SidedDice {
    Debug debug = new Debug(true);
    double prec = 1e-9;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[][] mat = new int[3][3];
        int sum = 0;
        for (int i = 0; i < 3; i++) {
            mat[0][i] = in.readInt();
            sum += mat[0][i];
        }
        if (sum == 0) {
            throw new UnknownError();
        }

        for (int i = 1; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mat[i][j] = in.readInt();
            }
        }

        int[] want = new int[3];
        for (int i = 0; i < 3; i++) {
            want[i] = in.readInt();
        }

        GuassianElimination ge = new GuassianElimination(5, 3, prec);
        prepare(ge, mat, want, -1);
        //debug.debug("ge", ge);
        if(!ge.solve()){
            //debug.debug("ge", ge);
            out.println("NO");
            return;
        }
        int rank = ge.getRank();
        boolean[] independent = ge.getIndependent();

        if (rank == 3) {
            double[] solution = ge.getSolutions();
            if (check(solution)) {
                debug.debug("sol", solution);
                out.println("YES");
            } else {
                out.println("NO");
            }
            return;
        }
        if (rank == 1) {
            debug.debug("sol", "1 1 9998");
            out.println("YES");
            return;
        }

        IntegerList permList = new IntegerList(3);
        for (int i = 0; i < 3; i++) {
            if (independent[i]) {
                permList.add(i);
            }
        }
        for (int i = 0; i < 3; i++) {
            if (!independent[i]) {
                permList.add(i);
            }
        }
        int[] perm = permList.toArray();
        mat = apply(mat, perm);
        //want = apply(want, perm);
       // debug.debug("mat", mat);
        //debug.debug("want", want);
        for(int i = 0; i <= 10000; i++){
            prepare(ge, mat, want, i);
            ge.solve();
         //   debug.debug("ge", ge);
            if(check(ge.getSolutions())){
                debug.debug("sol", ge.getSolutions());
                out.println("YES");
                return;
            }
        }
        out.println("NO");
    }

    public int[][] apply(int[][] x, int[] perm) {
        int n = x.length;
        int[][] ans = new int[n][];
        for (int i = 0; i < n; i++) {
            ans[perm[i]] = x[i];
        }
        return ans;
    }

    public boolean check(double[] sol) {
        boolean ans = true;
        for (int i = 0; i < 3; i++) {
            ans = ans && check(sol[i]);
        }
        return ans;
    }

    public boolean check(double x) {
        long round = DigitUtils.round(x);
        return Math.abs(x - round) <= prec && round >= 1;
    }

    public void prepare(GuassianElimination ge, int[][] mat, int[] want, int c) {
        ge.clear(5, 3);
        double base = 1e4;
        double[][] coes = ge.getMat();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                coes[i][j] = mat[j][i] / base;
            }
            ge.setRight(i, want[i]);
        }
        coes[3][0] = coes[3][1] = coes[3][2] = 1;
        ge.setRight(3, 10000);

        if (c != -1) {
            coes[4][2] = 1;
            ge.setRight(4, c);
        }
    }
}