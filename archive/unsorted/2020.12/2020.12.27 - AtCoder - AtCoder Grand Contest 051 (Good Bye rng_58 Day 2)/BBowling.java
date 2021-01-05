package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BBowling {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        List<int[]> pts = new ArrayList<>();
        pts.add(new int[2]);
        int l = 1;
        int round = 10;
        while (round-- > 0) {
            pts = gen(pts, l);
            l *= 3;
        }
        out.println(pts.size());
        for (int[] pt : pts) {
            out.append(pt[0]).append(' ').append(pt[1]).println();
        }

        valid(pts);
    }

    public void valid(List<int[]> pts) {
        Set<Integer> a = new HashSet<>();
        Set<Integer> b = new HashSet<>();
        Set<Integer> c = new HashSet<>();
        Set<Integer> d = new HashSet<>();
        for(int[] x : pts){
            a.add(x[1]);
            b.add(x[0]);
            c.add(x[1] - x[0]);
            d.add(x[1] + x[0]);
        }
        int max = Math.max(a.size(), b.size());
        max = Math.max(max, c.size());
        assert max * 10 <= d.size();
    }

    public List<int[]> gen(List<int[]> list, int l) {
        List<int[]> ans = new ArrayList<>(list.size() * 3);
        for (int[] pt : list) {
            ans.add(pt);
            ans.add(new int[]{pt[0], pt[1] + l + 1});
            ans.add(new int[]{pt[0] + l + 1, pt[1] + l + 1});
        }
        return ans;
    }
}