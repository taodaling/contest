package on2020_05.on2020_05_30_.BitSetTest0;




import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;

public class BitSetTest {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        BitSet[] seq = new BitSet[n + 1];
        seq[0] = new BitSet(m);
        for (int i = 1; i <= n; i++) {
            int t = in.readInt();
            seq[i] = seq[i - 1].clone();
            if (t == 0) {
                //set one
                int l = in.readInt();
                seq[i].set(l);
            } else if (t == 1) {
                //set bulk
                int l = in.readInt();
                int r = in.readInt();
                seq[i].set(l, r);
            } else if (t == 2) {
                //clear
                int l = in.readInt();
                seq[i].clear(l);
            } else if (t == 3) {
                //clear bulk
                int l = in.readInt();
                int r = in.readInt();
                seq[i].clear(l, r);
            } else if (t == 4) {
                //flip
                int l = in.readInt();
                seq[i].flip(l);
            } else if (t == 5) {
                //flip bulk
                int l = in.readInt();
                int r = in.readInt();
                seq[i].flip(l, r);
            } else if (t == 6) {
                //and
                int prev = in.readInt();
                seq[i].and(seq[prev]);
            } else if (t == 7) {
                //or
                int prev = in.readInt();
                seq[i].or(seq[prev]);
            } else if (t == 8) {
                //xor
                int prev = in.readInt();
                seq[i].xor(seq[prev]);
            } else if (t == 9) {
                //left shift
                int step = in.readInt();
                seq[i].leftShift(step);
            } else if (t == 10) {
                //right shift
                int step = in.readInt();
                seq[i].rightShift(step);
            } else if (t == 11) {
                int l = in.readInt();
                int r = in.readInt();
                output(out, seq[i].interval(l, r), 0, r - l);
            } else if (t == 12) {
                int l = in.readInt();
                int r = in.readInt();
                out.println(seq[i].size(l, r));
            } else if (t == 13) {
                int index = in.readInt();
                int prevSet = seq[i].prevSetBit(index);
                int prevClear = seq[i].prevClearBit(index);
                out.append(prevSet).append(' ').append(prevClear).println();
            }
            output(out, seq[i], 0, m - 1);
        }
    }


    public void output(FastOutput out, BitSet bs, int l, int r) {
        out.append(bs.size()).append(' ');
        for (int i = l; i <= r; i++) {
            if (bs.get(i)) {
                out.append(i).append(' ');
            }
        }
        out.println();
    }
}
