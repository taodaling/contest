package on2020_09.on2020_09_14_Codeforces___Codeforces_Round__359__Div__1_.D__Kay_and_Eternity;



import template.datastructure.Range2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerDiscreteMap;

import java.util.Arrays;

public class DKayAndEternity {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        Cell[] cells = new Cell[n];
        long[] cnts = new long[n + 1];
        long[] ans = new long[n + 1];

        int inf = (int) 1e9 + 500;
        IntegerArrayList xs = new IntegerArrayList(n + 2);
        xs.add(-inf);
        xs.add(inf);
        IntegerArrayList ys = new IntegerArrayList(n);
        for (int i = 0; i < n; i++) {
            cells[i] = new Cell();
            cells[i].x = in.readInt();
            cells[i].y = in.readInt();
            xs.add(cells[i].x);
            ys.add(cells[i].y);
        }

        Arrays.sort(cells, (a, b) -> Integer.compare(a.y, b.y));
        IntegerDiscreteMap xsDM = new IntegerDiscreteMap(xs.getData(), 0, xs.size());
        int[] xVal = new int[xsDM.maxRank() + 1];
        for (int i = 0; i <= xsDM.maxRank(); i++) {
            xVal[i] = xsDM.iThElement(i);
        }

        int size = 0;
        int[] row = new int[xsDM.maxRank() + 1];
        SimplifiedDeque<Cell> addDq = new Range2DequeAdapter<>(i -> cells[i], 0, cells.length - 1);
        SimplifiedDeque<Cell> removeDq = new Range2DequeAdapter<>(i -> cells[i], 0, cells.length - 1);
        for (int i = -inf; i < inf; i++) {
            while (!removeDq.isEmpty() && removeDq.peekFirst().y < i) {
                Cell head = removeDq.removeFirst();
                int xRank = xsDM.rankOf(head.x);
                int l = xRank;
                int r = xRank;
                int cur = row[xRank];

                while (xVal[r] - xVal[l - 1] + 1 <= k) {
                    cur += row[l - 1];
                    l--;
                }
                for (int j = xVal[xRank] - k + 1; j <= xVal[xRank]; j++) {
                    while (xVal[r + 1] - j + 1 <= k) {
                        r++;
                        cur += row[r];
                    }
                    while (xVal[l] < j) {
                        cur -= row[l];
                        l++;
                    }
                    cnts[cur]--;
                    if(cur > 0) {
                        cnts[cur - 1]++;
                    }
                }

                row[xRank]--;
                size--;
            }
            while (!addDq.isEmpty() && addDq.peekFirst().y < i + k) {
                Cell head = addDq.removeFirst();
                int xRank = xsDM.rankOf(head.x);
                int l = xRank;
                int r = xRank;
                int cur = row[xRank];

                while (xVal[r] - xVal[l - 1] + 1 <= k) {
                    cur += row[l - 1];
                    l--;
                }
                for (int j = xVal[xRank] - k + 1; j <= xVal[xRank]; j++) {
                    while (xVal[r + 1] - j + 1 <= k) {
                        r++;
                        cur += row[r];
                    }
                    while (xVal[l] < j) {
                        cur -= row[l];
                        l++;
                    }
                    cnts[cur]--;
                    cnts[cur + 1]++;
                }

                row[xRank]++;
                size++;
            }


            if (size == 0) {
                int next = inf;
                if (!addDq.isEmpty()) {
                    next = addDq.peekFirst().y - k + 1;
                }
                i = next - 1;
                continue;
            }

            for (int j = 1; j <= size; j++) {
                ans[j] += cnts[j];
            }
        }

        for(int i = 1; i <= n; i++){
            out.println(ans[i]);
        }
    }
}

class Cell {
    int x;
    int y;

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }
}
