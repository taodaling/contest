package on2020_12.on2020_12_01_Codeforces___acm_sgu_ru_archive.Task;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Calendar;

public class Task {
    int[] date = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        if(!(m >= 1 && m <= 12 && n >= 1 && n <= date[m - 1])){
            out.println("Impossible");
            return;
        }

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2001);
        c.set(Calendar.MONTH, m - 1);
        c.set(Calendar.DAY_OF_MONTH, n);
        int ans = c.get(Calendar.DAY_OF_WEEK);
        if (ans == 1) {
            ans = 8;
        }
        ans--;
        out.println(ans);
    }
}
