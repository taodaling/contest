package contest;

import template.FastInput;
import template.FastOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        Circle begin = new Circle();
        begin.x = in.readInt();
        begin.y = in.readInt();
        Circle end = new Circle();
        end.x = in.readInt();
        end.y = in.readInt();
        List<Circle> circles = new ArrayList<>();
        circles.add(begin);
        circles.add(end);
        int n = in.readInt();
        for(int i = 0; i < n; i++){
            Circle c = new Circle();
            c.x = in.readInt();
            c.y = in.readInt();
            c.r = in.readInt();
            circles.add(c);
        }

        TreeSet<Circle> set = new TreeSet<>((a, b) -> a.dist == b.dist ?
                a.id - b.id : Double.compare(a.dist, b.dist));

        begin.dist = 0;
        set.add(begin);
        while(!set.isEmpty()){
            Circle head = set.pollFirst();
            head.handled = true;
            for(Circle c : circles){
                if(c.handled){
                    continue;
                }
                double dist = head.dist + Math.max(0, distOf(c, head) - c.r - head.r);
                if(dist >= c.dist){
                    continue;
                }
                set.remove(c);
                c.dist = dist;
                set.add(c);
            }
        }

        out.printf("%.10f", end.dist);
    }

    public double distOf(Circle a, Circle b){
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}

class Circle{
    static int alloc = 0;
    boolean handled;
    int x;
    int y;
    int r;
    double dist = 1e50;
    int id = alloc++;
}
