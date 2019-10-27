package contest;

import template.FastInput;
import template.FastOutput;
import template.ISAP;

public class TaskD {
    int n;
    int m;

    int idOfRow(int i) {
        return i;
    }

    int idOfCol(int i) {
        return n + i;
    }

    int idOfSrc() {
        return n + m + 1;
    }

    int idOfDst() {
        return idOfSrc() + 1;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        m = in.readInt();

        int inf = (int) 1e8;
        ISAP isap = new ISAP(idOfDst());
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                char c = in.readChar();
                if (c == '.') {
                    continue;
                }
                isap.getChannel(idOfRow(i), idOfCol(j)).modify(2, 1);
                if (c == 'S') {
                    isap.getChannel(idOfSrc(), idOfRow(i)).modify(inf, 0);
                    isap.getChannel(idOfSrc(), idOfCol(j)).modify(inf, 0);
                }
                if (c == 'T') {
                    isap.getChannel(idOfRow(i), idOfDst()).modify(inf, 0);
                    isap.getChannel(idOfCol(j), idOfDst()).modify(inf, 0);
                }
            }
        }

        isap.setSource(idOfSrc());
        isap.setTarget(idOfDst());

        int f = (int) (isap.sendFlow(inf) + 0.5);
        if (f == inf) {
            out.println(-1);
            return;
        }
        out.println(f);
    }
}
