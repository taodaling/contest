package contest;

import template.FastInput;
import template.FastOutput;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                int ball = (j - 1) * n;
                if (j % 2 == 0) {
                    ball += i;
                } else {
                    ball += n + 1 - i;
                }
                out.append(ball).append(' ');
            }
            out.println();
        }
//        else{
//            int l = 1;
//            int r = n * n;
//            for(int i = 0; i < n; i++){
//                for(int j = 0; j < n / 2; j++, l++){
//                    out.append(l).append(' ');
//                }
//                for(int j = 0; j < n / 2; j++, r--){
//                    out.append(r).append(' ');
//                }
//                out.println();
//            }
//        }
    }
}
