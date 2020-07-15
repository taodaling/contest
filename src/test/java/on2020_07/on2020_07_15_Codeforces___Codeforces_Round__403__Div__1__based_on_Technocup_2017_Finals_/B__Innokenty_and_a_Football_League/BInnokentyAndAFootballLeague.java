package on2020_07.on2020_07_15_Codeforces___Codeforces_Round__403__Div__1__based_on_Technocup_2017_Finals_.B__Innokenty_and_a_Football_League;



import template.graph.BipartiteMatch;
import template.graph.TwoSatBeta;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BInnokentyAndAFootballLeague {
    public int getIdOf(TwoSatBeta tsb, int i, int b) {
        if (b == 0) {
            return tsb.elementId(i);
        } else {
            return tsb.negateElementId(i);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        String[][] names = new String[n][2];
        TwoSatBeta tsb = new TwoSatBeta(n, n * 6);
        for (int i = 0; i < n; i++) {
            String a = in.readString();
            String b = in.readString();
            names[i][0] = a.substring(0, 3);
            names[i][1] = a.substring(0, 2) + b.substring(0, 1);
        }
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = 0; k < 2; k++) {
                    for (int t = 0; t < 2; t++) {
                        if (names[i][k].equals(names[j][t])) {
                            if (k == 0 && t == 0) {
                                tsb.isFalse(tsb.elementId(i));
                                tsb.isFalse(tsb.elementId(j));
                            }
                            tsb.atLeastOneIsFalse(getIdOf(tsb, i, k), getIdOf(tsb, j, t));
                        }
                    }
                }
            }
        }

        if (!tsb.solve(true)) {
            out.println("NO");
            return;
        }
        out.println("YES");
        for (int i = 0; i < n; i++) {
            if (tsb.valueOf(i)) {
                out.println(names[i][0]);
            }else{
                out.println(names[i][1]);
            }
        }
    }
}
