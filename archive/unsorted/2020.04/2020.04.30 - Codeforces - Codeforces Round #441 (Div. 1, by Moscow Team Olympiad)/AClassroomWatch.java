package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;

public class AClassroomWatch {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        IntegerList ans = new IntegerList();
        for(int i = Math.max(0, n - 100); i <= n; i++){
            if(digits(i) + i == n){
                ans.add(i);
            }
        }
        out.println(ans.size());
        for(int i = 0; i < ans.size(); i++){
            out.append(ans.get(i)).append(' ');
        }
    }

    public int digits(int n){
        return n == 0 ? 0 : (n % 10 + digits(n / 10));
    }
}
