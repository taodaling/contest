package contest;

import template.math.DigitUtils;

import java.util.ArrayList;
import java.util.List;

public class Hamiltons {
    public int[] construct(int N, int L) {
        int[][] mat = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                mat[i][j] = L;
            }
        }
        int half = (L + 1) / 2;
        int x = -1;
        int y = -1;
        for (int i = 1; i <= L; i++) {
            for (int j = 1; j <= L; j++) {
                if ((N - 1) * i + L > N * j &&
                        (N - 1) * (j - i) >= half) {
                    x = i;
                    y = j;
                }
            }
        }

        for (int i = 0; i < N; i++) {
            mat[i][(i + 1) % N] = y;
        }

        if (N % 2 == 0) {
            for (int i = 0; i + 2 < N; i += 2) {
                mat[i][i + 2] = x;
            }
            mat[N - 2][1] = x;
            mat[1][N - 1] = x;
            for (int i = N - 1; i > 3; i -= 2) {
                mat[i][i - 2] = x;
            }
        } else {
            for (int i = 0; i < N - 1; i++) {
                mat[i][(i + 2) % N] = x;
            }
        }

        List<Integer> ans = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                ans.add(mat[i][j]);
            }
        }

        return ans.stream().mapToInt(Integer::intValue).toArray();
    }

}
