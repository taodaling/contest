package on2020_05.on2020_05_08_TopCoder_SRM__750.PurpleSubsequences;



import template.math.LongRadix;
import template.primitve.generated.datastructure.LongHashMap;
import template.primitve.generated.datastructure.LongList;

import java.util.*;

public class PurpleSubsequences {

    //Debug debug = new Debug(true);

    public long count(int[] A, int L) {
        int l = 1;
        int r = 20;
        for (int i = 0; i < L; i++) {
            gen(l, r, i, 0, 0);
        }
        long end = 0;
        for (int i = 0; i < L; i++) {
            end = radix.set(end, i, 1);
        }
        collect.add(end);
       // debug.debug("collect", collect);
        LongHashMap map = new LongHashMap(collect.size(), false);
        long[] state = collect.toArray();
        for (int i = 0; i < state.length; i++) {
            map.put(state[i], i);
        }

        int[][] transfer = new int[state.length][r + 1];
        for (int i = 0; i < state.length; i++) {
            long v = state[i];
            int lastBit = 0;
            for (int j = l; j <= r; j++) {
                long bit;
                while ((bit = radix.get(v, lastBit)) != 0 && bit < j) {
                    lastBit++;
                }
                long next = v;
                if (bit > j || bit == 0) {
                    next = radix.set(v, lastBit, j);
                }
                transfer[i][j] = (int) map.getOrDefault(next, state.length - 1);
            }
        }


        //debug.debug("transfer", transfer);
        long[][] dp = new long[A.length + 1][state.length];
        int[][] next = new int[A.length + 1][r + 1];
        for (int i = A.length; i >= 0; i--) {
            if (i == A.length) {
                Arrays.fill(next[i], A.length + 1);
            } else {
                System.arraycopy(next[i + 1], 0, next[i], 0, next[i].length);
                next[i][A[i]] = i + 1;
            }
        }

       // debug.debug("next", next);

        dp[0][0] = 1;
        for (int i = 0; i <= A.length; i++) {
            for (int j = 0; j < state.length; j++) {
                for (int k = l; k <= r; k++) {
                    if (next[i][k] > A.length) {
                        continue;
                    }
//                    if(next[i][k] == i){
//                        throw new RuntimeException();
//                    }
                    dp[next[i][k]][transfer[j][k]] += dp[i][j];
                }
            }
        }

//        debug.debug("dp", dp);
//        debug.debug("transfer", transfer);

        long ans = 0;
        for (int i = 0; i <= A.length; i++) {
            ans += dp[i][state.length - 1];
        }

//        Set<List<Integer>> bf = new HashSet<>();
//        dfs(A, 0, L, new ArrayList<>(), bf);
//        debug.debug("bf", bf);
//        debug.debug("bf.size()", bf.size());
//        int[] cnt = new int[A.length];
//        for (List<Integer> list : bf) {
//            cnt[match(A, list)]++;
//        }
//        debug.debug("cnt", cnt);

        return ans;
    }

    LongRadix radix = new LongRadix(100);
    LongList collect = new LongList(30000);

    public void gen(int l, int r, int L, int i, long val) {
        if (i == L) {
            collect.add(val);
            return;
        }
        for (int j = l; j <= r; j++) {
            gen(j + 1, r, L, i + 1, radix.set(val, i, j));
        }
    }

    public int lcs(List<Integer> list) {
        if (list.isEmpty()) {
            return 0;
        }
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (int x : list) {
            Map.Entry<Integer, Integer> floor = map.floorEntry(x - 1);
            int depth = 1;
            if (floor != null) {
                depth = floor.getValue() + 1;
            }
            while (!map.isEmpty()) {
                Map.Entry<Integer, Integer> ceil = map.ceilingEntry(x);
                if (ceil != null && ceil.getValue() <= depth) {
                    map.remove(ceil.getKey());
                } else {
                    break;
                }
            }

            map.put(x, depth);
        }
        return map.lastEntry().getValue();
    }

    public void dfs(int[] A, int i, int limit, List<Integer> list, Set<List<Integer>> ans) {
        if (i == A.length) {
            if (lcs(list) >= limit) {
                ans.add(new ArrayList<>(list));
            }
            return;
        }
        dfs(A, i + 1, limit, list, ans);
        list.add(A[i]);
        dfs(A, i + 1, limit, list, ans);
        list.remove(list.size() - 1);
    }

    public int match(int[] A, List<Integer> list) {
        int i = 0;
        int j = 0;
        while (j < list.size()) {
            if (A[i] == list.get(j)) {
                j++;
                i++;
                continue;
            }
            i++;
        }

        return i - 1;
    }
}

