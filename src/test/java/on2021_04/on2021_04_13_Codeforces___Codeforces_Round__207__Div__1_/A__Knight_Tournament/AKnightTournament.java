package on2021_04.on2021_04_13_Codeforces___Codeforces_Round__207__Div__1_.A__Knight_Tournament;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.TreeSet;

public class AKnightTournament {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] res = new int[n];
        TreeSet<Integer> set = new TreeSet<>();
        for (int i = 0; i < n; i++) {
            set.add(i);
        }
        for (int i = 0; i < m; i++) {
            int l = in.ri() - 1;
            int r = in.ri() - 1;
            int x = in.ri() - 1;
            while (true) {
                Integer head = set.ceiling(l);
                if (head == null || head > r) {
                    break;
                }
                set.remove(head);
                res[head] = x;
            }
            set.add(x);
        }
        res[set.first()] = -1;
        for(int x : res){
            out.append(x + 1).append(' ');
        }
    }
}
