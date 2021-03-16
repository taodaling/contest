package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.PermutationUtils;
import template.primitve.generated.datastructure.LongArrayList;
import template.primitve.generated.datastructure.LongHashMap;
import template.rand.MultiSetHasher;
import template.rand.SparseMultiSetHasher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class BRestoreCube {
    long[][][] permPts;
    long[] first;
    long[][] sorted = new long[7][];
    boolean[] used = new boolean[7];
    long[] sum = new long[3];

    public boolean dfs(int i, List<long[]> seq) {
        if (i == permPts.length) {
            seq.toArray(sorted);
            Arrays.sort(sorted, Comparator.comparingLong(x -> dist2(first, x)));
            if(dist2(sorted[0], first) == 0){
                return false;
            }
            for (int j = 1; j < 3; j++) {
                if (dist2(sorted[j], first) != dist2(sorted[0], first)) {
                    return false;
                }
                for (int k = 0; k < j; k++) {
                    if (dot(first, sorted[j], sorted[k]) != 0) {
                        return false;
                    }
                }
            }
            Arrays.fill(used, false);
            for (int ui = 0; ui <= 1; ui++) {
                for (int uj = 0; uj <= 1; uj++) {
                    for (int uk = 0; uk <= 1; uk++) {
                        if (ui + uj + uk <= 1) {
                            continue;
                        }
                        Arrays.fill(sum, 0);
                        add(sum, first);
                        if (ui == 1) {
                            add(sum, sorted[0]);
                        }
                        if (uj == 1) {
                            add(sum, sorted[1]);
                        }
                        if (uk == 1) {
                            add(sum, sorted[2]);
                        }
                        boolean find = false;
                        for (int t = 3; t < 7; t++) {
                            if (used[t]) {
                                continue;
                            }
                            if (match(sum, sorted[t])) {
                                find = true;
                                used[t] = true;
                                break;
                            }
                        }
                        if (!find) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }

        for (long[] pt : permPts[i]) {
            seq.add(pt);
            if (dfs(i + 1, seq)) {
                return true;
            }
            seq.remove(seq.size() - 1);
        }
        return false;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long[][] pts = new long[8][3];
        permPts = new long[8][6][];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 3; j++) {
                pts[i][j] = in.ri();
            }
            Arrays.sort(pts[i]);
            int[] perm = IntStream.range(0, 3).toArray();
            int wpos = 0;
            do {
                long[] cast = new long[3];
                for (int j = 0; j < perm.length; j++) {
                    cast[j] = pts[i][perm[j]];
                }
                permPts[i][wpos++] = cast;
            } while (PermutationUtils.nextPermutation(perm));
        }
        first = pts[0];
        List<long[]> seq = new ArrayList<>(8);
        if (!dfs(1, seq)) {
            out.println("NO");
            return;
        }
        out.println("YES");
        seq.add(0, first);
        for (long[] pt : seq) {
            out.append(pt[0]).append(' ').append(pt[1]).append(' ').append(pt[2]).println();
        }

    }

    public void add(long[] sum, long[] a) {
        for (int i = 0; i < 3; i++) {
            sum[i] += a[i];
        }
    }

    public boolean match(long[] a, long[] b) {
        for (int i = 0; i < 3; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    public long dist2(long[] a, long[] b) {
        long ans = 0;
        for (int i = 0; i < 3; i++) {
            long d = a[i] - b[i];
            ans += d * d;
        }
        return ans;
    }

    public long dot(long[] center, long[] a, long[] b) {
        long ans = 0;
        for (int i = 0; i < 3; i++) {
            ans += (a[i] - center[i]) * (b[i] - center[i]);
        }
        return ans;
    }
}
