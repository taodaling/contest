package contest;

import template.math.KahanSummation;
import template.utils.SequenceUtils;

public class EllysTournament {
    double[][] g;
    double[][] L;
    double[][] R;
    int[] rating;

    public double winProb(int a, int b){
        return (double)rating[a] / (rating[a] + rating[b]);
    }

    public double g(int l, int r) {
        if (g[l][r] == -1) {
            KahanSummation sum = new KahanSummation();
            for (int i = l; i < r; i++) {
                sum.add(L(l, i) * R(i + 1, r));
            }
            g[l][r] = sum.sum();
        }
        return g[l][r];
    }

    public double R(int l, int r) {
        if (R[l][r] == -1) {
            if (l == r) {
                return R[l][r] = 1;
            }
            KahanSummation sum = new KahanSummation();
            for (int x = l; x < r; x++) {
                sum.add(R(l, x) * g(x, r) * winProb(r, x));
            }
            R[l][r] = sum.sum() / (r - l);
        }
        return R[l][r];
    }

    public double L(int l, int r) {
        if (L[l][r] == -1) {
            if (l == r) {
                return L[l][r] = 1;
            }
            KahanSummation sum = new KahanSummation();
            for (int x = l + 1; x <= r; x++) {
                sum.add(L(x, r) * g(l, x) * winProb(l, x));
            }
            L[l][r] = sum.sum() / (r - l);
        }
        return L[l][r];
    }

    public double getChance(int N, int K, int[] ratings) {
        K--;
        this.rating = ratings;
        g = new double[N][N];
        L = new double[N][N];
        R = new double[N][N];
        SequenceUtils.deepFill(g, -1D);
        SequenceUtils.deepFill(L, -1D);
        SequenceUtils.deepFill(R, -1D);
        double ans = L(K, N - 1) * R(0, K);
        return ans;
    }
}
