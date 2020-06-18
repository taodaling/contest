package on2020_06.on2020_06_18_Codeforces___Codeforces_Global_Round_8.C__Even_Picture;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class CEvenPicture {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        List<int[]> seq = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int x = i * 2;
            seq.add(new int[]{x, 0});
            seq.add(new int[]{x, 1});
            seq.add(new int[]{x, -1});
            seq.add(new int[]{x + 1, 0});
            if (i == 0) {
                seq.add(new int[]{x - 1, 0});
                seq.add(new int[]{x - 1, -1});
            }

            if (i > 0) {
                int y = i % 2 == 1 ? 2 : -2;
                for (int j = x - 2; j <= x; j++) {
                    seq.add(new int[]{j, y});
                }
            }

            if (i == n - 1) {
                int y = i % 2 == 1 ? -1 : 1;
                seq.add(new int[]{x + 1, y});
            }
        }

        out.println(seq.size());
        for (int[] pt : seq) {
            out.append(pt[0]).append(' ').append(pt[1]).println();
        }

        //draw(seq);
    }

    public void draw(List<int[]> seq) {
        int l = -1;
        int r = seq.get(seq.size() - 1)[0];
        char[][] mat = new char[5][r - l + 1];
        SequenceUtils.deepFill(mat, '.');
        for (int[] xy : seq) {
            int x = xy[0] + 1;
            int y = xy[1] + 2;
            mat[y][x] = '#';
        }
        for (char[] row : mat) {
            for (char c : row) {
                System.err.append((char) c);
            }
            System.err.println();
        }
    }
}
