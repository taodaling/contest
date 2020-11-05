package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DRectangularPolyline {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int h = in.readInt();
        int[] hl = new int[h];
        in.populate(hl);
        int v = in.readInt();
        int[] vl = new int[v];
        in.populate(vl);

        if (h != v || Arrays.stream(hl).sum() % 2 != 0 ||
                Arrays.stream(vl).sum() % 2 != 0) {
            out.println("No");
            return;
        }

        List<Integer>[] vlist = part(vl);
        List<Integer>[] hlist = part(hl);
        if (vlist == null || hlist == null) {
            out.println("No");
            return;
        }

        if (hlist[0].size() > hlist[1].size()) {
            SequenceUtils.swap(hlist, 0, 1);
        }
        if (vlist[1].size() > vlist[0].size()) {
            SequenceUtils.swap(vlist, 0, 1);
        }

        hlist[1] = hlist[1].stream().map(x -> -x).collect(Collectors.toList());
        vlist[1] = vlist[1].stream().map(x -> -x).collect(Collectors.toList());
        hlist[0].sort(Comparator.naturalOrder());
        vlist[0].sort(Comparator.reverseOrder());
        hlist[1].sort(Comparator.reverseOrder());
        vlist[1].sort(Comparator.naturalOrder());
        int[][] chance = new int[][]{
                {0, 0},
                {1, 0},
                {1, 1},
                {0, 1}
        };


        out.println("Yes");
        int curX = 0;
        int curY = 0;
        for (int[] c : chance) {
            int x = c[0];
            int y = c[1];

            while (!hlist[x].isEmpty() && !vlist[y].isEmpty()) {
                curX += pop(hlist[x]);

                out.append(curX).append(' ').append(curY).println();
                curY += pop(vlist[y]);

                out.append(curX).append(' ').append(curY).println();
            }
        }
    }

    public int pop(List<Integer> list) {
        return list.remove(list
                .size() - 1);
    }

    int limit = 250;
    byte[][] dp = new byte[limit * 2 + 1][limit * 1000 + 1];


    public List<Integer>[] part(int[] a) {
        int n = a.length;

        int sum = Arrays.stream(a).sum();
        int half = sum / 2;
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= half; j++) {
                dp[i][j] = -1;
            }
        }

        dp[0][0] = 0;
        for (int i = 1; i <= n; i++) {
            int v = a[i - 1];
            for (int j = 0; j <= half; j++) {
                dp[i][j] = (byte) (dp[i - 1][j] == -1 ? -1 : 0);
                if (j >= v && dp[i - 1][j - v] >= 0) {
                    dp[i][j] = 1;
                }
            }
        }

        if (dp[n][half] == -1) {
            return null;
        }

        List<Integer>[] ans = new List[2];
        for (int i = 0; i < 2; i++) {
            ans[i] = new ArrayList<>();
        }
        int x = n;
        int y = half;
        while (x > 0) {
            if (dp[x][y] == 0) {
                ans[1].add(a[x - 1]);
                x--;
            } else {
                ans[0].add(a[x - 1]);
                y -= a[x - 1];
                x--;
            }
        }

        return ans;
    }


}
