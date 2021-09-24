package on2021_07.on2021_07_23_Codeforces___Harbour_Space_Scholarship_Contest_2021_2022__open_for_everyone__rated__Div__1___Div__2_.I__Stairs;



import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Factorial;
import template.math.Power;
import template.math.PrimitiveRoot;
import template.polynomial.NumberTheoryTransform;
import template.polynomial.Polynomials;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;
import template.utils.PrimitiveBuffers;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IStairs {

    int mod = 998244353;
    int g = PrimitiveRoot.findAnyRoot(mod);
    Debug debug = new Debug(true);
    int maxlog = 18;
    int[][] dftx1 = new int[maxlog][];
    int[][] dft2x1 = new int[maxlog][];
    Power power = new Power(mod);
    Factorial fact = new Factorial((int) 1e5, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        IntegerArrayList lengthList = new IntegerArrayList(n);
        for (int i = 0; i < n; i++) {
            int l = i;
            int r = i + a[i] - 1;
            for (int j = l; j <= r; j++) {
                if (a[j] != a[l]) {
                    out.println(0);
                    return;
                }
            }
            lengthList.add(r - l + 1);
            i = r;
        }
        debug.elapse("init");
        int[] b = lengthList.toArray();
        int block = 200;
        List<int[][][]> allDpState = new ArrayList<>(n);
        for (int i = 0; i < b.length; i++) {
            int l = i;
            int r = l + block - 1;
            r = Math.min(r, b.length - 1);
            if (r + block >= b.length) {
                r = b.length - 1;
            }
            i = r;
            allDpState.add(solve(b, l, r));
        }
        debug.elapse("block");
        int[][][][] dps = allDpState.toArray(new int[0][][][]);
        for (int i = 1; i < maxlog; i++) {
            dftx1[i] = new int[1 << i];
            dftx1[i][0] = 1;
            dftx1[i][1] = 1;
            dft2x1[i] = new int[1 << i];
            dft2x1[i][0] = 1;
            dft2x1[i][1] = 2;
            NumberTheoryTransform.ntt(dftx1[i], false, mod, g, power);
            NumberTheoryTransform.ntt(dft2x1[i], false, mod, g, power);
        }
        debug.elapse("ntt");
        int[][][] mergedDp = dac(dps, 0, dps.length - 1);
//        debug.debug("mergedDp", mergedDp);
        debug.elapse("dac");
        long ans = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < mergedDp[i][j].length; k++) {
                    //violate k
                    long contrib = mergedDp[i][j][k];
                    if (contrib == 0) {
                        continue;
                    }
                    contrib = contrib * fact.fact(b.length - k) % mod;
                    if (k % 2 == 1) {
                        contrib = -contrib;
                    }
                    ans += contrib;
                }
            }
        }
        debug.elapse("solve");
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
        debug.summary();
    }

    public int[][][] solve(int[] a, int l, int r) {
        int max = r - l;
        long[][][] prev = new long[3][3][max + 1];
        long[][][] next = new long[3][3][max + 1];
        if (a[l] == 1) {
            prev[2][2][0] = 1;
        } else {
            prev[0][0][0] = prev[1][1][0] = 1;
        }
        for (int i = l + 1; i <= r; i++) {
            SequenceUtils.deepFill(next, 0L);
            int x = a[i];
            for (int b = 0; b < 3; b++) {
                for (int e = 0; e < 3; e++) {
                    for (int c = 0; c <= max; c++) {
                        long way = prev[b][e][c];
                        if (way == 0) {
                            continue;
                        }
                        //don't bind
                        if (x == 1) {
                            next[b][2][c] += way;
                        } else {
                            next[b][0][c] += way;
                            next[b][1][c] += way;
                        }
                        //bind
                        if (e == 2) {
                            if (i == l + 1) {
                                //fuck
                                next[0][0][c + 1] += way;
                                next[1][1][c + 1] += way;
                            } else {
                                next[b][0][c + 1] += way;
                                next[b][1][c + 1] += way;
                            }
                        } else {
                            next[b][e][c + 1] += way;
                        }
                    }
                }
            }
            for (int b = 0; b < 3; b++) {
                for (int e = 0; e < 3; e++) {
                    for (int c = 0; c <= max; c++) {
                        next[b][e][c] %= mod;
                    }
                }
            }

            long[][][] tmp = prev;
            prev = next;
            next = tmp;
        }

        int[][][] ans = new int[3][3][];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                ans[i][j] = PrimitiveBuffers.allocIntPow2(max + 1);
                for (int k = 0; k <= max; k++) {
                    ans[i][j][k] = DigitUtils.mod(prev[i][j][k], mod);
                }
            }
        }
        return ans;
    }

    public int maxRank(int[][][] a) {
        int ans = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                ans = Math.max(ans, Polynomials.rankOf(a[i][j]));
            }
        }
        return ans;
    }

    public int[][][] dac(int[][][][] a, int l, int r) {
        if (l == r) {
            return a[l];
        }
        int m = (l + r) / 2;
        int[][][] left = dac(a, l, m);
        int[][][] right = dac(a, m + 1, r);
        int max = maxRank(left) + maxRank(right) + 1;
//        debug.debug("left", left);
//        debug.debug("right", right);
        int[][][] leftDft = new int[3][3][];
        int[][][] rightDft = new int[3][3][];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                leftDft[i][j] = PrimitiveBuffers.allocIntPow2(left[i][j], max + 1);
                rightDft[i][j] = PrimitiveBuffers.allocIntPow2(right[i][j], max + 1);
                NumberTheoryTransform.ntt(leftDft[i][j], false, mod, g, power);
                NumberTheoryTransform.ntt(rightDft[i][j], false, mod, g, power);
            }
        }
        int[] dotBuf = PrimitiveBuffers.allocIntPow2(max + 1);
        int[] sumBuf = PrimitiveBuffers.allocIntPow2(max + 1);
        int log2 = Log2.floorLog(dotBuf.length);

        int[][][] ans = new int[3][3][];
        for (int head = 0; head < 3; head++) {
            for (int tail = 0; tail < 3; tail++) {
                Arrays.fill(sumBuf, 0);
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        Polynomials.dotmul(leftDft[head][i], rightDft[j][tail], dotBuf, mod);
                        boolean same = i == j;
                        if (i == 2 || j == 2) {
                            same = true;
                            if (i == 2 && j == 2) {
                                same = false;
                                Polynomials.dotmul(dotBuf, dft2x1[log2], dotBuf, mod);
                            }
                        }
                        if (same) {
                            Polynomials.dotmul(dotBuf, dftx1[log2], dotBuf, mod);
                        }
                        int finalHead = head;
                        int finalTail = tail;
                        int finalI = i;
                        int finalJ = j;
//                        debug.run(() -> {
//                            debug.debugArray("state", new int[]{finalHead, finalTail, finalI, finalJ});
//                            int[] data = dotBuf.clone();
//                            NumberTheoryTransform.ntt(data, true, mod, g, power);
//                            debug.debugArray("dotBuf", data);
//                        });
                        for (int k = 0; k < sumBuf.length; k++) {
                            sumBuf[k] += dotBuf[k];
                            if (sumBuf[k] >= mod) {
                                sumBuf[k] -= mod;
                            }
                        }
                    }
                }

                NumberTheoryTransform.ntt(sumBuf, true, mod, g, power);
                ans[head][tail] = PrimitiveBuffers.allocIntPow2(sumBuf);
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                PrimitiveBuffers.release(left[i][j], leftDft[i][j], right[i][j], rightDft[i][j]);
            }
        }
        PrimitiveBuffers.release(dotBuf, sumBuf);
        return ans;
    }
}

