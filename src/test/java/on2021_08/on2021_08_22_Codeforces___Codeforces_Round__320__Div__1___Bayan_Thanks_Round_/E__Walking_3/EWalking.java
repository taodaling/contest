package on2021_08.on2021_08_22_Codeforces___Codeforces_Round__320__Div__1___Bayan_Thanks_Round_.E__Walking_3;



import template.datastructure.RangeTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EWalking {
    int[] next;
    int[] prev;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.rs().toCharArray();
        int n = s.length;
        next = new int[n];
        prev = new int[n];
        Arrays.fill(next, -1);
        Arrays.fill(prev, -1);
        for (int i = 0; i < n; i++) {
            s[i] = (char) (s[i] == 'R' ? 1 : 0);
        }

        List<int[]> path = new ArrayList<>(n);
        RangeTree[] rt = new RangeTree[2];
        for (int i = 0; i < 2; i++) {
            rt[i] = new RangeTree(n);
        }
        for (int i = 0; i < n; i++) {
            rt[s[i]].add(i);
        }

        while (rt[0].size() + rt[1].size() > 0) {
            int cur = rt[0].first();
            int type = 0;
            int cand = rt[1].first();
            if (cand != -1 && (cand < cur || cur == -1)) {
                cur = cand;
                type = 1;
            }
            rt[type].remove(cur);
            while (true) {
                type ^= 1;
                int go = rt[type].ceil(cur);
                if (go == -1) {
                    break;
                }
                rt[type].remove(go);
                prev[go] = cur;
                next[cur] = go;
                cur = go;
            }
        }

        for (int i = 0; i < n; i++) {
            if (prev[i] != -1) {
                continue;
            }
            int end = i;
            while (next[end] != -1) {
                end = next[end];
            }
            path.add(new int[]{i, end});
        }

        List<int[]>[][] type = new List[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                type[i][j] = new ArrayList<>(n);
            }
        }
        for (int[] p : path) {
            type[s[p[0]]][s[p[1]]].add(p);
        }
        while (type[0][0].size() > 0 && type[1][1].size() > 0) {
            int[] t1 = CollectionUtils.pop(type[0][0]);
            int[] t2 = CollectionUtils.pop(type[1][1]);
            int[] merge = concat(t1, t2);
            type[0][1].add(merge);
        }
        while (type[0][1].size() >= 2) {
            int[] t1 = CollectionUtils.pop(type[0][1]);
            int[] t2 = CollectionUtils.pop(type[0][1]);
            type[0][1].add(concat(t1, t2));
        }
        while (type[1][0].size() >= 2) {
            int[] t1 = CollectionUtils.pop(type[1][0]);
            int[] t2 = CollectionUtils.pop(type[1][0]);
            type[1][0].add(concat(t1, t2));
        }
        if (type[0][1].size() > 0 && type[1][0].size() > 0) {
            int[] t1 = CollectionUtils.pop(type[0][1]);
            int[] t2 = CollectionUtils.pop(type[1][0]);
            if (t1[1] < t2[1]) {
                int[] end = detach(t2);
                type[0][1].add(concat(concat(t1, end), t2));
            } else {
                int[] end = detach(t1);
                type[1][0].add(concat(concat(t2, end), t1));
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (type[i][i ^ 1].size() > 0 && type[j][j].size() > 0) {
                    int[] t1 = CollectionUtils.pop(type[i][i ^ 1]);
                    int[] t2 = CollectionUtils.pop(type[j][j]);
                    int[] end;
                    if (i != j) {
                        end = concat(t2, t1);
                    } else {
                        end = concat(t1, t2);
                    }
                }
            }
        }

        out.println(path.size() - 1);
        for (int i = 0; i < n; i++) {
            if (prev[i] != -1) {
                continue;
            }
            int end = i;
            while (end != -1) {
                out.append(end + 1).append(' ');
                end = next[end];
            }
        }
    }

    public int[] detach(int[] t1) {
        int end = t1[1];
        t1[1] = prev[end];
        next[prev[end]] = -1;
        prev[end] = -1;
        return new int[]{end, end};
    }

    public int[] concat(int[] t1, int[] t2) {
        next[t1[1]] = t2[0];
        prev[t2[0]] = t1[1];
        return new int[]{t1[0], t2[1]};
    }


}
