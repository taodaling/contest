package on2020_04.on2020_04_11_Round_1A_2020___Code_Jam_2020.Pascal_Walk;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class PascalWalk {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        out.printf("Case #%d:", testNumber).println();
        if (n <= 500) {
            for (int i = 0; i < n; i++) {
                out.append(i + 1).append(' ').append(1).println();
            }
            return;
        }
        //i = 29
        List<int[]> seq = new ArrayList<>(500);
        int m = n - 30;
        boolean right = false;
        for (int i = 0; i <= 29; i++) {
            if (Bits.bitAt(m, i) == 0) {
                if (right) {
                    seq.add(new int[]{i, i});
                } else {
                    seq.add(new int[]{i, 0});
                }
            } else {
                if (right) {
                    for (int j = i; j >= 0; j--) {
                        seq.add(new int[]{i, j});
                    }
                } else {
                    for (int j = 0; j <= i; j++) {
                        seq.add(new int[]{i, j});
                    }
                }
                right = !right;
            }
        }

        for (int i = 1, until = Integer.bitCount(m); i <= until; i++) {
            if (right) {
                seq.add(new int[]{29 + i, 29 + i});
            } else {
                seq.add(new int[]{29 + i, 0});
            }
        }

        for (int[] s : seq) {
            out.append(s[0] + 1).append(' ').append(s[1] + 1).println();
        }
    }
}
