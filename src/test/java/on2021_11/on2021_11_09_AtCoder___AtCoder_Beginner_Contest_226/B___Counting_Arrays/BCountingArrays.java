package on2021_11.on2021_11_09_AtCoder___AtCoder_Beginner_Contest_226.B___Counting_Arrays;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BCountingArrays {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Set<List<Integer>> set = new HashSet<>(n);
        for(int i = 0; i < n; i++){
            int l = in.ri();
            List<Integer> list = new ArrayList<>(l);
            for(int j = 0; j < l; j++){
                list.add(in.ri());
            }
            set.add(list);
        }
        out.println(set.size());
    }
}
