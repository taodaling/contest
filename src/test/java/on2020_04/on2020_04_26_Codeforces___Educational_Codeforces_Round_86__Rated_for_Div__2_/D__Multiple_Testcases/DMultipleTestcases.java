package on2020_04.on2020_04_26_Codeforces___Educational_Codeforces_Round_86__Rated_for_Div__2_.D__Multiple_Testcases;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class DMultipleTestcases {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = in.readInt();
        }
        int[] c = new int[k + 1];
        for (int i = 1; i <= k; i++) {
            c[i] = in.readInt();
        }
        List<Threshold> thresholds = new ArrayList<>(k);
        for (int i = 1; i <= k; i++) {
            int r = i;
            while (r + 1 <= k && c[i] == c[r + 1]) {
                r++;
            }
            thresholds.add(new Threshold(r, c[i]));
            i = r;
        }

        for(int i = 0; i < thresholds.size() - 1; i++){
            thresholds.get(i).add -= thresholds.get(i + 1).add;
        }

        TreeMap<Integer, Integer> cntMap = new TreeMap<>();
        for (int x : m) {
            modify(cntMap, x, 1);
        }

        List<List<Integer>> ansList = new ArrayList<>(n);
        while (!cntMap.isEmpty()) {
            int chance = 0;
            int up;
            List<Integer> used = new ArrayList<>();
            for (int i = thresholds.size() - 1; i >= 0 && chance == 0; i--) {
                Threshold t = thresholds.get(i);
                chance += t.add;
                up = t.t;

                while (chance > 0) {
                    Integer floor = cntMap.floorKey(up);
                    if (floor == null) {
                        break;
                    }
                    chance--;
                    used.add(floor);
                    modify(cntMap, floor, -1);
                }
            }
            ansList.add(used);
        }

        out.println(ansList.size());
        for (List<Integer> ans : ansList) {
            out.append(ans.size()).append(' ');
            for (int x : ans) {
                out.append(x).append(' ');
            }
            out.println();
        }
    }

    public void modify(TreeMap<Integer, Integer> map, Integer key, int mod) {
        int val = map.getOrDefault(key, 0) + mod;
        if (val == 0) {
            map.remove(key);
        } else {
            map.put(key, val);
        }
    }
}

class Threshold {
    int t;
    int add;

    public Threshold(int t, int add) {
        this.t = t;
        this.add = add;
    }
}
