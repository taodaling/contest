package on2020_01.on2020_01_12_Mail_Ru_Cup_2018_Round_1.E__Chips_Puzzle;



import javafx.scene.SnapshotResult;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class EChipsPuzzle {
    List<int[]> seq = new ArrayList<>(400000);

    public void add(int... pts) {
        seq.add(pts);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();


        String[][] now = new String[n][m];
        String[][] target = new String[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                now[i][j] = in.readString();
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                target[i][j] = in.readString();
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < now[i][i].length(); j++) {
                add(i, i, i, 1 - i);
            }
            now[i][1 - i] = now[i][i] + now[i][1 - i];
            now[i][i] = "";
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (i == j) {
                    continue;
                }
                for (int k = now[i][j].length() - 1; k >= 0; k--) {
                    int c = now[i][j].charAt(k) - '0';
                    add(i, j, c, c);
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (i < 2 && j < 2) {
                    continue;
                }
                for (int k = now[i][j].length() - 1; k >= 0; k--) {
                    int c = now[i][j].charAt(k) - '0';
                    if (i < 2) {
                        add(i, j, i, 1 - i);
                        add(i, 1 - i, c, c);
                    } else if (j < 2) {
                        add(i, j, 1 - j, j);
                        add(1 - j, j, c, c);
                    } else {
                        add(i, j, c, j);
                        add(c, j, c, c);
                    }
                }
            }
        }

        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < m; j++) {
                if (i < 2 && j < 2) {
                    continue;
                }
                for (int k = target[i][j].length() - 1; k >= 0; k--) {
                    int c = target[i][j].charAt(k) - '0';
                    if (i < 2) {
                        add(c, c, i, 1 - i);
                        add(i, 1 - i, i, j);
                    } else if (j < 2) {
                        add(c, c, 1 - j, j);
                        add(1 - j, j, i, j);
                    } else {
                        add(c, c, c, j);
                        add(c, j, i, j);
                    }
                }
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int k = target[i][i].length() - 1; k >= 0; k--) {
                int c = target[i][i].charAt(k) - '0';
                add(c, c, i, 1 - i);
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (i == j) {
                    continue;
                }
                for (int k = target[i][j].length() - 1; k >= 0; k--) {
                    int c = target[i][j].charAt(k) - '0';
                    ;
                    add(c, c, i, j);
                }
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int k = target[i][i].length() - 1; k >= 0; k--) {
                add(i, 1 - i, i, i);
            }
        }

        out.println(seq.size());
        for (int[] task : seq) {
            for (int x : task) {
                out.append(x + 1).append(' ');
            }
            out.println();
        }
    }
}
