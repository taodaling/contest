package on2019_10.on2019_10_13_Atcode_AGC005.TaskC;



import template.FastInput;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class TaskC {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] a = new int[n];
        int max = 0;

        Map<Integer, Integer> cntMap = new HashMap<>(n);
        for(int i = 0; i < n; i++){
            a[i] = in.readInt();
            max = Math.max(max, a[i]);
            cntMap.put(a[i], cntMap.getOrDefault(a[i], 0) + 1);
        }

        int atLeast = (max + 1) / 2 + 1;
        boolean flag = true;
        for(int i = (max + 1) / 2; i <= max; i++){
            flag = flag && remove(cntMap, i);
            if(i == (max + 1) / 2 && max % 2 == 1) {
                flag = flag && remove(cntMap, i);
            }
        }

        for(Integer k : cntMap.keySet()){
            flag = flag && k >= atLeast;
        }

        out.print(flag ? "Possible" : "Impossible");
    }

    public boolean remove(Map<Integer, Integer> map, int k){
        int val = map.getOrDefault(k, 0);
        if(val == 0){
            return false;
        }
        map.remove(k);
        val--;
        if(val > 0){
            map.put(k, val);
        }
        return true;
    }
}
