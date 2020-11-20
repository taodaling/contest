package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class CleaningTheRoom {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Box[] boxes = new Box[n];
        for (int i = 0; i < n; i++) {
            int k = in.readInt();
            boxes[i] = new Box();
            boxes[i].l = (int) 1e6;
            boxes[i].r = -1;
            int last = -1;
            for (int j = 0; j < k; j++) {
                int x = in.readInt();
                if (x < last) {
                    out.println("NO");
                    return;
                }
                last = x;
                boxes[i].l = Math.min(boxes[i].l, x);
                boxes[i].r = Math.max(boxes[i].r, x);
            }
        }

        Arrays.sort(boxes, (a, b) -> a.l != b.l ? Integer.compare(a.l, b.l) : Integer.compare(a.r, b.r));
        for (int i = 1; i < n; i++) {
            if (boxes[i].l < boxes[i - 1].r) {
                out.println("NO");
                return;
            }
        }
        out.println("YES");
    }
}

class Box {
    int l;
    int r;
}
