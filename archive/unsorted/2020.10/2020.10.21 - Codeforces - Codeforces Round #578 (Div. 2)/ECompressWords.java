package contest;

import template.io.FastInput;
import template.primitve.generated.datastructure.LongHashMap;
import template.rand.*;
import template.utils.Debug;

import java.io.PrintWriter;

public class ECompressWords {
    char[] s = new char[(int) 1e6];
    char[] cur = new char[s.length];
    int mod = (int) 1e9 + 7;
    LongHashData hd1 = new LongHashData(s.length);
    LongRangeHash hash1 = new LongRangeHash(hd1, s.length);
    LongRangeHash hash2 = new LongRangeHash(hd1, s.length);

    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int m = 0;
        for (int i = 0; i < n; i++) {
            int len = in.readString(s, 0);
            int commonLen = Math.min(len, m);
            hash1.populate(j -> s[j], commonLen);
            int finalM = m;
            hash2.populate(j -> cur[finalM - commonLen + j], commonLen);
            int largest = commonLen - 1;
            while (largest >= 0 && hash2.hash(commonLen - 1 - largest, commonLen - 1) != hash1.hash(0, largest)) {
                largest--;
            }
            largest++;

            debug.debug("common", largest);
            //debug.debug("prefix", String.valueOf(cur, 0, m));
            for (int j = largest; j < len; j++) {
                cur[m++] = s[j];
            }
        }
        out.println(String.valueOf(cur, 0, m));
    }
}
