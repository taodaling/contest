package contest;

import template.datastructure.MultiSet;
import template.io.FastInput;

import java.io.PrintWriter;
import java.util.Arrays;

public class MovieFestivalII {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int k = in.readInt();
        MultiSet<Integer> set = new MultiSet<>();
        set.update(0, k);
        Movie[] movies = new Movie[n];
        for (int i = 0; i < n; i++) {
            movies[i] = new Movie(in.readInt(), in.readInt());
        }
        Arrays.sort(movies, (a, b) -> Integer.compare(a.r, b.r));
        int ans = 0;
        for (Movie m : movies) {
            Integer floor = set.floor(m.l);
            if (floor == null) {
                continue;
            }
            ans++;
            set.remove(floor);
            set.add(m.r);
        }
        out.println(ans);
    }
}

class Movie {
    int l;
    int r;

    public Movie(int l, int r) {
        this.l = l;
        this.r = r;
    }
}
