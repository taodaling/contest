package on2020_07.on2020_07_03_AtCoder___AtCoder_Grand_Contest_011.D___Half_Reflector0;




import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;


public class DHalfReflector {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] vals = new int[n];
        for (int i = 0; i < n; i++) {
            vals[i] = 'B' - in.readChar();
        }

        Data data = new Data(vals);
        data.next(k);
        for (int i = 0; i < n; i++) {
            out.append(data.get(i) == 1 ? 'A' : 'B');
        }
    }

    public void output(BitSet bs, FastOutput out) {
        for (int i = 0; i < bs.capacity(); i++) {
            out.append(bs.get(i) ? 'A' : 'B');
        }
    }

}

class Data {
    public Data(int[] data) {
        this.data = data;
        start = (data.length - 1) % 2 == 0 ? 1 : 0;
    }

    int[] data;
    int index;
    int start = 1;
    boolean inv;

    public int get(int i) {
        int threshold = data.length - index;
        if (i < threshold) {
            if (inv) {
                return 1 - data[index + i];
            } else {
                return data[index + i];
            }
        }
        if (i == 0) {
            return start;
        }
        if (i % 2 == (data.length - 1) % 2) {
            return 1;
        }
        return 0;
    }

    public void next(int k) {
        if (k == 0) {
            return;
        }
        if (index >= data.length) {
            if (k % 2 == 1) {
                start = 0;
            }
            return;
        }
        if (data[index] == 1) {
            data[index] = 0;
            next(k - 1);
            return;
        }
        index++;
        inv = !inv;
        next(k - 1);
    }
}