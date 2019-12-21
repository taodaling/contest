package contest;

import template.FastInput;
import template.FastOutput;
import template.IntegerList;

import java.util.TreeSet;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();

        int[][] balls = new int[n][2];
        int rMax = 0;
        int bMax = 0;
        int rMin = (int) 1e9;
        int bMin = rMin;
        for (int i = 0; i < n; i++) {
            int a = in.readInt();
            int b = in.readInt();
            if (a > b) {
                int tmp = a;
                a = b;
                b = tmp;
            }
            balls[i][0] = a;
            balls[i][1] = b;
            rMax = Math.max(rMax, a);
            rMin = Math.min(rMin, a);
            bMax = Math.max(bMax, b);
            bMin = Math.min(bMin, b);
        }

        long ans = (long) (rMax - rMin) * (bMax - bMin);

        rMin = Math.min(rMin, bMin);
        rMax = Math.max(rMax, bMax);
        TreeSet<int[]> orderByMin = new TreeSet<>((a, b) -> a[0] == b[0] ? a[1] - b[1] : a[0] - b[0]);
        TreeSet<int[]> orderByMax = new TreeSet<>((a, b) -> a[1] == b[1] ? a[0] - b[0] : a[1] - b[1]);
        for (int[] ball : balls) {
            orderByMin.add(ball);
        }

        IntegerList seq = new IntegerList(n * 2);
        for (int i = 0; i < n; i++) {
            seq.add(balls[i][0]);
            seq.add(balls[i][1]);
        }
        seq.unique();
        for (int i = 0; i < seq.size(); i++) {
            int v = seq.get(i);
            while (!orderByMin.isEmpty() && orderByMin.first()[0] < v) {
                int[] ball = orderByMin.pollFirst();
                orderByMax.add(ball);
            }
            if(!orderByMax.isEmpty() && orderByMax.first()[1] < v){
                break;
            }
            int mx = 0;
            if(!orderByMin.isEmpty()){
                mx = Math.max(mx, orderByMin.last()[0]);
            }
            if(!orderByMax.isEmpty()){
                mx = Math.max(mx, orderByMax.last()[1]);
            }

            ans = Math.min(ans, (long)(rMax - rMin) * (mx - v));
        }

        out.println(ans);
    }
}
