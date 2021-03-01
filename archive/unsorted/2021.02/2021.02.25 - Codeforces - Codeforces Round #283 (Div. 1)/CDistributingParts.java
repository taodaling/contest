package contest;

import template.datastructure.Range2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class CDistributingParts {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Part[] parts = new Part[n];
        for (int i = 0; i < n; i++) {
            parts[i] = new Part();
            parts[i].l = in.ri();
            parts[i].r = in.ri();
        }
        int m = in.ri();
        Actor[] actors = new Actor[m];
        for (int i = 0; i < m; i++) {
            actors[i] = new Actor();
            actors[i].id = i;
            actors[i].l = in.ri();
            actors[i].r = in.ri();
            actors[i].k = in.ri();
        }
        Part[] sorted = parts.clone();
        Arrays.sort(sorted, Comparator.comparingInt(x -> x.l));
        Arrays.sort(actors, Comparator.comparingInt(x -> x.l));
        SimplifiedDeque<Actor> dq = new Range2DequeAdapter<>(i -> actors[i], 0, m - 1);
        TreeSet<Actor> set = new TreeSet<>(Comparator.<Actor>comparingInt(x -> x.r).thenComparingInt(x -> x.id));
        Actor empty = new Actor();
        empty.id = -1;
        for (Part part : sorted) {
            while (!dq.isEmpty() && dq.peekFirst().l <= part.l) {
                set.add(dq.removeFirst());
            }
            empty.r = part.r;
            Actor fit = set.ceiling(empty);
            while (fit != null && fit.k == 0) {
                set.remove(fit);
                fit = set.ceiling(empty);
            }
            if (fit == null) {
                out.println("NO");
                return;
            }
            fit.k--;
            part.assign = fit;
        }
        out.println("YES");
        for(Part part : parts){
            out.append(part.assign.id + 1).append(' ');
        }
    }
}

class Actor {
    int id;
    int l;
    int r;
    int k;
}

class Part {
    int l;
    int r;
    Actor assign;
}