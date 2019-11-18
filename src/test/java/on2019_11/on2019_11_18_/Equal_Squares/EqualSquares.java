package on2019_11.on2019_11_18_.Equal_Squares;



import template.*;

import java.util.HashMap;
import java.util.Map;

public class EqualSquares {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        m = in.readInt();
        mat = new char[n][m];
        rows31 = new Hash[n];
        rows11 = new Hash[n];
        r31 = new RollingHash(n, Hash.POWER.pow(31, m));
        r11 = new RollingHash(n, Hash.POWER.pow(11, m));
        for (int i = 0; i < n; i++) {
            in.readString(mat[i], 0);
            if (i == 0) {
                rows31[i] = new Hash(m, 31);
                rows11[i] = new Hash(m, 11);
            } else {
                rows31[i] = new Hash(rows31[i - 1]);
                rows11[i] = new Hash(rows11[i - 1]);
            }
            rows31[i].populate(mat[i], m);
            rows11[i].populate(mat[i], m);
        }


        int l = 1;
        int r = Math.min(n, m);
        while (l < r) {
            int mid = (l + r + 1) / 2;
            if (check(mid) == null) {
                r = mid - 1;
            } else {
                l = mid;
            }
        }

        int[][] poses = check(l);
        if (poses == null) {
            out.println(0);
            return;
        }

        out.println(l);
        out.printf("%d %d\n%d %d", poses[0][0] + 1, poses[0][1] + 1, poses[1][0] + 1, poses[1][1] + 1);
    }

    int n;
    int m;
    char[][] mat;
    Map<Long, Long> map = new HashMap<>(500 * 500);
    Hash[] rows31;
    Hash[] rows11;
    RollingHash r31;
    RollingHash r11;

    public int[][] check(int k) {
        map.clear();
        for (int j = 0; j + k - 1 < m; j++) {
            r11.clear();
            r31.clear();
            for (int i = 0; i < k - 1; i++) {
                r11.addLast(rows11[i].partial(j, j + k - 1));
                r31.addLast(rows31[i].partial(j, j + k - 1));
            }
            for (int i = k - 1; i < n; i++) {
                r11.addLast(rows11[i].partial(j, j + k - 1));
                r31.addLast(rows31[i].partial(j, j + k - 1));
                Long h = DigitUtils.asLong(r11.hash(), r31.hash());
                if (map.containsKey(h)) {
                    long pos = map.get(h);
                    return new int[][]{{DigitUtils.highBit(pos), DigitUtils.lowBit(pos)}, {i - k + 1, j}};
                }
                map.put(h, DigitUtils.asLong(i - k + 1, j));
                r11.removeFirst();
                r31.removeFirst();
            }
        }
        return null;
    }
}
