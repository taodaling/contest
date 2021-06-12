package on2021_05.on2021_05_27_Codeforces___Educational_Codeforces_Round_107__Rated_for_Div__2_.D__Min_Cost_String;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DMinCostString {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        g = new List[k];
        for (int i = 0; i < k; i++) {
            g[i] = IntStream.range(0, k).boxed().collect(Collectors.toList());
        }
        dq = new ArrayList<>(k * k);
        dfs(0);
        if(dq.size() > 1){
            CollectionUtils.pop(dq);
        }
        Collections.reverse(dq);
        for(int i = 0; i < n; i++){
            int x = dq.get(i % dq.size());
            out.append((char)('a' + x));
        }
    }

    List<Integer> dq;
    public void dfs(int root){
        while(!g[root].isEmpty()){
            int back = CollectionUtils.pop(g[root]);
            dfs(back);
        }
        dq.add(root);
    }

    List<Integer>[] g;
}