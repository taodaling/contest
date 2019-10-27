package on2019_10.on2019_10_27_abc144.TaskD;



import template.FastInput;
import template.FastOutput;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        double a = in.readInt();
        double b = in.readInt();
        double x = in.readInt();
        double theta;
        if (x <= b * a * a / 2) {
            theta = Math.atan(b * b * a / (2 * x));
        } else {
            double t = 2 * x / a / a - b;
            theta = Math.atan((b - t) / a);
        }
        out.printf("%.15f", theta  / Math.PI * 180);
    }
}
