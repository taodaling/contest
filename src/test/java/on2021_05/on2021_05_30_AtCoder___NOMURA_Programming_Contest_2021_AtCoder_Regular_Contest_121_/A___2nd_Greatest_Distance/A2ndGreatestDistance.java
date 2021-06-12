package on2021_05.on2021_05_30_AtCoder___NOMURA_Programming_Contest_2021_AtCoder_Regular_Contest_121_.A___2nd_Greatest_Distance;



import template.io.FastInput;
import template.io.FastOutput;

public class A2ndGreatestDistance {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[][] pts = new int[n][];
        for (int i = 0; i < n; i++) {
            pts[i] = in.ri(2);
        }

        int[] pair = farthestPair(pts);
        int[][] remove1 = remove(pts, pair[0]);
        int[][] remove2 = remove(pts, pair[1]);
        int[] p1 = farthestPair(remove1);
        int[] p2 = farthestPair(remove2);
        int ans = Math.max(dist(remove1, p1[0], p1[1]), dist(remove2, p2[0], p2[1]));

        out.println(ans);
    }

    int dist(int[][] pts, int i, int j) {
        return Math.max(Math.abs(pts[i][0] - pts[j][0]), Math.abs(pts[i][1] - pts[j][1]));
    }

    int[][] remove(int[][] pts, int index) {
        int[][] ans = new int[pts.length - 1][];
        int wpos = 0;
        for (int i = 0; i < pts.length; i++) {
            if (i == index) {
                continue;
            }
            ans[wpos++] = pts[i];
        }
        assert wpos == pts.length - 1;
        return ans;
    }

    public int[] farthestPair(int[][] pts, int dimension) {
        int maxIndex = 0;
        int minIndex = 0;
        for (int i = 0; i < pts.length; i++) {
            if (pts[i][dimension] > pts[maxIndex][dimension]) {
                maxIndex = i;
            }
            if (pts[i][dimension] < pts[minIndex][dimension]) {
                minIndex = i;
            }
        }
        return new int[]{minIndex, maxIndex};
    }

    public int[] farthestPair(int[][] pts) {
        int[] p1 = farthestPair(pts, 0);
        int[] p2 = farthestPair(pts, 1);
        if (dist(pts, p1[0], p1[1]) > dist(pts, p2[0], p2[1])) {
            return p1;
        } else {
            return p2;
        }
    }
}
