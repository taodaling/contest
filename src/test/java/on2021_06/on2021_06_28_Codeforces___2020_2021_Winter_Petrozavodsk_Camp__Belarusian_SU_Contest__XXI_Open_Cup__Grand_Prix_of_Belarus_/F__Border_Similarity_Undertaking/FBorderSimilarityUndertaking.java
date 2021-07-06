package on2021_06.on2021_06_28_Codeforces___2020_2021_Winter_Petrozavodsk_Camp__Belarusian_SU_Contest__XXI_Open_Cup__Grand_Prix_of_Belarus_.F__Border_Similarity_Undertaking;




import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.IndexLayer;

import java.util.Arrays;
import java.util.stream.IntStream;

public class FBorderSimilarityUndertaking {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        A = new char[n][m];
        int L = 2000 + 10;
        B = new char[L][L];
        C = new char[L][L];
        pair1 = new int[L][L];
        pair2 = new int[L][L];
        left = new int[L][L];
        right = new int[L][L];
        ps = new int[L];
        height = new int[L];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                A[i][j] = in.rc();
            }
        }
        dac(0, m - 1, 0, n - 1);
        out.println(ans);
    }

    char[][] A;
    char[][] B;
    char[][] C;
    int[][] pair1;
    int[][] pair2;
    int[] ps;
    long ans;
    int[][] left;
    int[][] right;
    int[] height;

    public void process(char[][] mat, int[][] pair, int h, int w) {
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < w; j++) {
                pair[i][j] = 0;
            }
        }

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                left[i][j] = j;
                if (j > 0 && mat[i][j] == mat[i][j - 1]) {
                    left[i][j] = left[i][j - 1];
                }
            }

            for (int j = w - 1; j >= 0; j--) {
                right[i][j] = j;
                if (j + 1 < w && mat[i][j] == mat[i][j + 1]) {
                    right[i][j] = right[i][j + 1];
                }
            }
        }

        for (int j = 0; j < w; j++) {
            int d = 0;
            while (d + 1 < h && mat[0][j] == mat[d + 1][j]) {
                d++;
            }
            height[j] = d;
        }

        for (int j = 0; j < w; j++) {
            Arrays.fill(ps, 0, w, 0);
            for (int i = 0; i <= height[j]; i++) {
                ps[left[i][j]]++;
                ps[right[i][j] + 1]--;
            }
            for (int i = 0; i < w; i++) {
                if (i > 0) {
                    ps[i] += ps[i - 1];
                }
                if (height[i] > height[j] || height[i] == height[j] && i < j) {
                    pair[j][i] += ps[i];
                }
            }
        }
    }

    IndexLayer layer = new IndexLayer(2);

    public void copy(int l, int r, int b, int t, boolean rotate, boolean flip, char[][] dst) {
        layer.init();

        if (flip) {
            if (rotate) {
                layer.affine(0, -1, r - l);
            } else {
                layer.affine(0, -1, t - b);
            }
        }

        if (rotate) {
            layer.permutation(1, 0);
        }
        for (int i = b; i <= t; i++) {
            for (int j = l; j <= r; j++) {
                layer.indices[0] = i - b;
                layer.indices[1] = j - l;
                layer.transform();
                dst[layer.indices[0]][layer.indices[1]] = A[i][j];
            }
        }
    }

    public void dac(int l, int r, int b, int t) {
        int n = r - l + 1;
        int m = t - b + 1;
        if (n < 2 || m < 2) {
            return;
        }
        int h1, h2, w;
        if (n > m) {
            int mid = (r + l) / 2;
            dac(l, mid, b, t);
            dac(mid + 1, r, b, t);
            copy(l, mid, b, t, true, true, B);
            copy(mid + 1, r, b, t, true, false, C);
            h1 = mid - l + 1;
            h2 = r - (mid + 1) + 1;
            w = t - b + 1;
        } else {
            int mid = (b + t) / 2;
            dac(l, r, b, mid);
            dac(l, r, mid + 1, t);
            copy(l, r, b, mid, false, true, B);
            copy(l, r, mid + 1, t, false, false, C);
            h1 = mid - b + 1;
            h2 = t - (mid + 1) + 1;
            w = r - l + 1;
        }
        process(B, pair1, h1, w);
        process(C, pair2, h2, w);
        long contrib = 0;
        for (int i = 0; i < w; i++) {
            for (int j = i + 1; j < w; j++) {
                if (B[0][i] == B[0][j] && C[0][i] == C[0][j] && B[0][i] == C[0][i]) {
                    long top = pair1[i][j] + pair1[j][i];
                    long bot = pair2[i][j] + pair2[j][i];
                    contrib += top * bot;
                }
            }
        }
        if (contrib != 0) {
            ans += contrib;
        }
    }
}
