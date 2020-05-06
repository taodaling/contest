package contest;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class TransformBoardDiv1 {
    public int[] getOperations(String[] start, String[] target) {
        int n = start.length;
        int m = start[0].length();
        begin = new int[n][m];
        int[][] end = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                begin[i][j] = start[i].charAt(j) == '#' ? 1 : 0;
                end[i][j] = target[i].charAt(j) == '#' ? 1 : 0;
            }
        }

        int[] impossible = new int[]{-1};
        for (int i = 0; i < n; i++) {
            for (int j = m - 1; j >= 0; j--) {
                if (begin[i][j] == end[i][j] || begin[i][j] == 1) {
                    continue;
                }
                for (int k = j; k >= 0 && begin[i][j] != end[i][j]; k--) {
                    for (int t = i; t >= 0 && begin[i][j] != end[i][j]; t--) {
                        if (k == j && t == i || begin[t][k] == 0 || begin[t][k] == end[t][k]) {
                            continue;
                        }
                        if (k == j || t == i) {
                            add(t, k, i, j);
                        } else {
                            add(t, k, i, k);
                            add(i, k, i, j);
                        }
                    }
                }

                if (begin[i][j] != end[i][j]) {
                    return impossible;
                }
            }
        }

        List<int[]> index = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (begin[i][j] != end[i][j]) {
                    index.add(new int[]{i, j});
                }
            }
        }

        if (index.size() % 2 == 1) {
            return impossible;
        }

        index.sort((a, b) -> Integer.compare(a[0] + a[1], b[0] + b[1]));

        for (int i = 1; i < index.size(); i += 2) {
            int[] p1 = index.get(i - 1);
            int[] p2 = index.get(i);

            if (p1[0] == p2[0] || p1[1] == p2[1]) {
                add(p1[0], p1[1], p2[0], p2[1]);
            } else if (p1[0] < p2[0] && p1[1] < p2[1]) {
                if (begin[p2[0]][p1[1]] == 1) {
                    add(p2[0], p1[1], p2[0], p2[1]);
                    add(p1[0], p1[1], p2[0], p1[1]);
                } else {
                    add(p1[0], p1[1], p2[0], p1[1]);
                    add(p2[0], p1[1], p2[0], p2[1]);
                }
            } else {
                if (p1[0] > p2[0]) {
                    int[] tmp = p1;
                    p1 = p2;
                    p2 = tmp;
                }
                add(p1[0], p1[1], p2[0], p1[1]);
                add(p2[0], p2[1], p2[0], p1[1]);
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (begin[i][j] != end[i][j]) {
                    throw new RuntimeException();
                }
            }
        }

        return seq.stream().mapToInt(Integer::intValue).toArray();
    }

    List<Integer> seq = new ArrayList<>();
    int[][] begin;

    public void add(int r1, int c1, int r2, int c2) {
        seq.add(r1 * 1000000 + c1 * 10000 + r2 * 100 + c2);
        begin[r2][c2] ^= begin[r1][c1];
        begin[r1][c1] = 0;
    }
}
