package on2021_08.on2021_08_04_AtCoder___AtCoder_Beginner_Contest_212.F___Greedy_Takahashi;



import template.graph.*;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FGreedyTakahashi {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int q = in.ri();
        List<Car>[] cars = new List[n];
        Car[] allCars = new Car[m];
        for (int i = 0; i < n; i++) {
            cars[i] = new ArrayList<>();
        }
        for (int i = 0; i < m; i++) {
            Car car = new Car();
            car.id = i;
            car.s = in.ri() - 1;
            car.t = in.ri() - 1;
            car.startTime = in.ri();
            car.endTime = in.ri();
            cars[car.s].add(car);
            allCars[i] = car;
        }
        for (int i = 0; i < n; i++) {
            cars[i].sort(Comparator.comparingInt(x -> x.startTime));
        }
        Car[] sortedAllCars = allCars.clone();
        Arrays.sort(sortedAllCars, Comparator.comparingInt(x -> -x.startTime));
        for (Car c : sortedAllCars) {
            int off = c.endTime;
            c.next = nextCar(cars[c.t], off);
        }


        ParentOnTree pot = new ParentOnTreeByFunction(m, i -> allCars[i].next == null ? -1 : allCars[i].next.id);
        DepthOnTree dot = new DepthOnTreeByParent(m, pot);
        CompressedBinaryLift bl = new CompressedBinaryLift(m, dot, pot);
        for (int i = 0; i < q; i++) {
            int x = in.ri();
            int y = in.ri() - 1;
            int z = in.ri();
            Car c = nextCar(cars[y], x);
            if (c == null || c.startTime >= z) {
                out.println(y + 1);
                continue;
            }
            int finalCarId = bl.lastTrue(c.id, mid -> {
                return allCars[mid].startTime < z;
            });
            Car finalCar = allCars[finalCarId];
            if (finalCar.endTime >= z) {
                out.append(finalCar.s + 1).append(' ').append(finalCar.t + 1).println();
            } else {
                out.println(finalCar.t + 1);
            }
        }
    }

    public Car nextCar(List<Car> list, int endTime) {
        int l = 0;
        int r = list.size();
        while (l < r) {
            int mid = (l + r) / 2;
            if (list.get(mid).startTime >= endTime) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        if (l < list.size()) {
            return list.get(l);
        } else {
            return null;
        }
    }
}

class Car {
    Car next;
    int id;
    int startTime;
    int endTime;
    int s;
    int t;
}