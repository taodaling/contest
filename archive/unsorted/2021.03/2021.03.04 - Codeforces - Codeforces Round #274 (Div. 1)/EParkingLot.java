package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerComparator;
import template.primitve.generated.datastructure.IntegerMinQueue;
import template.utils.Debug;

import java.util.ArrayDeque;
import java.util.Deque;

public class EParkingLot {
    int[][] bot;
    int[][] up;
    boolean[][] block;
    int n;
    int m;

    void calcCol(int j) {
        for (int i = 0; i < n; i++) {
            if (block[i][j]) {
                up[i][j] = i;
            } else {
                up[i][j] = i == 0 ? -1 : up[i - 1][j];
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            if (block[i][j]) {
                bot[i][j] = i;
            } else {
                bot[i][j] = i == n - 1 ? n : bot[i + 1][j];
            }
        }
    }

    String paint(){
        StringBuilder sb = new StringBuilder("\n");
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                sb.append(block[i][j] ? '#' : '.');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        m = in.ri();
        int k = in.ri();
        block = new boolean[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                block[i][j] = in.rc() == 'X';
            }
        }
        Deque<int[]> removeSeq = new ArrayDeque<>(k);
        for (int i = 0; i < k; i++) {
            removeSeq.add(new int[]{in.ri() - 1, in.ri() - 1});
        }
        for (int[] rm : removeSeq) {
            block[rm[0]][rm[1]] = true;
        }
        int max = 0;
        int[][] dp = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (block[i][j]) {
                    continue;
                }
                if (i > 0 && j > 0) {
                    dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]);
                    dp[i][j] = Math.min(dp[i][j], dp[i - 1][j - 1]);
                }
                dp[i][j]++;
                max = Math.max(dp[i][j], max);
            }
        }
        bot = new int[n][m];
        up = new int[n][m];

        for (int i = 0; i < m; i++) {
            calcCol(i);
        }

        IntegerArrayList ans = new IntegerArrayList(k + 1);
        ans.add(max);
        int[] a = new int[m];
        int[] b = new int[m];
        IntegerMinQueue aq = new IntegerMinQueue(m, IntegerComparator.NATURE_ORDER);
        IntegerMinQueue bq = new IntegerMinQueue(m, IntegerComparator.NATURE_ORDER);

        while (!removeSeq.isEmpty()) {
            int[] tail = removeSeq.removeLast();
            assert block[tail[0]][tail[1]];
            block[tail[0]][tail[1]] = false;
            calcCol(tail[1]);
            int r = tail[0];
            for (int i = 0; i < m; i++) {
                a[i] = r - up[r][i];
                b[i] = bot[r][i] - r - 1;
            }
           // debug.debug("paint", paint());
            while (true) {
                aq.clear();
                bq.clear();
                boolean find = false;
                for (int i = 0; i < m; i++) {
                    aq.addLast(a[i]);
                    bq.addLast(b[i]);
                    while (aq.size() > max + 1) {
                        aq.removeFirst();
                        bq.removeFirst();
                    }
                    if (aq.size() == max + 1 && aq.min() + bq.min() >= max + 1) {
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    break;
                }
                max++;
            }
            ans.add(max);
        }
        ans.reverse();
        for (int i = 1; i <= k; i++) {
            out.println(ans.get(i));
        }
    }
}
