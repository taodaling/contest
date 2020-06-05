package contest;

import template.datastructure.DSU;
import template.datastructure.MultiWayDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.Arrays;

public class CJohnnyAndMegansNecklace {
    //Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();

        int[] balls = new int[n * 2];
        in.populate(balls);

//        for (int x : balls) {
//            if ((x & ((1 << 3) - 1)) == 1) {
//                debug.debug("x", x);
//            }
//        }

        int bans = -1;
        int limit = 20;
        DSU dsu = new DSU(1 << limit);
        int[] cnts = new int[1 << limit];
        for (int i = 0; i < limit; i++) {
            int mask = (1 << (i + 1)) - 1;
            dsu.reset();
            Arrays.fill(cnts, 0);
            for (int j = 0; j < n * 2; j += 2) {
                dsu.merge(mask & balls[j], mask & balls[j + 1]);
            }
            boolean valid = true;
            for (int j = 2; valid && j < n * 2; j += 2) {
                int prev = balls[j - 2] & mask;
                int cur = balls[j] & mask;
                if (dsu.find(prev) != dsu.find(cur)) {
                    valid = false;
                }
            }

            for (int x : balls) {
                cnts[x & mask]++;
            }

            for (int j = 0; valid && j < cnts.length; j++) {
                if ((cnts[j] & 1) == 1) {
                    valid = false;
                }
            }

            if (!valid) {
                bans = i - 1;
                break;
            }
            bans = i;
        }


        if (bans == -1) {
            out.println(0);
            for (int i = 0; i < 2 * n; i++) {
                out.append(i + 1).append(' ');
            }
            return;
        }

        DSU ballDSU = new DSU(n);
        int[] stack = new int[1 << limit];
        Arrays.fill(stack, -1);
        int[] match = new int[n * 2];
        MultiWayDeque<int[]> groups = new MultiWayDeque<>(1 << limit, n);
        int mask = (1 << (1 + bans)) - 1;
        for (int i = 0; i < n * 2; i++) {
            int b = balls[i] & mask;
            if (stack[b] == -1) {
                stack[b] = i;
            } else {
                ballDSU.merge(i / 2, stack[b] / 2);
                groups.addLast(b, new int[]{i, stack[b]});
                addEdge(match, i, stack[b]);
                stack[b] = -1;
            }
        }

        for (int i = 0; i < 1 << limit; i++) {
            if (groups.isEmpty(i)) {
                continue;
            }
            int[] top = groups.removeFirst(i);
            while (!groups.isEmpty(i)) {
                int[] head = groups.removeFirst(i);
                if (ballDSU.find(top[0] / 2) == ballDSU.find(head[0] / 2)) {
                    continue;
                }
                ballDSU.merge(top[0] / 2, head[0] / 2);
                addEdge(match, top[0], head[0]);
                addEdge(match, top[1], head[1]);
                top[1] = head[0];
            }
        }

        out.println(bans + 1);
        int index = 0;
        for (int i = 0; i < n; i++) {
            out.append(index + 1).append(' ');
            index ^= 1;
            out.append(index + 1).append(' ');
            index = match[index];
        }
    }

    public void addEdge(int[] match, int i, int j) {
        match[i] = j;
        match[j] = i;
    }
}