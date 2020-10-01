package on2020_10.on2020_10_01_Codeforces___Grakn_Forces_2020.F__Two_Different;



import dp.Lis;
import template.binary.Bits;
import template.io.FastInput;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FTwoDifferent {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        List<List<Integer>> lists = new ArrayList<>();
        int alloc = 1;

        for (int i = 0; i < 20; i++) {
            if (Bits.get(n, i) == 0) {
                continue;
            }
            lists.add(solve(alloc, alloc + (1 << i) - 1));
            alloc += 1 << i;
        }

        while(lists.size() > 2){
            List<Integer> top = lists.remove(0);
            List<Integer> second = lists.get(0);

            List<Integer> last = lists.get(lists.size() - 1);

            while(top.size() < second.size()) {
                List<Integer> delete = new ArrayList<>();
                while(delete.size() != top.size()){
                    delete.add(last.remove(last.size() - 1));
                }
                top = merge(top, delete);
            }

            merge(second, top);
        }

        out.println(qs.size());
        for(int[] x : qs){
            out.print(x[0]);
            out.print(' ');
            out.print(x[1]);
            out.println();
        }
    }

    public List<Integer> merge(List<Integer> a, List<Integer> b){
        for(int i = 0; i < a.size(); i++){
            addPair(a.get(i), b.get(i));
        }
        a.addAll(b);
        return a;
    }

    List<int[]> qs = new ArrayList<>();

    public void addPair(int x, int y) {
        qs.add(new int[]{x, y});
    }


    public List<Integer> solve(int l, int r) {
        if (l == r) {
            List<Integer> ans = new ArrayList<>();
            ans.add(l);
            return ans;
        }
        int m = (l + r) / 2;
        List<Integer> a = solve(l, m);
        List<Integer> b = solve(m + 1, r);
        return merge(a, b);
    }
}

