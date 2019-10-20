package on2019_10.on2019_10_20_Codeforces_Round__594__Div__1_.C___Queue_in_the_Train;



import template.FastInput;
import template.FastOutput;
import template.MinQueue;

import java.util.*;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long p = in.readInt();

        PriorityQueue<Event> events = new PriorityQueue<>(2 * n, (a, b) -> Long.compare(a.time, b.time));

        Seat[] seats = new Seat[n];
        for (int i = 0; i < n; i++) {
            seats[i] = new Seat();
            seats[i].id = i;
            Event e = new Event();
            e.type = 2;
            e.which = seats[i];
            e.time = in.readInt();
            events.add(e);
        }
        MinQueue<Seat> inQue = new MinQueue<>(n, (a, b) -> a.id - b.id);
        PriorityQueue<Seat> wait = new PriorityQueue<>(n, (a, b) -> a.id - b.id);

        boolean state = false;
        while (!events.isEmpty()) {
            long time = events.peek().time;
            while (!events.isEmpty() && events.peek().time == time) {
                Event head = events.remove();
                if (head.type == 2) {
                    wait.add(head.which);
                } else if (head.type == 1) {
                    state = false;
                    inQue.deque();
                    head.which.ans = head.time;
                }
            }

            while (!wait.isEmpty() && (inQue.isEmpty() || inQue.query().id > wait.peek().id)) {
                Seat s = wait.remove();
                inQue.enqueue(s);
            }

            if (!state && !inQue.isEmpty()) {
                state = true;
                Seat s = inQue.peek();
                Event e = new Event();
                e.which = s;
                e.type = 1;
                e.time = time + p;
                events.add(e);
            }
        }

        for(Seat s : seats){
            out.append(s.ans).append(' ');
        }
    }
}

class Event {
    int type; //2 ready, 1 back
    Seat which;
    long time;
}

class Seat {
    int id;
    long ans;

    @Override
    public String toString() {
        return "" + id;
    }
}