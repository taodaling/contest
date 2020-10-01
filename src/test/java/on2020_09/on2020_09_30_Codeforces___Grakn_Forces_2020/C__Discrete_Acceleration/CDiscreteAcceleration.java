package on2020_09.on2020_09_30_Codeforces___Grakn_Forces_2020.C__Discrete_Acceleration;



import template.io.FastInput;

import java.io.PrintWriter;

public class CDiscreteAcceleration {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int l = in.readInt();

        int[] a = new int[n + 2];
        a[0] = 0;
        for (int i = 1; i <= n; i++) {
            a[i] = in.readInt();
        }
        a[n + 1] = l;


        double time = 0;
        double leftRemain = 0;
        double rightRemain = 0;
        int left = 0;
        int right = n + 1;
        while (true) {
            if (right - left == 1) {
                double dist = a[right] - a[left] - (left + 1) * leftRemain - (n + 2 - right) * rightRemain;
                double need = dist / (n + 2);
                time += need;
                break;
            }
            double leftReq = (double) (a[left + 1] - a[left]) / (left + 1);
            double rightReq = (double) (a[right] - a[right - 1]) / (n + 2 - right);
            if (leftReq - leftRemain <= rightReq - rightRemain) {
                double t = leftReq - leftRemain;
                left++;
                time += t;
                leftRemain = 0;
                rightRemain += t;
                continue;
            }

            double t = rightReq - rightRemain;
            right--;
            time += t;
            leftRemain += t;
            rightRemain = 0;
        }

        out.printf("%.9f\n", time);
    }
}
