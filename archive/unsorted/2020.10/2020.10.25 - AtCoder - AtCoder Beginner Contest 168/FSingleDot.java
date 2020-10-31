package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.LongDeque;
import template.primitve.generated.datastructure.LongDequeImpl;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class FSingleDot {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.readInt();
        int n = in.readInt();
        IntegerArrayList list = new IntegerArrayList((n + m) * 3 + 1);
        int[][] vLines = new int[m][3];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 3; j++) {
                vLines[i][j] = in.readInt();
                list.add(vLines[i][j]);
            }
            if (vLines[i][0] > vLines[i][1]) {
                SequenceUtils.swap(vLines[i], 0, 1);
            }
        }

        int[][] hLines = new int[n][3];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                hLines[i][j] = in.readInt();
                list.add(hLines[i][j]);
            }
            SequenceUtils.swap(hLines[i], 0, 2);
            if (hLines[i][0] > hLines[i][1]) {
                SequenceUtils.swap(hLines[i], 0, 1);
            }
        }


        list.add(0);
        list.unique();

        for (int[] l : hLines) {
            for (int i = 0; i < 3; i++) {
                l[i] = list.binarySearch(l[i]);
            }
        }
        for (int[] l : vLines) {
            for (int i = 0; i < 3; i++) {
                l[i] = list.binarySearch(l[i]);
            }
        }
        int k = list.size();
        boolean[][] bottom = new boolean[k][k];
        boolean[][] left = new boolean[k][k];
        boolean[][] visited = new boolean[k][k];
        for (int[] line : hLines) {
            int l = line[0];
            int r = line[1];
            int y = line[2];
            debug.debug("y", y);
            for (int i = l; i < r; i++) {
                bottom[y][i] = true;
            }
        }
        for (int[] line : vLines) {
            int b = line[0];
            int t = line[1];
            int x = line[2];
            for (int i = b; i < t; i++) {
                left[i][x] = true;
            }
        }

        int zero = list.binarySearch(0);
        debug.run(() -> {
            debug.debug("g", paint(left, bottom, zero, zero));
        });
        visited[zero][zero] = true;
        LongDeque dq = new LongDequeImpl(k * k);
        dq.addLast(DigitUtils.asLong(zero, zero));
        while (!dq.isEmpty()) {
            int y = DigitUtils.highBit(dq.peekFirst());
            int x = DigitUtils.lowBit(dq.removeFirst());
            if (y - 1 >= 0 && !bottom[y][x] && !visited[y - 1][x]) {
                visited[y - 1][x] = true;
                dq.addLast(DigitUtils.asLong(y - 1, x));
            }
            if (y + 1 < k && !bottom[y + 1][x] && !visited[y + 1][x]) {
                visited[y + 1][x] = true;
                dq.addLast(DigitUtils.asLong(y + 1, x));
            }
            if (x - 1 >= 0 && !left[y][x] && !visited[y][x - 1]) {
                visited[y][x - 1] = true;
                dq.addLast(DigitUtils.asLong(y, x - 1));
            }
            if (x + 1 < k && !left[y][x + 1] && !visited[y][x + 1]) {
                visited[y][x + 1] = true;
                dq.addLast(DigitUtils.asLong(y, x + 1));
            }
        }
        for (int i = 0; i < k; i++) {
            if (!bottom[0][i] && visited[0][i] || visited[k - 1][i] ||
                    !left[i][0] && visited[i][0] || visited[i][k - 1]) {
                out.println("INF");
                return;
            }
        }

        long area = 0;
        for (int i = 0; i < k - 1; i++) {
            for (int j = 0; j < k - 1; j++) {
                if (!visited[i][j]) {
                    continue;
                }
                long l = list.get(i);
                long r = list.get(i + 1);
                long b = list.get(j);
                long t = list.get(j + 1);
                area += (r - l) * (t - b);
            }
        }

        out.println(area);
    }

    public String paint(boolean[][] left, boolean[][] bot, int x, int y) {
        StringBuilder builder = new StringBuilder("\n");
        int n = left.length;
        int m = left[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                builder.append(" ");
                builder.append(bot[i][j] ? "-" : " ");
            }
            builder.append("\n");
            for (int j = 0; j < m; j++) {
                builder.append(left[i][j] ? "|" : " ");
                builder.append(i == x && j == y ? "*" : ".");
            }
            builder.append("\n");

        }
        return builder.toString();
    }
}

