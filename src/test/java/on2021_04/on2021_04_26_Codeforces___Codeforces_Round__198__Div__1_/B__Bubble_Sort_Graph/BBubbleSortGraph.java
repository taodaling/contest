package on2021_04.on2021_04_26_Codeforces___Codeforces_Round__198__Div__1_.B__Bubble_Sort_Graph;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.TreeSet;

public class BBubbleSortGraph {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        TreeSet<Integer> set = new TreeSet<>();
        for (int x : a) {
            Integer ceil = set.ceiling(x);
            if(ceil != null){
                set.remove(ceil);
            }
            set.add(x);
        }
        out.println(set.size());
    }
}
