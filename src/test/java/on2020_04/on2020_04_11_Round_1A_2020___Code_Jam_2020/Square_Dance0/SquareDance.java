package on2020_04.on2020_04_11_Round_1A_2020___Code_Jam_2020.Square_Dance0;




import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class SquareDance {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        out.printf("Case #%d: ", testNumber);

        int r = in.readInt();
        int c = in.readInt();

        long sum = 0;
        Dancer[][] dancers = new Dancer[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                dancers[i][j] = new Dancer();
                dancers[i][j].id = i * c + j;
                dancers[i][j].skill = in.readInt();
                sum += dancers[i][j].skill;
            }
        }
        int[][] ds = new int[][]{
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                for (int k = 0; k < 4; k++) {
                    int[] d = ds[k];
                    int ni = i + d[0];
                    int nj = j + d[1];
                    if (ni < 0 || ni >= r || nj < 0 || nj >= c) {
                        continue;
                    }
                    dancers[i][j].next[k] = dancers[ni][nj];
                }
            }
        }

        TreeSet<Dancer> set = new TreeSet<>((a, b) -> {
            int ans = a.weight() - b.weight();
            if (ans == 0) {
                ans = a.id - b.id;
            }
            return ans;
        });
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                set.add(dancers[i][j]);
            }
        }

        long ans = sum;
        List<Dancer> wait = new ArrayList<>(r * c);
        List<Dancer> insert = new ArrayList<>(r * c * 4);
        while (!set.isEmpty() && set.first().weight() < 0) {
            wait.clear();
            insert.clear();
            while (!set.isEmpty() && set.first().weight() < 0) {
                wait.add(set.pollFirst());
            }
            for (Dancer dancer : wait) {
                for (int i = 0; i < 4; i++) {
                    if (dancer.next[i] != null) {
                        insert.add(dancer.next[i]);
                    }
                }
            }
            set.removeAll(insert);
            for (Dancer dancer : wait) {
                dancer.killed = true;
                sum -= dancer.skill;
            }
            for (Dancer dancer : insert) {
                if (dancer.killed) {
                    continue;
                }
                set.add(dancer);
            }

            ans += sum;
        }

        out.println(ans);
    }
}

class Dancer {
    int skill;
    boolean killed;

    Dancer[] next = new Dancer[4];

    int id;

    public int weight() {
        int a = 0;
        int b = 0;
        for (int i = 0; i < 4; i++) {
            while (next[i] != null && next[i].killed) {
                next[i] = next[i].next[i];
            }
            if (next[i] != null) {
                a += skill;
                b += next[i].skill;
            }
        }


        return a - b;
    }
}