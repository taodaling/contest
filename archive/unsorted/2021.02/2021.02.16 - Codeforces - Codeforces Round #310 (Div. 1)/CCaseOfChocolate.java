package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Map;
import java.util.TreeMap;

public class CCaseOfChocolate {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();

        TreeMap<Integer, Cut> map = new TreeMap<>();
        map.put(0, new Cut(n + 1, 0, 'U', 0));
        map.put(n + 1, new Cut(0, n + 1, 'L', 0));
        for (int i = 0; i < q; i++) {

            int y = in.ri();
            int x = in.ri();
            char dir = in.rc();
            if (map.containsKey(y)) {
                out.println(0);
                continue;
            }
            Cut block = null;
            if (dir == 'L') {
                block = map.floorEntry(y).getValue();
            } else {
                block = map.ceilingEntry(y).getValue();
            }
            int to;
            if (block.dir == dir) {
                to = block.endPos;
            } else {
                if (dir == 'L') {
                    to = block.y;
                } else {
                    to = block.x;
                }
            }
            if(dir == 'L'){
                out.println(y - to);
            }else{
                out.println(x - to);
            }
            map.put(y, new Cut(x, y, dir, to));
        }
    }
}

class Cut {
    int x;
    int y;
    char dir;
    int endPos;

    public Cut(int x, int y, char dir, int endPos) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.endPos = endPos;
    }
}