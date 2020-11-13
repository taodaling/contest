package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class FBracketSequencing {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] buf = new char[(int) 1e6];

        List<Seq> pos = new ArrayList<>(n);
        List<Seq> neg = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            int m = in.readString(buf, 0);
            Seq seq = new Seq();
            for (int j = 0; j < m; j++) {
                if (buf[j] == '(') {
                    seq.contrib++;
                } else {
                    seq.contrib--;
                }
                seq.req = Math.max(seq.req, -seq.contrib);
            }
            if (seq.contrib >= 0) {
                pos.add(seq);
            } else {
                neg.add(seq);
            }
        }
        pos.sort((a, b) -> Integer.compare(a.req, b.req));
        neg.sort((a, b) -> -Integer.compare(a.req + a.contrib, b.req + b.contrib));
        int sum = 0;
        for (Seq seq : pos) {
            if (sum < seq.req) {
                out.println("No");
                return;
            }
            sum += seq.contrib;
        }
        for (Seq seq : neg) {
            if (sum < seq.req) {
                out.println("No");
                return;
            }
            sum += seq.contrib;
        }
        if(sum != 0){
            out.println("No");
            return;
        }
        out.println("Yes");
    }
}

class Seq {
    int req;
    int contrib;
}
