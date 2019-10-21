package on2019_10.on2019_10_21_ARC063.TaskD;



import template.FastInput;
import template.FastOutput;

public class TaskD {
    int inf = (int) 1e9;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int t = in.readInt();
        int[] a = new int[n];
        for(int i = 0; i < n; i++){
            a[i] = in.readInt();
        }
        int minCost = inf;
        int maxProfit = 0;
        int pair = 0;
        for (int i = 0; i < n; i++) {
            if (maxProfit < a[i] - minCost) {
                maxProfit = a[i] - minCost;
                pair = 0;
            }
            if (maxProfit == a[i] - minCost) {
                pair++;
            }
            minCost = Math.min(minCost, a[i]);
        }

        out.println(pair);
    }
}
