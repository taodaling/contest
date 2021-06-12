package on2021_06.on2021_06_11_Luogu.P1520_____;



import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.CyclotomicPolynomialBF;
import template.polynomial.Polynomials;
import template.utils.PrimitiveBuffers;
import template.utils.SortUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class P1520 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        CyclotomicPolynomialBF bf = new CyclotomicPolynomialBF();
        List<P> ps = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            if (n % i != 0) {
                continue;
            }
            ps.add(new P(bf.get(i)));
        }
        ps.sort((a, b) -> {
            if (a.rank != b.rank) {
                return Integer.compare(a.rank, b.rank);
            }
            for (int i = a.rank; i >= 0; i--) {
                if (Math.abs(a.a[i]) != Math.abs(b.a[i])) {
                    return Integer.compare(Math.abs(a.a[i]), Math.abs(b.a[i]));
                }
                if (a.a[i] != b.a[i]) {
                    return Integer.compare(a.a[i], b.a[i]);
                }
            }
            return 0;
        });

        boolean bracket = ps.size() > 1;
        for (P p : ps) {
            if (bracket) {
                out.append("(");
            }
            for (int i = p.rank; i >= 0; i--) {
                if (p.a[i] == 0) {
                    continue;
                }
                if(p.a[i] > 0 && i < p.rank){
                    out.append("+");
                }
                if(Math.abs(p.a[i]) == 1){
                    if(p.a[i] == -1){
                        out.append("-");
                    }
                    if(i > 0){
                        //skip
                    }else{
                        out.append(1);
                    }
                }else {
                    out.append(p.a[i]);
                }
                if(i > 0){
                    out.append("x");
                    if(i > 1){
                        out.append("^").append(i);
                    }
                }
            }
            if (bracket) {
                out.append(")");
            }
        }

        PrimitiveBuffers.check();
    }
}


class P {
    int[] a;
    int rank;

    public P(int[] a) {
        this.a = a;
        rank = Polynomials.rankOf(a);
    }
}