package on2020_04.on2020_04_17_VK_Cup_2018___Round_2.E___Wardrobe;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class TaskE {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int l = in.readInt();
        int r = in.readInt();
        Box[] boxes = new Box[n];
        for (int i = 0; i < n; i++) {
            boxes[i] = new Box();
        }
        int sum = 0;
        for (int i = 0; i < n; i++) {
            boxes[i].a = in.readInt();
            sum += boxes[i].a;
        }

        for (int i = 0; i < n; i++) {
            boxes[i].b = in.readInt();
        }

        int ll = sum - r;
        int rr = (r - l) + ll;
        Arrays.sort(boxes, (a, b) -> a.b == b.b ? -Integer.compare(a.a, b.a) :
                Integer.compare(a.b, b.b));

        debug.debug("boxes", boxes);

        int[] last = new int[rr + 1];
        int[] cur = new int[rr + 1];
        int inf = (int) 1e8;
        SequenceUtils.deepFill(last, -inf);
        last[0] = 0;

        debug.debug("last", last);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= rr; j++) {
                cur[j] = last[j];
                if (j >= boxes[i].a) {
                    cur[j] = Math.max(cur[j], last[j - boxes[i].a] + (ll <= j ? boxes[i].b : 0));
                }
            }
            int[] tmp = last;
            last = cur;
            cur = tmp;

            debug.debug("last", last);
        }


        int ans = 0;
        for (int v : last) {
            ans = Math.max(ans, v);
        }
        out.println(ans);
    }
}

class Box {
    int a;
    int b;

    @Override
    public String toString() {
        return String.format("%d %d", a, b);
    }
}