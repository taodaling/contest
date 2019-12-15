package on2019_12.on2019_12_14_Codeforces_Round__606__Div__1__based_on_Technocup_2020_Elimination_Round_4_.C__Beautiful_Rectangle;



import template.datastructure.Array2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.*;

public class CBeautifulRectangle {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Map<Integer, Cnt> map = new HashMap<>(n);
        for (int i = 1; i <= n; i++) {
            int a = in.readInt();
            Cnt cnt = map.get(a);
            if (cnt == null) {
                cnt = new Cnt();
                cnt.c = a;
                cnt.cnt = 0;
                map.put(a, cnt);
            }
            cnt.cnt++;
        }

        Cnt[] cnts = map.values().toArray(new Cnt[0]);
        Arrays.sort(cnts, (a, b) -> a.cnt - b.cnt);
        SimplifiedDeque<Cnt> deque = new Array2DequeAdapter<>(cnts);
        int sum = n;
        int exceed = 0;
        int mxRow = 0;
        int mxCol = 0;
        for (int i = n; i >= 1; i--) {
            while (!deque.isEmpty() && deque.peekLast().cnt > i) {
                sum -= deque.removeLast().cnt;
                exceed++;
            }
            int total = sum + i * exceed;
            int col = total / i;
            if (col < i) {
                continue;
            }
            if (mxRow * mxCol < i * col) {
                mxRow = i;
                mxCol = col;
            }
        }

        out.println(mxRow * mxCol);
        out.append(mxRow).append(' ').append(mxCol).append('\n');

        int[][] mat = new int[mxRow][mxCol];
        for (int i = 0; i < cnts.length; i++) {
            cnts[i].cnt = Math.min(cnts[i].cnt, mxRow);
        }
        deque = new Array2DequeAdapter<>(cnts);
        for (int i = 0; i < mxCol; i++) {
            for (int j = 0; j < mxRow; j++) {
                while (deque.peekLast().cnt == 0) {
                    deque.removeLast();
                }
                mat[j][i] = deque.peekLast().c;
                deque.peekLast().cnt--;
            }
        }

        for (int i = 0; i < mxRow; i++) {
            SequenceUtils.rotate(mat[i], 0, (mxCol - i) % mxCol, mxCol - 1);
        }

        for (int i = 0; i < mxRow; i++) {
            for (int j = 0; j < mxCol; j++) {
                out.append(mat[i][j]).append(' ');
            }
            out.println();
        }
    }
}

class Cnt {
    int c;
    int cnt;
}
