package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerHashMap;

public class BLongJumps {
    int findDelta(int d, boolean first) {
        map.clear();
        int ans = -1;
        for (int v : a) {
            if (map.containKey(v - d)) {
                if (ans == -1 || !first) {
                    ans = v;
                }
            }
            map.modify(v, 1);
        }
        return ans;
    }

    int[] a;
    IntegerHashMap map;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int l = in.ri();
        int x = in.ri();
        int y = in.ri();
        a = in.ri(n);
        map = new IntegerHashMap(n, false);
        boolean xOk = findDelta(x, true) != -1;
        boolean yOk = findDelta(y, true) != -1;
        if (xOk && yOk) {
            out.println(0);
            return;
        }
        if (xOk != yOk) {
            int target = xOk ? y : x;
            out.println(1);
            out.println(target);
            return;
        }
        //find both
        int both = findDelta(x + y, true);
        if (both != -1) {
            //find, cool
            out.println(1);
            out.println(both - y);
            return;
        }
        int leftside = findDelta(y - x, true);
        if (leftside != -1 && leftside + x <= l) {
            out.println(1);
            out.println(leftside + x);
            return;
        }
        int rightside = findDelta(y - x, false);
        if(rightside != -1 && rightside - y >= 0){
            out.println(1);
            out.println(rightside - y);
            return;
        }
        out.println(2);
        out.append(x).append(' ').append(y).println();
    }
}
