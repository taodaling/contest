package on2021_05.on2021_05_30_Codeforces___Deltix_Round__Spring_2021__open_for_everyone__rated__Div__1___Div__2_.A__Game_of_Life;



import template.io.FastInput;
import template.io.FastOutput;

public class AGameOfLife {
    public int get(int[] cell, int i) {
        if (i < 0 || i >= cell.length) {
            return 0;
        }
        return cell[i];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] cells = new int[n];
        for (int i = 0; i < n; i++) {
            cells[i] = in.rc() - '0';
        }
        int[] prev = cells;
        int[] next = new int[n];
        for (int round = 0; round < m; round++) {
            boolean active = false;
            for (int i = 0; i < n; i++) {
                next[i] = prev[i];
                if (prev[i] == 0 && get(prev, i - 1) + get(prev, i + 1) == 1) {
                    next[i] = 1;
                    active = true;
                }
            }
            if (!active) {
                break;
            }
            int[] tmp = prev;
            prev = next;
            next = tmp;
        }
        for (int x : prev) {
            out.append(x);
        }
        out.println();
    }
}
