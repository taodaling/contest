package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.KahamSummation;
import template.utils.SequenceUtils;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class Buoys {
    double prec = 1e-10;
    Buoy[] buoys;
    KahamSummation la = new KahamSummation();
    KahamSummation lb = new KahamSummation();
    KahamSummation ra = new KahamSummation();
    KahamSummation rb = new KahamSummation();
    int lBound;
    int rBound;
    int middle;
    PriorityQueue<Event> events;

    public void swap(int i) {
        if (buoys[i].a >= buoys[i - 1].a) {
            return;
        }
        if (i == rBound) {
            ra.add(buoys[i - 1].a - buoys[i].a);
            rb.add(buoys[i - 1].b - buoys[i].b);
        }
        if (i - 1 == lBound) {
            la.add(buoys[i].a - buoys[i - 1].a);
            lb.add(buoys[i].b - buoys[i - 1].b);
        }
        SequenceUtils.swap(buoys, i, i - 1);
    }

    public void addEvent(int i) {
        if (i >= buoys.length || i == 0 || buoys[i - 1].a <= buoys[i].a) {
            return;
        }
        //ax+b=cx+d
        //x=(d-b)/(a-c)
        events.add(new Event(i, (buoys[i].b - buoys[i - 1].b) / (buoys[i - 1].a - buoys[i].a)));
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        buoys = new Buoy[n];
        lBound = n / 2 - 1;
        rBound = (n + 1) / 2;
        middle = rBound - 1;
        events = new PriorityQueue<>(2 * n * n, Comparator.comparingDouble(x -> x.time));
        for (int i = 0; i < n; i++) {
            buoys[i] = new Buoy();
            buoys[i].id = i;
            buoys[i].a = -i;
            buoys[i].b = in.ri();
            if (i <= lBound) {
                la.add(buoys[i].a);
                lb.add(buoys[i].b);
            }
            if (i >= rBound) {
                ra.add(buoys[i].a);
                rb.add(buoys[i].b);
            }
            if (i > 0) {
                addEvent(i);
            }
        }

        double bestDist = -1;
        double begin = -1;
        double minCost = Double.MAX_VALUE;

        double now = 0;
        while (true) {
            double l = now;
            while (!events.isEmpty()) {
                Event head = events.peek();
                if (buoys[head.index - 1].apply(head.time) + prec < buoys[head.index].apply(head.time) ||
                        buoys[head.index].a >= buoys[head.index - 1].a) {
                    events.remove();
                } else {
                    break;
                }
            }

            double r = events.isEmpty() ? l : events.peek().time;
            double a = ra.sum() - la.sum();
            double b = rb.sum() - lb.sum();
            double distChoice = a < 0 ? r : l;
            double cost = a * distChoice + b;
            if (cost < minCost) {
                minCost = cost;
                bestDist = distChoice;
                begin = buoys[middle].apply(bestDist);
            }
            if (events.isEmpty()) {
                break;
            }
            Event head = events.remove();
            assert head.time + prec >= now;
            now = head.time;
            if (buoys[head.index - 1].apply(now) + prec >= buoys[head.index].apply(now)) {
                swap(head.index);
                addEvent(head.index + 1);
                addEvent(head.index - 1);
            }
        }

        out.println(minCost);
        for (int i = 0; i < n; i++) {
            out.append(begin + i * bestDist).append(' ');
        }
    }
}

class Event {
    int index;
    double time;

    public Event(int index, double time) {
        this.index = index;
        this.time = time;
    }
}

class Buoy {
    int id;
    double b;
    double a;

    public double apply(double x) {
        return a * x + b;
    }
}
