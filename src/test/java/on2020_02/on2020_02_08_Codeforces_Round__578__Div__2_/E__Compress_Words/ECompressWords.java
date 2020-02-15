package on2020_02.on2020_02_08_Codeforces_Round__578__Div__2_.E__Compress_Words;



import template.io.FastInput;
import template.io.FastOutput;
import template.rand.AppendableHash;

public class ECompressWords {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] buf = new char[1000000];
        AppendableHash h31 = new AppendableHash(1000000, 31);
        AppendableHash h11 = new AppendableHash(1000000, 11);
        AppendableHash new31 = new AppendableHash(h31, 1000000);
        AppendableHash new11 = new AppendableHash(h11, 1000000);
        for (int i = 0; i < n; i++) {
            int len = in.readString(buf, 0);
            new31.clear();
            new11.clear();
            for (int j = 0; j < len; j++) {
                new31.append(buf[j]);
                new11.append(buf[j]);
            }

            int repeat;
            int whole = h31.size();
            for (repeat = Math.min(len, whole);
                 repeat > 0; repeat--) {
                int l = whole - repeat;
                int r = whole - 1;

                int ll = 0;
                int rr = repeat - 1;
                if (new31.hash(ll, rr) ==
                        h31.hash(l, r) &&
                        new11.hash(ll, rr) ==
                                h11.hash(l, r)) {
                    break;
                }
            }

            for (int j = repeat; j < len; j++) {
                h31.append(buf[j]);
                h11.append(buf[j]);
                out.append(buf[j]);
            }
        }
    }
}

