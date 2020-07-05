package contest;

import template.math.ILongModular;
import template.math.LongModular;
import template.math.LongPower;
import template.math.Power;
import template.primitve.generated.datastructure.LongArrayList;

public class EllysHash {
    public long getHash(int N, String A, String B, String C) {
        int left = N / 2;
        int right = N - left;
        long[] h1 = possible(A.substring(0, left), B.substring(0, left), C.substring(0, left),
                power.pow(127, right));
        long[] h2 = possible(A.substring(left), B.substring(left), C.substring(left), 1);

        int j = 0;
        int i = h1.length;
        long ans = modVal - 1;
        while (i > 0) {
            i--;
            while (j < h2.length && h2[j] + h1[i] < modVal) {
                j++;
            }
            if (j < h2.length) {
                ans = Math.min(ans, (h2[j] + h1[i]) % modVal);
            }
            ans = Math.min(ans, (h2[0] + h1[i]) % modVal);
        }
        return ans;
    }

    public long[] possible(String A, String B, String C, long mul) {
        list.clear();
        build(new char[][]{A.toCharArray(), B.toCharArray(), C.toCharArray()}, 0, 0);

        if (mul != 1) {
            long[] data = list.getData();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                data[i] = mod.mul(data[i], mul);
            }
        }

        list.unique();
        return list.toArray();
    }

    ILongModular mod = ILongModular.getInstance(1000000000000037L);
    LongPower power = new LongPower(mod);
    long modVal = mod.getMod();
    LongArrayList list = new LongArrayList(4782969);

    public long merge(long hash, char c) {
        hash = (hash * 127 + c) % modVal;
        return hash;
    }

    public void build(char[][] ABC, int i, long hash) {
        if (i == ABC[0].length) {
            list.add(hash);
            return;
        }
        for (int j = 0; j < 3; j++) {
            build(ABC, i + 1, merge(hash, ABC[j][i]));
        }
    }
}
