package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class FEmployment {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.readInt();
        int n = in.readInt();
        Item[] a = new Item[n];
        Item[] b = new Item[n];
        for (int i = 0; i < n; i++) {
            a[i] = new Item();
            a[i].index = i;
            a[i].x = in.readInt();
        }

        for (int i = 0; i < n; i++) {
            b[i] = new Item();
            b[i].index = i;
            b[i].x = in.readInt();
        }

        Arrays.sort(a, (x, y) -> x.x - y.x);
        Arrays.sort(b, (x, y) -> x.x - y.x);

        Machine machine = new Machine();
        machine.a = new int[n];
        machine.b = new int[n];

        for (int i = 0; i < n; i++) {
            if (clockwise(a[i].x, b[i].x, m)) {
                if (b[i].x >= a[i].x) {
                    machine.b[i] = b[i].x;
                    machine.a[i] = -a[i].x;
                } else {
                    machine.b[i] = b[i].x;
                    machine.a[i] = m - a[i].x;
                }
            } else {
                if (b[i].x <= a[i].x) {
                    machine.b[i] = -b[i].x;
                    machine.a[i] = a[i].x;
                } else {
                    machine.b[i] = -b[i].x;
                    machine.a[i] = m + a[i].x;
                }
            }
            machine.sum += machine.a[i];
            machine.sum += machine.b[i];
        }

        PriorityQueue<Event> pq = new PriorityQueue<>(n * 8, (x, y) -> x.time() - y.time());
        Item tmpItem = new Item();
        for (int i = 0; i < n; i++) {
            if (a[i].x >= m / 2) {
                int since = a[i].x - m / 2;
                tmpItem.x = since;
                int index = SequenceUtils.leftBound(b, tmpItem, 0, n - 1, Item.sortByX);
                if (index < n && b[index].x <= a[i].x) {
                    if (!clockwise(a[i].x, b[i].x, m)) {

                    }
                }
            }
        }
    }

    public int meet(int i, int j, int n) {
        return DigitUtils.mod(j - i, n);
    }

    /**
     * Determine whether b located on clockwise side of a
     */
    public boolean clockwise(int a, int b, int m) {
        if (b >= a) {
            return b - a <= m / 2;
        } else {
            return b + m - a <= m / 2;
        }
    }
}

interface Event {
    int time();

    void apply();
}

class Item {
    int x;
    int index;
    int partner;

    static Comparator<Item> sortByX = (a, b) -> a.x - b.x;
}

class Machine {
    int[] a;
    int[] b;
    long sum;

    void makeANegative(int i) {
        if (a[i] <= 0) {
            return;
        }
        sum -= a[i];
        a[i] = -a[i];
        sum += a[i];
    }

    void makeAPositive(int i) {
        if (a[i] >= 0) {
            return;
        }
        sum -= a[i];
        a[i] = -a[i];
        sum += a[i];
    }

    void makeBNegative(int i) {
        if (b[i] <= 0) {
            return;
        }
        sum -= b[i];
        b[i] = -b[i];
        sum += b[i];
    }

    void makeBPositive(int i) {
        if (b[i] >= 0) {
            return;
        }
        sum -= b[i];
        b[i] = -b[i];
        sum += b[i];
    }

    void modifyA(int i, int x) {
        if (a[i] < 0) {
            a[i] -= x;
            sum -= x;
        } else {
            a[i] += x;
            sum += x;
        }
    }
}