package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.ZAlgorithm;

public class P5410KMPZ {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int limit = (int) 2e7;
        byte[] a = new byte[limit];
        byte[] b = new byte[limit];
        int aLen = in.readString(a, 0);
        int bLen = in.readString(b, 0);

        ZAlgorithm zab = new ZAlgorithm(aLen + bLen + 1, i -> {
            if (i < bLen) {
                return b[i];
            }
            if (i == bLen) {
                return '#';
            }
            return a[i - bLen - 1];
        });
        zab.z[0] = bLen;
        out.println(xor(zab, 0, bLen - 1));
        out.println(xor(zab, bLen + 1, aLen + bLen));
    }

    public long xor(ZAlgorithm za, int l, int r) {
        long xor = 0;
        for (int i = l; i <= r; i++) {
            xor ^= (long)(i + 1 - l) * (za.z[i] + 1);
        }
        return xor;
    }
}
