package on2021_01.on2021_01_22_Luogu.P6243__USACO06OPEN_The_Milk_Queue_G;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.DoubleWorkStreamProblem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class P6243USACO06OPENTheMilkQueueG {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        //test();
        int n = in.ri();
        long[] a = new long[n];
        long[] b = new long[n];
        for(int i = 0; i < n; i++){
            a[i] = in.ri();
            b[i] = in.ri();
        }
        long ans = DoubleWorkStreamProblem.solve(a, b).b;
        out.println(ans);
    }
}

