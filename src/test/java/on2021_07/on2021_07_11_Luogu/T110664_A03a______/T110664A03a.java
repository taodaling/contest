package on2021_07.on2021_07_11_Luogu.T110664_A03a______;



import template.geometry.geo2.IntegerRect2;
import template.io.FastInput;
import template.io.FastOutput;
import template.problem.RectUnionArea;

public class T110664A03a {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        IntegerRect2[] rects = new IntegerRect2[n];
        for (int i = 0; i < n; i++) {
            int x1 = in.ri();
            int x2 = in.ri();
            int y1 = in.ri();
            int y2 = in.ri();
            if (x1 > x2) {
                int tmp = x1;
                x1 = x2;
                x2 = tmp;
            }
            if (y1 > y2) {
                int tmp = y1;
                y1 = y2;
                y2 = tmp;
            }
            rects[i] = new IntegerRect2(x1, y1, x2, y2);
        }
        long ans = RectUnionArea.unionArea(rects);
        out.println(ans);
    }
}
