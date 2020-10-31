package on2020_10.on2020_10_24_CSES___CSES_Problem_Set.Traffic_Lights;



import com.sun.org.apache.xpath.internal.operations.Mult;
import template.datastructure.MultiSet;
import template.io.FastInput;
import java.io.PrintWriter;
import java.util.TreeSet;

public class TrafficLights {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int x = in.readInt();
        int n = in.readInt();
        TreeSet<Integer> set = new TreeSet<>();
        set.add(0);
        set.add(x);
        MultiSet<Integer> pq = new MultiSet<>();
        pq.add(x);
        for(int i = 0; i < n; i++){
            int y = in.readInt();
            int floor = set.floor(y);
            int ceil = set.ceiling(y);
            set.add(y);
            pq.remove(ceil - floor);
            pq.add(y - floor);
            pq.add(ceil - y);
            int max = pq.last();
            out.println(max);
        }

    }
}
