package contest;

import template.DigitUtils;
import template.FastInput;
import template.FastOutput;
import template.IntDeque;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[][] boards = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (in.readChar() == '#') {
                    boards[i][j] = 1;
                }
            }
        }

        int ans = Math.max(n, m);

        int[][] upRight = new int[n + 1][m + 1];
        for (int j = 1; j <= m; j++) {
            upRight[1][j] = m - j + 1;
        }
        for (int i = 2; i <= n; i++) {
            upRight[i][m] = 1;
            for (int j = m - 1; j >= 1; j--) {
                upRight[i][j] = DigitUtils
                                .isEven(boards[i][j] + boards[i - 1][j] + boards[i][j + 1] + boards[i - 1][j + 1])
                                                ? upRight[i][j + 1] + 1
                                                : 1;
            }
        }


        int[][] botRight = new int[n + 1][m + 1];
        for (int j = 1; j <= m; j++) {
            botRight[n][j] = m - j + 1;
        }
        for (int i = 1; i < n; i++) {
            botRight[i][m] = 1;
            for (int j = m - 1; j >= 1; j--) {
                botRight[i][j] = DigitUtils
                                .isEven(boards[i][j] + boards[i + 1][j] + boards[i][j + 1] + boards[i + 1][j + 1])
                                                ? botRight[i][j + 1] + 1
                                                : 1;
            }
        }

        int[][] upUntil = new int[n + 1][m + 1];
        int[][] botUntil = new int[n + 1][m + 1];

        IntDeque deque = new IntDeque(n);
        for (int j = 1; j <= m; j++) {
            deque.clear();
            for (int i = 1; i <= n; i++) {
                while (!deque.isEmpty() && upRight[deque.peekLast()][j] >= upRight[i][j]) {
                    deque.removeLast();
                }
                if (deque.isEmpty()) {
                    upUntil[i][j] = 1;
                } else {
                    upUntil[i][j] = deque.peekLast();
                }
                deque.addLast(i);
            }
        }

        for (int j = 1; j <= m; j++) {
            deque.clear();
            for (int i = n; i >= 1; i--) {
                while (!deque.isEmpty() && botRight[deque.peekFirst()][j] >= botRight[i][j]) {
                    deque.removeFirst();
                }
                if (deque.isEmpty()) {
                    botUntil[i][j] = n;
                } else {
                    botUntil[i][j] = deque.peekFirst();
                }
                deque.addFirst(i);
            }
        }

        for (int i = 2; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int area1 = (botUntil[i - 1][j] - upUntil[i][j] + 1) * upRight[i][j];
                int area2 = (botUntil[i - 1][j] - (i - 1) + 1) * upRight[i][j];
                int area3 = (i - upUntil[i][j] + 1) * upRight[i][j];

                ans = Math.max(ans, area1);
                ans = Math.max(ans, area2);
                ans = Math.max(ans, area3);
            }
        }

        out.println(ans);
    }
}
