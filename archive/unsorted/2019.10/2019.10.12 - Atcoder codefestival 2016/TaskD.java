package contest;

import java.io.PrintWriter;
import java.util.Arrays;

import template.FastInput;

public class TaskD {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int r = in.readInt();
        int c = in.readInt();
        int n = in.readInt();
        int[][] rcas = new int[n][3];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                rcas[i][j] = in.readInt();
            }
        }

        out.println(check(r, c, rcas) ? "Yes" : "No");
    }


    public boolean check(int r, int c, int[][] rcas) {
        int n = rcas.length;
        Arrays.sort(rcas, (a, b) -> a[0] == b[0] ? a[1] - b[1] : a[0] - b[0]);

        Node[] cols = new Node[c + 1];
        for (int i = 1; i <= c; i++) {
            cols[i] = new Node();
        }

        for (int i = 1; i < n; i++) {
            int ci = rcas[i][1];

            for (int j = i - 1; j >= 0 && rcas[i][0] == rcas[j][0]; j--) {
                int cj = rcas[j][1];
                if (cols[ci].find() == cols[cj].find()) {
                    if (cols[ci].diff - cols[cj].diff != rcas[i][2] - rcas[j][2]) {
                        return false;
                    }
                } else {
                    Node.merge(cols[cj], cols[ci], rcas[i][2] - rcas[j][2]);
                }
                break;
            }
        }

        Arrays.sort(rcas, (a, b) -> a[1] == b[1] ? a[0] - b[0] : a[1] - b[1]);
        Node[] rows = new Node[r + 1];
        for (int i = 1; i <= r; i++) {
            rows[i] = new Node();
        }

        for (int i = 1; i < rcas.length; i++) {
            int ri = rcas[i][0];

            for (int j = i - 1; j >= 0 && rcas[i][1] == rcas[j][1]; j--) {
                int rj = rcas[j][0];
                if (rows[ri].find() == rows[rj].find()) {
                    if (rows[ri].diff - rows[rj].diff != rcas[i][2] - rcas[j][2]) {
                        return false;
                    }
                } else {
                    Node.merge(rows[rj], rows[ri], rcas[i][2] - rcas[j][2]);
                }
                break;
            }
        }

        for (int i = 0; i < n; i++) {
            int rr = rcas[i][0];
            int cc = rcas[i][1];
            long v = rcas[i][2];

            Node minRow = rows[rr].find().min;
            v = v - rows[rr].diff + minRow.diff;
            Node minCol = cols[cc].find().min;
            v = v - cols[cc].diff + minCol.diff;
            if (v < 0) {
                return false;
            }
        }

        return true;
    }
}


class Node {
    Node p = this;
    int rank;
    long diff;
    Node min = this;

    public Node find() {
        if (p.p == p) {
            return p;
        }
        p.find();
        diff += p.diff;
        p = p.find();
        return p;
    }

    public static Node min(Node a, Node b) {
        a.find();
        b.find();
        return a.diff <= b.diff ? a : b;
    }

    /**
     * b = a + d
     */
    public static void merge(Node a, Node b, long d) {
        a.find();
        b.find();
        // Rb + Db = Ra + Da + d
        // Rb = Ra + Da + d - Db
        d = a.diff + d - b.diff;
        a = a.find();
        b = b.find();
        if (a == b) {
            return;
        }
        if (a.rank == b.rank) {
            a.rank++;
        }
        if (a.rank > b.rank) {
            b.p = a;
            b.diff = d;
            a.min = min(a.min, b.min);

        } else {
            a.p = b;
            a.diff = -d;
            b.min = min(a.min, b.min);
        }
    }
}
