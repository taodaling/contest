package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class BRobotArms {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[][] robots = new int[n][2];
        for (int i = 0; i < n; i++) {
            robots[i][0] = in.readInt();
            robots[i][1] = in.readInt();
        }
        Arrays.sort(robots, (a, b) -> (a[0] + a[1]) - (b[0] + b[1]));
        TreeMap<Integer, Integer> map = new TreeMap<>();
        int ans = 0;
        int last = 0;
        for(int[] r : robots){
            Map.Entry<Integer, Integer> floor = map.floorEntry(r[0] - r[1]);
            int pre = floor == null ? 0 : floor.getValue();
            int dp = Math.max(last, pre + 1);
            last = dp;
            map.put(r[0] + r[1], dp);
            ans = Math.max(ans, dp);
        }
        out.println(ans);
    }
}
