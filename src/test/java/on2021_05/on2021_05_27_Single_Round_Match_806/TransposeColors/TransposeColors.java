package on2021_05.on2021_05_27_Single_Round_Match_806.TransposeColors;



import template.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TransposeColors {
    public int[] move(int N) {
        if(N == 1){
            return new int[0];
        }

        g = new List[N];
        for (int i = 0; i < N; i++) {
            int finalI = i;
            g[i] = IntStream.range(0, N).filter(x -> x != finalI)
                    .boxed().collect(Collectors.toList());
        }
        dq = new ArrayList<>(N * N);
        dfs(0);
        List<Integer> ans = new ArrayList<>(N * N);
        for(int i = 1; i < dq.size(); i++){
            int a = dq.get(i - 1);
            int b = dq.get(i);
            ans.add(a * N + b);
        }
        ans.add(N * N);
        return ans.stream().mapToInt(Integer::intValue).toArray();
    }

    List<Integer>[] g;
    List<Integer> dq;

    void dfs(int root){
        while(!g[root].isEmpty()){
            int head = CollectionUtils.pop(g[root]);
            dfs(head);
        }
        dq.add(root);
    }
}
