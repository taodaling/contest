package on2019_11.on2019_11_11_Codeforces_Round__586__Div__1___Div__2_.D___Alex_and_Julian;



import template.FastInput;
import template.FastOutput;

import java.util.Comparator;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] b = new long[n];
        for(int i = 0; i < n; i++){
            b[i] = in.readLong();
        }
        int[] groups = new int[100];
        for(int i = 0; i < n; i++){
            groups[extract(b[i])]++;
        }
        int maxGroupIndex = 0;
        for(int i = 0; i < 100; i++){
            if(groups[i] > groups[maxGroupIndex]){
                maxGroupIndex = i;
            }
        }

        out.println(n - groups[maxGroupIndex]);
        for(int i = 0; i < n; i++){
            if(extract(b[i]) != maxGroupIndex){
                out.append(b[i]).append(' ');
            }
        }
    }

    public int extract(long x){
        int ans = 0;
        while(x > 0 && x % 2 == 0){
            x /= 2;
            ans++;
        }
        return ans;
    }
}
