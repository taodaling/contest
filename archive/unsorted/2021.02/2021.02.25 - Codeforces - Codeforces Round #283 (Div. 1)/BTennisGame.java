package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class BTennisGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        IntegerArrayList[] pos = new IntegerArrayList[]{new IntegerArrayList(n), new IntegerArrayList(n)};
        int[] data = new int[n];
        int[] indices = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = in.ri() - 1;
            pos[data[i]].add(i);
            indices[i] = pos[data[i]].size() - 1;
        }
        int[] reg = new int[2];
        Arrays.fill(reg, n);
        int[] next = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            next[i] = reg[data[i] ^ 1];
            reg[data[i]] = i;
        }
        List<int[]> ans = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            int now = 0;
            int[] score = new int[2];
            boolean valid = true;
            int last = 0;
            while (now < n) {
                int cur = data[now];
                int to = indices[now] + i - 1;
                if (to >= pos[cur].size()) {
                    to = n;
                } else {
                    to = pos[cur].get(to);
                }
                int diff = cur ^ 1;
                int toDiff = n;
                if (next[now] < n) {
                    toDiff = indices[next[now]] + i - 1;
                    if (toDiff >= pos[diff].size()) {
                        toDiff = n;
                    } else {
                        toDiff = pos[diff].get(toDiff);
                    }
                }
                int dst = Math.min(to, toDiff);
                if (dst == n) {
                    valid = false;
                    break;
                }
                last = data[dst];
                score[last]++;
                now = dst + 1;
            }
            if (!valid || score[last ^ 1] >= score[last]) {
                continue;
            }
            ans.add(new int[]{score[last], i});
        }

        ans.sort(Comparator.<int[]>comparingInt(x -> x[0]).thenComparingInt(x -> x[1]));
        out.println(ans.size());
        for(int[] x : ans){
            out.append(x[0]).append(' ').append(x[1]).println();
        }
    }
}