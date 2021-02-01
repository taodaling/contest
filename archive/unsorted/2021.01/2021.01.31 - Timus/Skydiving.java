package contest;

import template.datastructure.MinQueue;
import template.datastructure.MultiWayStack;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.DoubleComparator;
import template.primitve.generated.datastructure.DoubleMinQueue;

import java.util.Comparator;
import java.util.List;

public class Skydiving {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Pt[] pts = new Pt[n + 1];
        pts[n] = new Pt();
        pts[n].x = in.ri();
        pts[n].y = in.ri();
        for (int i = 0; i < n; i++) {
            pts[i] = new Pt();
            pts[i].x = in.ri();
            pts[i].y = in.ri();
            pts[i].c = in.ri();
        }
        int threshold = 10000;
        double[] time = new double[threshold + 1];
        double g = 10;
        for (int i = 0; i <= threshold; i++) {
            //0.5gt^2 = i
            time[i] = Math.sqrt(2 * i / g);
        }
        MinQueue<Pt> mq = new MinQueue<>(n, Comparator.comparingDouble(x -> x.time));
        List<Pt>[] lists = Graph.createGraph(threshold + 1);
        for (Pt pt : pts) {
            lists[pt.y].add(pt);
        }
        for (int i = 0; i <= threshold; i++) {
            lists[i].sort(Comparator.comparingInt(x -> x.x));
        }
        for (int i = 1; i <= 10000; i++) {
            if(lists[i].isEmpty()){
                continue;
            }
            for (int j = 0; j < i; j++) {
//                if(lists[j].isEmpty()){
//                    continue;
//                }
                int k = 0;
                mq.clear();
                for (Pt pt : lists[i]) {
                    double l = pt.x - time[i - j];
                    double r = pt.x + time[i - j];
                    while (k < lists[j].size() && lists[j].get(k).x <= r) {
                        mq.add(lists[j].get(k++));
                    }
                    while (!mq.isEmpty() && mq.first().x < l) {
                        mq.removeFirst();
                    }
                    double max = mq.isEmpty() ? 0 : mq.first().time;
                    pt.time = Math.max(pt.time, max + time[i - j] + pt.c);
                }
            }
        }

        double ans = pts[n].time;
        out.println(ans);
    }
}

class Pt {
    int x;
    int y;
    int c;
    double time;
}