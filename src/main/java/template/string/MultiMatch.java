package template.string;

import graphs.spanningtree.Prim;
import template.polynomial.FastFourierTransform;
import template.utils.PrimitiveBuffers;
import template.utils.SequenceUtils;

import java.util.Arrays;

/**
 * 多模式或多原串匹配，pattern的第i位和source的第j位匹配，当且仅当存在一对a,b，满足第a个pattern的第i位等于第b个source的第j位。
 */
public class MultiMatch {
    private int[][] sumOf(int[][] pattern, boolean matchAll) {
        int p = pattern.length;
        int n = pattern[0].length;
        int[][] A = PrimitiveBuffers.allocIntPow2Array(n, p * 2 + 1);
        int[] accumulate = PrimitiveBuffers.allocIntPow2(n);
        int[][] buf = PrimitiveBuffers.allocIntPow2Array(n, p);
        for (int i = 0; i < n; i++) {
            accumulate[i] = 1;
        }
        if (matchAll) {
            for (int[] pat : pattern) {
                for (int i = 0; i < n; i++) {
                    if (pat[i] == 0) {
                        accumulate[i] = 0;
                    }
                }
            }
        }
        sumOf(pattern, 0, A, 0, accumulate, buf);
        PrimitiveBuffers.release(accumulate);
        PrimitiveBuffers.release(buf);
        return A;
    }

    private void sumOf(int[][] s, int pick, int[][] sum, int id, int[] accumulate, int[][] buf) {
        int len = s[0].length;
        if (id >= s.length) {
            for (int i = 0; i < len; i++) {
                sum[pick][i] += accumulate[i];
            }
            return;
        }
        sumOf(s, pick + 0, sum, id + 1, accumulate, buf);
        int[] data = buf[id];
        for (int j = 0; j < len; j++) {
            data[j] = accumulate[j] * s[id][j] * 2;
        }
        sumOf(s, pick + 1, sum, id + 1, data, buf);
        for (int j = 0; j < len; j++) {
            data[j] = accumulate[j] * s[id][j] * s[id][j];
        }
        sumOf(s, pick + 2, sum, id + 1, data, buf);
    }

    public boolean[] match(int[] pattern, int[][] source, boolean matchAll) {
        int n = pattern.length;
        int s = source.length;
        int m = source[0].length;
        int total = s * 2;
        int[] prodA = PrimitiveBuffers.allocIntPow2(n);
        Arrays.fill(prodA, 1);
        if (matchAll) {
            for (int i = 0; i < n; i++) {
                prodA[i] = pattern[i];
            }
        }
        int[][] Bs = sumOf(source, matchAll);
        double[][] B = PrimitiveBuffers.allocDoublePow2Array(m + n - 1, 2);
        double[][] A = PrimitiveBuffers.allocDoublePow2Array(m + n - 1, 2);
        double[][] sum = PrimitiveBuffers.allocDoublePow2Array(m + n - 1, 2);
        for (int i = 0; i <= total; i++) {
            int j = total - i;
            SequenceUtils.deepFill(B, 0d);
            if (j % 2 == 0) {
                for (int k = 0; k < m; k++) {
                    B[0][k] = Bs[j][k];
                }
            } else {
                for (int k = 0; k < m; k++) {
                    B[0][k] = -Bs[j][k];
                }
            }
            SequenceUtils.deepFill(A, 0d);
            for (int k = 0; k < n; k++) {
                A[0][k] = prodA[k];
            }
            SequenceUtils.reverse(B[0], 0, m - 1);
            FastFourierTransform.fft(B, false);
            FastFourierTransform.fft(A, false);
            double[][] dm = FastFourierTransform.dotMul(A, B);
            for (int t = 0; t < 2; t++) {
                for (int k = 0; k < dm[t].length; k++) {
                    sum[t][k] += dm[t][k];
                }
            }
            for (int k = 0; k < n; k++) {
                prodA[k] *= pattern[k];
            }
            PrimitiveBuffers.release(dm);
        }
        boolean[] possible = new boolean[m];
        FastFourierTransform.fft(sum, true);
        SequenceUtils.reverse(sum[0], 0, m - 1);
        for (int i = 0; i + n <= m; i++) {
            possible[i] = -0.5 < sum[0][i] && sum[0][i] < 0.5;
        }
        PrimitiveBuffers.release(sum);
        PrimitiveBuffers.release(A);
        PrimitiveBuffers.release(B);
        PrimitiveBuffers.release(Bs);
        PrimitiveBuffers.release(prodA);
        return possible;
    }

    public boolean[] match(int[][] pattern, int[] source, boolean matchAll) {
        int n = source.length;
        int s = pattern.length;
        int m = pattern[0].length;
        int total = s * 2;
        int[] prodA = PrimitiveBuffers.allocIntPow2(n);
        Arrays.fill(prodA, 1);
        if (matchAll) {
            for (int i = 0; i < n; i++) {
                prodA[i] = source[i];
            }
        }
        int[][] Bs = sumOf(pattern, matchAll);
        double[][] B = PrimitiveBuffers.allocDoublePow2Array(m + n - 1, 2);
        double[][] A = PrimitiveBuffers.allocDoublePow2Array(m + n - 1, 2);
        double[][] sum = PrimitiveBuffers.allocDoublePow2Array(m + n - 1, 2);
        for (int i = 0; i <= total; i++) {
            int j = total - i;
            SequenceUtils.deepFill(A, 0d);
            SequenceUtils.deepFill(B, 0d);
            if (j % 2 == 0) {
                for (int k = 0; k < m; k++) {
                    B[0][k] = Bs[j][k];
                }
            } else {
                for (int k = 0; k < m; k++) {
                    B[0][k] = -Bs[j][k];
                }
            }
            for (int k = 0; k < n; k++) {
                A[0][k] = prodA[k];
            }
            SequenceUtils.reverse(A[0], 0, n - 1);
            FastFourierTransform.fft(A, false);
            FastFourierTransform.fft(B, false);
            double[][] dm = FastFourierTransform.dotMul(A, B);
            for (int t = 0; t < 2; t++) {
                for (int k = 0; k < dm[t].length; k++) {
                    sum[t][k] += dm[t][k];
                }
            }
            for (int k = 0; k < n; k++) {
                prodA[k] *= source[k];
            }
            PrimitiveBuffers.release(dm);
        }
        FastFourierTransform.fft(sum, true);
        SequenceUtils.reverse(sum[0], 0, n - 1);
        boolean[] possible = new boolean[n];
        for (int i = 0; i + m <= n; i++) {
            possible[i] = -0.5 < sum[0][i] && sum[0][i] < 0.5;
        }
        PrimitiveBuffers.release(sum);
        PrimitiveBuffers.release(A);
        PrimitiveBuffers.release(B);
        PrimitiveBuffers.release(Bs);
        PrimitiveBuffers.release(prodA);
        return possible;
    }
}
