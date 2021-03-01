package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;

public class DPackageDelivery {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int d = in.ri();
        n = in.ri();
        int m = in.ri();
        Station[] stations = new Station[m];
        for (int i = 0; i < m; i++) {
            stations[i] = new Station();
            stations[i].pos = in.ri();
            stations[i].price = in.ri();
        }
        Arrays.sort(stations, Comparator.comparingInt(x -> x.pos));
        addFuel = n;
        dq = new ArrayDeque<>(m);
        for (Station station : stations) {
            if (!move(station.pos)) {
                out.println(-1);
                return;
            }
            while(!dq.isEmpty() && dq.peekLast().price >= station.price){
                dq.removeLast();
            }
            dq.addLast(station);
        }
        if(!move(d)){
            out.println(-1);
            return;
        }
        out.println(totalFee);
    }

    long totalFee;
    long addFuel;
    long n;
    Deque<Station> dq;

    public boolean move(long target) {
        while (target > addFuel && !dq.isEmpty()) {
            Station station = dq.peekFirst();
            long cap = n - (addFuel - station.pos);
            if (cap <= 0) {
                dq.removeFirst();
                continue;
            }
            long added = Math.min(cap, target - addFuel);
            addFuel += added;
            totalFee += added * station.price;
        }
        return target <= addFuel;
    }
}

class Station {
    int price;
    int pos;
}
