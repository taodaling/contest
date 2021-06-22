package on2021_06.on2021_06_20_Library_Checker.Division_of_Polynomials;



import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.polynomial.IntPolyNTT;
import template.polynomial.Polynomials;

public class DivisionOfPolynomials {
    int mod = 998244353;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] f = in.ri(n);
        int[] g = in.ri(m);
        IntPolyNTT poly = new IntPolyNTT(mod);
        IntPolyNTT.FastDivision fd = poly.newFastDivision(g, n - 1);
        int[][] ans = fd.divideAndRemainder(f);
        int[] div = ans[0];
        int[] rem = ans[1];
        int rd = Polynomials.rankOf(div);
        int rr = Polynomials.rankOf(rem);
        if (div[rd] == 0) {
            rd--;
        }
        if (rem[rr] == 0) {
            rr--;
        }
        out.append(rd + 1).append(' ').append(rr + 1).println();
        for (int i = 0; i <= rd; i++) {
            out.append(div[i]).append(' ');
        }
        out.println();
        for (int i = 0; i <= rr; i++) {
            out.append(rem[i]).append(' ');
        }
        out.println();
    }
}
