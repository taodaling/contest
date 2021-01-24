package on2021_01.on2021_01_22_Luogu.T110664_A03a______;



import template.io.FastInput;
import template.io.FastOutput;
import template.problem.RectUnionArea;

public class T110664A03a {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        RectUnionArea.Rect[] rects = new RectUnionArea.Rect[n];
        for(int i = 0; i < n; i++){
            int x1 = in.ri();
            int x2 = in.ri();
            int y1 = in.ri();
            int y2 = in.ri();
            rects[i] = RectUnionArea.Rect.newInstanceByTwoPoints(x1, y1, x2, y2);
        }
        long ans = RectUnionArea.unionArea(rects);
        out.println(ans);
    }
}
