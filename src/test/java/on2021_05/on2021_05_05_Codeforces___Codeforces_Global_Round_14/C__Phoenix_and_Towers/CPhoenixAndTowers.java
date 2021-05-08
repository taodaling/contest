package on2021_05.on2021_05_05_Codeforces___Codeforces_Global_Round_14.C__Phoenix_and_Towers;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Comparator;
import java.util.TreeSet;

public class CPhoenixAndTowers {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        in.ri();
        int[] h = in.ri(n);
        long[] heights = new long[m];
        TreeSet<Integer> set = new TreeSet<>(Comparator.<Integer>comparingLong(x -> heights[x]).thenComparing(Comparator.naturalOrder()));
        for(int i = 0; i < m; i++){
            set.add(i);
        }
        out.println("YES");
        for(int t : h){
            Integer index = set.pollFirst();
            heights[index] += t;
            out.append(index + 1).append(' ');
            set.add(index);
        }
        out.println();

    }
}
