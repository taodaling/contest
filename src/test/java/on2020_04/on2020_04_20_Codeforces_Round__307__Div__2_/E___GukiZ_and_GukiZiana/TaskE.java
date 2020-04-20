package on2020_04.on2020_04_20_Codeforces_Round__307__Div__2_.E___GukiZ_and_GukiZiana;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.CompareUtils;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        int[] data = new int[n];
        int[] indexes = new int[n];
        int[] buf = indexes.clone();
        for (int i = 0; i < n; i++) {
            data[i] = in.readInt();
            indexes[i] = i;
        }
        int B = 1000;
        int blockCnt = DigitUtils.ceilDiv(n, B);
        Block[] blocks = new Block[blockCnt];
        for (int i = 0; i < blockCnt; i++) {
            blocks[i] = new Block(data, indexes, buf, i * B, Math.min(n - 1, (i + 1) * B - 1));
        }

        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            if (t == 1) {
                int l = in.readInt() - 1;
                int r = in.readInt() - 1;
                int x = in.readInt();
                for (Block b : blocks) {
                    b.update(l, r, x);
                }
            } else {
                int y = in.readInt();
                int left = -1;
                int right = -2;
                for (int j = 0; j < blockCnt; j++) {
                    if (blocks[j].queryLeft(y)) {
                        left = blocks[j].queryLeftBF(y);
                        break;
                    }
                }

                for (int j = blockCnt - 1; j >= 0; j--) {
                    if (blocks[j].queryRight(y)) {
                        right = blocks[j].queryRightBF(y);
                        break;
                    }
                }

                out.println(right - left);
            }
        }
    }
}

class Block {
    int[] data;
    int[] indexes;
    int[] buf;

    int l;
    int r;
    long tag = 0;
    static int inf = (int) (1e9 + 10);

    public Block(int[] data, int[] indexes, int[] buf, int l, int r) {
        this.data = data;
        this.indexes = indexes;
        this.buf = buf;
        this.l = l;
        this.r = r;
        CompareUtils.radixSort(indexes, l, r, k -> data[k]);
    }

    public void update(int ll, int rr, int x) {
        ll = Math.max(l, ll);
        rr = Math.min(r, rr);
        if (ll == l && rr == r) {
            tag = Math.min(inf, tag + x);
            return;
        }
        if (ll > rr) {
            return;
        }
        for (int i = ll; i <= rr; i++) {
            data[i] = Math.min(inf, data[i] + x);
        }

        CompareUtils.radixSort(indexes, l, r, k -> data[k]);
    }

    public boolean queryLeft(int x) {
        x -= tag;
        final int finalX = x;
        int left = SequenceUtils.upperBound(indexes, l, r, i -> Integer.compare(data[i], finalX));
        return left <= r && data[indexes[left]] == x;
    }

    public int queryLeftBF(long x) {
        int ans = -1;
        x -= tag;
        for (int i = l; i <= r; i++) {
            if (data[i] == x) {
                ans = i;
                break;
            }
        }
        return ans;
    }

    public int queryRightBF(long x) {
        int ans = -1;
        x -= tag;
        for (int i = r; i >= l; i--) {
            if (data[i] == x) {
                ans = i;
                break;
            }
        }
        return ans;
    }

    public boolean queryRight(int x) {
        x -= tag;
        final int finalX = x;
        int right = SequenceUtils.lowerBound(indexes, l, r, i -> Integer.compare(data[i], finalX));
        return right >= l && data[indexes[right]] == x;
    }

    @Override
    public String toString() {
        int[] copy = Arrays.copyOfRange(data, l, r + 1);
        for (int i = 0; i < copy.length; i++) {
            copy[i] += tag;
        }
        return Arrays.toString(copy);
    }
}
