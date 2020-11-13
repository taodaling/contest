package contest;

import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.problem.MovieProblem;

import java.util.Arrays;

public class MovieFestivalQueries {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        MovieProblem.Movie[] movies = new MovieProblem.Movie[n];
        for (int i = 0; i < n; i++) {
            movies[i] = new MovieProblem.Movie(in.readInt(), in.readInt() - 1);
        }
        MovieProblem mp = new MovieProblem(movies);
        for (int i = 0; i < q; i++) {
            int l = in.readInt();
            int r = in.readInt() - 1;
            int ans = mp.query(l, r);
            out.println(ans);
        }
    }
}