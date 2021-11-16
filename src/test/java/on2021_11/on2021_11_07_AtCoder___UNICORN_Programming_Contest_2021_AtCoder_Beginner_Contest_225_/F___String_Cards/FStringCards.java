package on2021_11.on2021_11_07_AtCoder___UNICORN_Programming_Contest_2021_AtCoder_Beginner_Contest_225_.F___String_Cards;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SortUtils;

import java.util.Arrays;

public class FStringCards {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        String[] ss = new String[n];
        for(int i = 0; i < n; i++){
            ss[i] = in.rs();
        }
        Arrays.sort(ss, (a, b) -> -(a + b).compareTo(b + a));
        String[] prev = new String[k + 1];
        String[] next = new String[k + 1];
        String inf = "|";
        Arrays.fill(prev, inf);
        prev[0] = "";
        for(String s : ss){
            Arrays.fill(next, inf);
            for(int j = 0; j <= k; j++){
                next[j] = SortUtils.min(prev[j], next[j]);
                if(j + 1 <= k){
                    next[j + 1] = SortUtils.min(s + prev[j], next[j + 1]);
                }
            }
            String[] tmp = prev;
            prev = next;
            next = tmp;
        }

        String ans = prev[k];
        out.println(ans);
    }


}
