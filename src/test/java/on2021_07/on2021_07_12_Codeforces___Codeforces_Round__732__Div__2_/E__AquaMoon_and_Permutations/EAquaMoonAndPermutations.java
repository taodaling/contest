package on2021_07.on2021_07_12_Codeforces___Codeforces_Round__732__Div__2_.E__AquaMoon_and_Permutations;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EAquaMoonAndPermutations {
    int mod = 998244353;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        List<int[]> ps = new ArrayList<>(n * 2);
        for (int i = 0; i < n * 2; i++) {
            int[] p = new int[n + 1];
            for (int j = 0; j < n; j++) {
                p[j] = in.ri();
            }
            p[n] = i;
            ps.add(p);
        }
        List<Integer> res = new ArrayList<>(n);
        int[][] cnts = new int[n + 1][n + 1];
        long ans = 1;
        while (!ps.isEmpty()) {
            SequenceUtils.fill(cnts, 0);
            for (int[] p : ps) {
                for (int i = 0; i < n; i++) {
                    cnts[i][p[i]]++;
                }
            }
            int col = -1;
            int v = -1;
            for (int i = 0; i < n; i++) {
                for (int j = 1; j <= n; j++) {
                    if (cnts[i][j] == 1) {
                        col = i;
                        v = j;
                    }
                }
            }
            int[] pick = null;
            if (col != -1) {
                for (int i = 0; i < ps.size(); i++) {
                    int[] p = ps.get(i);
                    if (p[col] == v) {
                        //find
                        pick = p;
                    }
                }
            } else {
                pick = ps.get(0);
                ans = ans * 2 % mod;
            }
            assert pick != null;
            res.add(pick[n]);
            int[] finalPick = pick;
            ps = ps.stream().filter(x -> {
                for (int i = 0; i < n; i++) {
                    if (x[i] == finalPick[i]) {
                        return false;
                    }
                }
                return true;
            }).collect(Collectors.toList());
        }

        res.sort(Comparator.naturalOrder());
        out.println(ans);
        for(int x : res){
            out.append(x + 1).append(' ');
        }
        out.println();
    }
}