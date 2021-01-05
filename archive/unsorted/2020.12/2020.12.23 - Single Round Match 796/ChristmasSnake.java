package contest;

import template.utils.SequenceUtils;

import java.util.*;

public class ChristmasSnake {
    char[][] mat;

    public String turnAround(String[] game) {
        mat = new char[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                mat[i][j] = game[i].charAt(j);
            }
        }

        int inf = (int) 1e9;
        int[][] dist = new int[8][8];
        for (int i = 0; i < 8; i++) {
            Arrays.fill(dist[i], inf);
        }
        Deque<int[]> dq = new ArrayDeque<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (mat[i][j] == 'a') {
                    dq.add(new int[]{i, j});
                    dist[i][j] = 0;
                }
            }
        }
        int[] begin = dq.peek();
        int[][] dirs = new int[][]{
                {1, 0},
                {0, -1},
                {-1, 0},
                {0, 1}
        };
        prev = new int[8][8][];
        while (!dq.isEmpty()) {
            int[] head = dq.removeFirst();
            for (int[] d : dirs) {
                int x = head[0] + d[0];
                int y = head[1] + d[1];
                if (!valid(x, y, '.')) {
                    continue;
                }
                if (dist[x][y] <= dist[head[0]][head[1]] + 1) {
                    continue;
                }
                dist[x][y] = dist[head[0]][head[1]] + 1;
                prev[x][y] = head.clone();
                dq.add(new int[]{x, y});
            }
        }
        int[] border = null;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == 0 || j == 0 || i == 7 || j == 7) {
                    if (border == null || dist[border[0]][border[1]] > dist[i][j]) {
                        border = new int[]{i, j};
                    }
                }
            }
        }
        Deque<int[]> seq = new ArrayDeque<>();
        for (int[] now = border; now != null; now = prev[now[0]][now[1]]) {
            seq.add(now);
        }
        Deque<int[]> clone = new ArrayDeque<>(seq);
        lastX = seq.peekLast()[0];
        lastY = seq.peekLast()[1];
        seq.removeLast();
        while (!seq.isEmpty()) {
            int[] tail = seq.removeLast();
            moveTo(tail[0], tail[1]);
        }
        seq = clone;
        //rotate
        int choice = -1;
        if (border[0] == 0) {
            choice = 3;
        } else if (border[0] == 7) {
            choice = 1;
        } else if (border[1] == 0) {
            choice = 2;
        } else {
            choice = 0;
        }
        while (true) {
            int x = lastX + dirs[choice][0];
            int y = lastY + dirs[choice][1];
            if (!valid(x, y, '.') || (x != 0 && x != 7 && y != 0 && y != 7)) {
                choice++;
                choice %= 4;
                continue;
            }
            if (x == border[0] && y == border[1]) {
                break;
            }
            moveTo(x, y);
        }
        while (!seq.isEmpty()) {
            int[] head = seq.removeFirst();
            moveTo(head[0], head[1]);
        }
        if (mat[lastX][lastY] != 'a') {
            throw new RuntimeException();
        }
        while (true) {
            boolean find = false;
            for (int[] d : dirs) {
                int x = lastX + d[0];
                int y = lastY + d[1];
                if (valid(x, y, (char) (mat[lastX][lastY] + 1))) {
                    find = true;
                    moveTo(x, y);
                    break;
                }
            }
            if (!find) {
                break;
            }
        }

        return output.toString();
    }

    public boolean valid(int x, int y, char c) {
        return !(x < 0 || y < 0 || x >= 8 || y >= 8 || mat[x][y] != c);
    }

    int[][][] prev;
    int lastX;
    int lastY;
    List<int[]> seq = new ArrayList<>();
    StringBuilder output = new StringBuilder();

    public void moveTo(int i, int j) {
        if (Math.abs(lastX - i) + Math.abs(lastY - j) != 1) {
            throw new RuntimeException();
        }
        if (lastX + 1 == i) {
            output.append('S');
        } else if (lastX - 1 == i) {
            output.append('N');
        } else if (lastY + 1 == j) {
            output.append('E');
        } else {
            output.append('W');
        }
        seq.add(new int[]{i, j});
        lastX = i;
        lastY = j;
    }

}
