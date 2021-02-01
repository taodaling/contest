package on2021_02.on2021_02_01_Codeforces___Codeforces_Round__313__Div__1_.C__Gerald_and_Giant_Chess;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CGeraldAndGiantChess {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int h = in.ri();
        int w = in.ri();
        int n = in.ri();
        int mod = (int) 1e9 + 7;
        Combination comb = new Combination((int) 5e5, mod);

        List<Pt> list = new ArrayList<>(n + 2);
        list.add(new Pt(0, 0));
        list.add(new Pt(h - 1, w - 1));
        for (int i = 0; i < n; i++) {
            list.add(new Pt(in.ri() - 1, in.ri() - 1));
        }
        list.sort(Comparator.<Pt>comparingInt(x -> x.x).thenComparingInt(x -> x.y));
        for (int i = 0; i < list.size(); i++) {
            Pt pt = list.get(i);
            if (i == 0) {
                pt.way = -1;
                continue;
            }
            for (int j = 0; j < i; j++) {
                Pt pre = list.get(j);
                if (pre.x <= pt.x && pre.y <= pt.y) {
                    pt.way += -pre.way * comb.combination(pt.x - pre.x + pt.y - pre.y, pt.x - pre.x) % mod;
                }
            }
            pt.way %= mod;
        }
        long ans = list.get(list.size() - 1).way;
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}

class Pt {
    int x;
    int y;
    long way;

    public Pt(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
