package on2021_11.on2021_11_11_CS_Academy___FIICode_2021_Final_Round.Final_B;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.LongArrayList;

import java.util.Arrays;

public class FinalB {
    LongArrayList list = new LongArrayList((int) 1e3);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int cur = n % 100;
        int[] map = new int[100];
        Arrays.fill(map, -1);
        IntegerArrayList list = new IntegerArrayList(100);
        for (int i = 0; i < k; i++) {
            if (map[cur] != -1) {
                //collision
                int l = map[cur];
                int r = i;
                int len = r - l;
                int[] arr = list.toArray();
                long sum = n;
                for(int j = 0; j < l; j++){
                    sum += arr[j];
                }
                long intervalSum = 0;
                for(int j = l; j < r; j++){
                    intervalSum += arr[j];
                }
                sum += (k - l) / len * intervalSum;
                int remain = (k - l) % len;
                for(int j = l; j < l + remain; j++){
                    sum += arr[j];
                }
                out.println(sum);
                return;
            } else {
                map[cur] = i;
                list.add(cur);
            }
            cur = cur * 2 % 100;
        }
        int[] arr = list.toArray();
        long sum = n;
        for(int i = 0; i < k; i++){
            sum += arr[i];
        }
        out.println(sum);
    }
}
