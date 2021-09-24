package on2021_07.on2021_07_26_Codeforces___2013_USP_Try_outs.G__Classroom_Scheduling;



import template.datastructure.PQTree;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.List;

public class GClassroomScheduling {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int c = in.ri();
        int p = in.ri();
        PQTree pq = new PQTree(c);
        boolean[] set = new boolean[c];
        for(int i = 0; i < p; i++){
            Arrays.fill(set, false);
            int k = in.ri();
            for(int j = 0; j < k; j++){
                set[in.ri() - 1] = true;
            }
            pq.update(set);
        }
        if(!pq.possible()){
            out.println(-1);
            return;
        }
        List<Integer> sol = pq.getOrder();
        for(int x : sol){
            out.append(x + 1).append(' ');
        }
        out.println();
    }
}
