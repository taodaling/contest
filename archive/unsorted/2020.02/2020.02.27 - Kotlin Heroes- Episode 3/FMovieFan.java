package contest;

import template.algo.IntBinarySearch;
import template.datastructure.IntervalBooleanMap;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.Arrays;

public class FMovieFan {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        m = in.readInt();
        Movie[] movies = new Movie[n];
        for (int i = 0; i < n; i++) {
            int a = in.readInt();
            int b = in.readInt();
            movies[i] = new Movie();
            movies[i].l = dayBegin(a);
            movies[i].r = dayEnd(b);
        }
        Movie[] sorted = movies.clone();
        Arrays.sort(sorted, (a, b) -> Long.compare(a.r, b.r));
        IntervalBooleanMap map = new IntervalBooleanMap();
        long day = 0;
        for (Movie movie : sorted) {
            long index = map.firstFalse(movie.l);
            movie.day = index;
            map.setTrue(index, index);
            if (movie.day > movie.r) {
                day = Math.max(DigitUtils.ceilDiv(movie.day - movie.r, m), day);
            }
        }
        out.println(day);
        for (int i = 0; i < n; i++) {
            out.append(movies[i].day / m).append(' ');
        }
        out.println();
    }

    long m;
    public long dayBegin(int i) {
        return i * m;
    }

    public long dayEnd(int i) {
        return dayBegin(i + 1) - 1;
    }
}

class Movie {
    long l;
    long r;
    long day;
}
