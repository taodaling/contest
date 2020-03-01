package on2020_02.on2020_02_27_Kotlin_Heroes__Episode_3.D__Bonus_Distribution;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CompareUtils;

import java.util.Arrays;

public class DBonusDistribution {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        Employee[] a = new Employee[n];
        for(int i = 0; i < n; i++){
            a[i] = new Employee();
            a[i].v = in.readInt();
        }
        Employee[] sorted = a.clone();
        Arrays.sort(sorted, (x, y) -> -Integer.compare(x.v, y.v));
        for(int i = 1; i < n; i++){
            int allow = sorted[i - 1].v - 1 - sorted[i].v;
            allow = Math.min(k, allow);
            k -= allow;
            sorted[i].v += allow;
            sorted[i].k = allow;
        }
        int avg = k / n;
        for(int i = 0; i < n; i++){
            sorted[i].k += avg;
        }
        int remain = k % n;
        for(int i = 0; i < remain; i++){
            sorted[i].k++;
        }

        for(Employee e : a){
            out.append(e.k).append(' ');
        }
        out.println();
    }
}


class Employee {
    int v;
    int k;
}