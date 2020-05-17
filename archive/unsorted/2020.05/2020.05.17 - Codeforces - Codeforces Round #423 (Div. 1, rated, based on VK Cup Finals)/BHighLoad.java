package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class BHighLoad {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();

        if (n == 2) {
            out.println(1);
            out.append(1).append(' ').append(2).println();
            return;
        }

        int type1 = n - k;
        int type2 = k;

        if (type1 <= 2) {
            out.println(type1 + 1);
            for (int i = 1; i <= type1; i++) {
                out.append(i).append(' ').append(i + type1).println();
                if(i != 1){
                    out.append(1).append(' ').append(i).println();
                }
            }
            for (int i = type1 + type1 + 1; i <= n; i++) {
                out.append(1).append(' ').append(i).println();
            }
            return;
        }

        int avg = (type1 - 1) / type2;
        int mod = type1 - 1 - avg * type2;

        out.println((avg + 1) * 2 + Math.min(2, mod));

        int first = 2;
        for (int i = type1 + 1; i <= n; i++) {
            int dist = avg;
            if (mod > 0) {
                mod--;
                dist++;
            }

            int pa = 1;
            for (int j = 0; j < dist; j++) {
                out.append(pa).append(' ').append(first).println();
                pa = first;
                first++;
            }

            out.append(pa).append(' ').append(i).println();
        }
    }
}
