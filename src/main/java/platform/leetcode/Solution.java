package platform.leetcode;


import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.TreeSet;

class Solution {

    public static void main(String[] args) {
        new Solution().getCollisionTimes(new int[][]{{1,2},{2,1},{4,3},{7,2}});
    }

    double inf = 1e50;

    private double meetTime(Car a, Car b) {
        if (a.speed <= b.speed) {
            return inf;
        }
        return (b.x - a.x) / (a.speed - b.speed);
    }

    public double[] getCollisionTimes(int[][] data) {
        int n = data.length;
        Car[] cars = new Car[n];
        PriorityQueue<Event> pq = new PriorityQueue<>(n, Comparator.comparingDouble(x -> x.time));
        TreeSet<Car> set = new TreeSet<>(Comparator.comparingInt(x -> x.id));
        for (int i = 0; i < n; i++) {
            cars[i] = new Car(data[i][0], data[i][1], i);
            set.add(cars[i]);
        }
        for (int i = 0; i < n - 1; i++) {
            pq.add(new Event(meetTime(cars[i], cars[i + 1]), cars[i], cars[i + 1]));
        }


        Car buf = new Car(0, 0, 0);
        while (!pq.isEmpty()) {
            Event head = pq.remove();
            if (head.time == inf) {
                break;
            }
            Car former = head.former;
            Car later = head.later;
            if (!set.contains(former) || Math.abs(meetTime(later, former) - head.time) >= 1e-10) {
                continue;
            }
            later.ans = head.time;
            set.remove(head.later);
            buf.id = former.id - 1;
            Car prev = set.floor(buf);
            if (prev != null) {
                pq.add(new Event(meetTime(prev, former), prev, former));
            }
        }
        double[] ans = Arrays.stream(cars).mapToDouble(x -> x.ans).toArray();
        return ans;
    }
}

class Event {
    double time;
    Car later;
    Car former;

    public Event(double time, Car later, Car former) {
        this.time = time;
        this.later = later;
        this.former = former;
    }
}

class Car {
    double x;
    int speed;
    int id;
    double ans = -1;

    public Car(double x, int speed, int id) {
        this.x = x;
        this.speed = speed;
        this.id = id;
    }
}