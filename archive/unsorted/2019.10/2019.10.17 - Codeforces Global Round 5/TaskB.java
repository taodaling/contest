package contest;

import template.FastInput;
import template.FastOutput;

import java.util.Arrays;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Car[] cars = new Car[n];
        for (int i = 0; i < n; i++) {
            cars[i] = new Car();
        }
        for (int i = 0; i < n; i++) {
            cars[in.readInt() - 1].enter = i;
        }
        for (int i = 0; i < n; i++) {
            cars[in.readInt() - 1].leave = i;
        }
        Car[] sortByEnter = cars.clone();
        Car[] sortByLeave = cars.clone();
        Arrays.sort(sortByEnter, (a, b) -> a.enter - b.enter);
        Arrays.sort(sortByLeave, (a, b) -> a.leave - b.leave);

        int ans = 0;
        int a = 0;
        int b = 0;
        while (a < n && b < n) {
            if (sortByEnter[a].away) {
                a++;
                continue;
            }
            if (sortByEnter[a] == sortByLeave[b]) {
                sortByLeave[b].away = true;
                a++;
                b++;
                continue;
            }
            sortByLeave[b].away = true;
            ans++;
            b++;
        }

        out.println(ans);
    }

}


class Car {
    int enter;
    int leave;
    boolean away;
}