package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class Totalizator {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] scores = new int[n];
        for(int i = 0; i < m; i++){
            int a = in.ri();
            int b = in.ri();
            for(int j = 0; j < n; j++){
                int x = in.ri();
                int y = in.ri();
                if(Integer.compare(a, b) == Integer.compare(x, y)){
                    scores[j] += 2;
                }
                if(x - y == a - b){
                    scores[j] += 3;
                }
                if(x == a){
                    scores[j]++;
                }
                if(y == b){
                    scores[j]++;
                }
            }
        }

        for(int x : scores){
            out.println(x);
        }
    }
}
