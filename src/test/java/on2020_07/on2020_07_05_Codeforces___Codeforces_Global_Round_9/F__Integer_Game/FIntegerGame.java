package on2020_07.on2020_07_05_Codeforces___Codeforces_Global_Round_9.F__Integer_Game;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class FIntegerGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long a = in.readLong();
        long b = in.readLong();
        long c = in.readLong();
        Ele[] data = new Ele[]{new Ele(false, a, 0), new Ele(false, b, 1), new Ele(false, c, 2)};
        Arrays.sort(data);

        out.println("First").flush();
        long x = 2 * data[2].val - data[1].val - data[0].val;
        out.println(x).flush();
        int index = in.readInt();
        data = tryAdd(data, index - 1, x);
        if (data[2].val - data[1].val != data[1].val - data[0].val) {
            x = 2 * data[2].val - data[1].val - data[0].val;
            out.println(x).flush();
            index = in.readInt();
            data = tryAdd(data, index - 1, x);
        }
        out.println(data[2].val - data[1].val).flush();
        index = in.readInt();
        if(index != 0){
            throw new RuntimeException();
        }
    }

    public Ele[] tryAdd(Ele[] data, int i, long x) {
        data = data.clone();
        for (int j = 0; j < data.length; j++) {
            data[j] = data[j].clone();
            data[j].forbiden = false;
            if (data[j].id == i) {
                data[j].forbiden = true;
                data[j].val += x;
            }
        }
        Arrays.sort(data);
        return data;
    }
}

class Ele implements Cloneable, Comparable<Ele> {
    boolean forbiden;
    long val;
    int id;

    @Override
    public int compareTo(Ele o) {
        return Long.compare(val, o.val);
    }

    public Ele(boolean forbiden, long val, int id) {
        this.id = id;
        this.forbiden = forbiden;
        this.val = val;
    }

    @Override
    public Ele clone() {
        try {
            return (Ele) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
