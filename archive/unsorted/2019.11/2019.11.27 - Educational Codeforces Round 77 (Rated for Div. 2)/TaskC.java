package contest;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Gcd;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int r = in.readInt();
        int b = in.readInt();
        int k = in.readInt();

        if (r < b) {
            int tmp = r;
            r = b;
            b = tmp;
        }

        if(r == b){
            out.println("OBEY");
            return;
        }

        Gcd gcd = new Gcd();
        int g = gcd.gcd(r, b);
        int time = (r - g - 1) / b + 1;
        out.println(time < k ? "OBEY" : "REBEL");
    }
}
