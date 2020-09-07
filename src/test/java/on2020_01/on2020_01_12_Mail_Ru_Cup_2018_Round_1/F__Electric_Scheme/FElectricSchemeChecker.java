package on2020_01.on2020_01_12_Mail_Ru_Cup_2018_Round_1.F__Electric_Scheme;





import net.egork.chelper.tester.StringInputStream;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.checkers.Checker;
import template.io.FastInput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FElectricSchemeChecker implements Checker {
    public FElectricSchemeChecker(String parameters) {
    }

    public Verdict check(String input, String expectedOutput, String actualOutput) {
        FastInput stdin = new FastInput(new StringInputStream(input));
        FastInput prog = new FastInput(new StringInputStream(actualOutput));
        List<int[]> pts = new ArrayList<>();
        int n = stdin.readInt();
        for (int i = 0; i < n; i++) {
            pts.add(new int[]{stdin.readInt(), stdin.readInt()});
        }

        List<X> xs = new ArrayList<>();
        List<Y> ys = new ArrayList<>();
        int h = prog.readInt();
        for (int i = 0; i < h; i++) {
            int x1 = prog.readInt();
            int y1 = prog.readInt();
            int x2 = prog.readInt();
            int y2 = prog.readInt();
            xs.add(new X(Math.min(x1, x2), Math.max(x1, x2), y1));
            if (y1 != y2) {
                return Verdict.WA;
            }
        }
        int v = prog.readInt();
        for (int i = 0; i < v; i++) {
            int x1 = prog.readInt();
            int y1 = prog.readInt();
            int x2 = prog.readInt();
            int y2 = prog.readInt();
            ys.add(new Y(Math.min(y1, y2), Math.max(y1, y2), x1));
            if (x1 != x2) {
                return Verdict.WA;
            }
        }

        List<int[]> hits = new ArrayList<>();
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < v; j++) {
                X x = xs.get(i);
                Y y = ys.get(j);
                if (between(y.x, x.l, x.r) && between(x.y, y.b, y.t)) {
                    hits.add(new int[]{y.x, x.y});
                }
            }
        }

        pts.sort((a, b) -> a[0] == b[0] ? a[1] - b[1] : a[0] - b[0]);
        hits.sort((a, b) -> a[0] == b[0] ? a[1] - b[1] : a[0] - b[0]);
        if(pts.size() != hits.size()){
            return Verdict.WA;
        }

        for(int i = 0; i < pts.size(); i++){
            int[] a = pts.get(i);
            int[] b = hits.get(i);
            if(a[0] != b[0] || a[1] != b[1]){
                return Verdict.WA;
            }
        }

        return Verdict.OK;
    }

    boolean between(int a, int l, int r) {
        return l <= a && a <= r;
    }

    static class X {
        int l;
        int r;
        int y;

        public X(int l, int r, int y) {
            this.l = l;
            this.r = r;
            this.y = y;
        }
    }

    static class Y {
        int b;
        int t;
        int x;

        public Y(int b, int t, int x) {
            this.b = b;
            this.t = t;
            this.x = x;
        }
    }
}
