package on2021_04.on2021_04_19_Codeforces___Codeforces_Round__204__Div__1_.E__Jeff_and_Permutation;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EJeffAndPermutation {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] p = in.ri(n);
        for(int i = 0; i < n; i++){
            p[i] = Math.abs(p[i]);
        }
        int inv = 0;
        for(int i = 0; i < n; i++){
            for(int j = i + 1; j < n; j++){
                if(p[i] > p[j]){
                    inv++;
                }
            }
        }

        Map<Integer, List<Integer>> map = IntStream.range(0, n).boxed().collect(Collectors.groupingBy(i -> p[i]));
        for (Map.Entry<Integer, List<Integer>> entry : map.entrySet()) {
            int k = entry.getKey();
            List<Integer> indices = entry.getValue();
            int[] x = new int[indices.size()];
            for (int i = 0; i < indices.size(); i++) {
                int index = indices.get(i);
                int total = 0;
                for (int j = 0; j < n; j++) {
                    if (p[j] < k) {
                        if (j < index) {
                            total++;
                        } else {
                            total--;
                        }
                    }
                }
                x[i] = total;
            }

            int best = dp(x);
            inv += best;
        }
        out.println(inv);
    }

    int inf = (int) 1e9;

    int dp(int[] x) {
        int n = x.length;
        int[] prev = new int[n + 1];
        int[] next = new int[n + 1];
        Arrays.fill(prev, inf);
        prev[0] = 0;
        for (int i = 0; i < n; i++) {
            Arrays.fill(next, inf);
            for (int j = 0; j <= i; j++) {
                //not swap
                next[j] = Math.min(next[j], prev[j]);
                //swap
                if (j + 1 <= n) {
                    next[j + 1] = Math.min(next[j], prev[j] + x[i] + (i - j));
                }
            }
            int[] tmp = prev;
            prev = next;
            next = tmp;
        }
        return Arrays.stream(prev).min().getAsInt();
    }
}
