package on2021_10.on2021_10_17_Library_Checker.Frequency_Table_of_Tree_Distance;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class FrequencyTableOfTreeDistance {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        template.problem.FrequencyTableOfTreeDistance ft = new template.problem.FrequencyTableOfTreeDistance(n);
        for (int i = 0; i < n - 1; i++) {
            ft.addEdge(in.ri(), in.ri());
        }
        long[] ans = ft.solve();
        for (int i = 1; i < n; i++) {
            out.println(ans[i]);
        }
    }


}
