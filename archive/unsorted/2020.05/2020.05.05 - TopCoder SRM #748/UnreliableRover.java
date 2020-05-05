package contest;

import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;

public class UnreliableRover {
  //  Debug debug = new Debug(true);
    public long getArea(String direction, int[] minSteps, int[] maxSteps) {
        int n = direction.length();
        List<Integer> move = new ArrayList<>();
        int w = 1;
        int h = 1;
        for (int i = 0; i < n; i++) {
            char c = direction.charAt(i);
            if (c == '?') {
                move.add(maxSteps[i]);
                continue;
            }
            int dist = maxSteps[i] - minSteps[i];
            if (c == 'S' || c == 'N') {
                h += dist;
            } else {
                w += dist;
            }
        }

        List<Rect> rects = new ArrayList<>(1 << move.size());
        dfs(move.stream().mapToInt(Integer::intValue).toArray(), move.size() - 1, rects, w, h);
        rects.sort((a, b) -> -Integer.compare(a.w, b.w));
     //   debug.debug("rects", rects);

        long ans = 0;
        Rect last = new Rect((int) 2e9, 0);
        for (Rect r : rects) {
            ans += (long) (r.h - last.h) * r.w;
            last = r;
        }

        return ans;
    }

    public void dfs(int[] move, int i, List<Rect> rects, int w, int h) {
        if (i < 0) {
            rects.add(new Rect(w, h));
            return;
        }
        dfs(move, i - 1, rects, w + move[i] * 2, h);
        dfs(move, i - 1, rects, w, h + move[i] * 2);
    }
}

class Rect {
    int w;
    int h;

    public Rect(int w, int h) {
        this.w = w;
        this.h = h;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", w, h);
    }
}
