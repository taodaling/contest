import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.AbstractQueue;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.AbstractCollection;
import java.io.Closeable;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.InputStream;
import java.util.function.IntFunction;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
*/public class Main {
	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1 << 27);
		thread.start();
		thread.join();
	}

	static class TaskAdapter implements Runnable {
		@Override
		public void run() {			InputStream inputStream = System.in;
			OutputStream outputStream = System.out;
			FastInput in = new FastInput(inputStream);
			FastOutput out = new FastOutput(outputStream);
			SpaciousOffice solver = new SpaciousOffice();
			solver.solve(1, in, out);
			out.close();
		}
	}
static class SpaciousOffice   {
boolean[][] edges;
boolean[] visited;
boolean[] instk;

public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Room[] rooms = new Room[n];
        for (int i = 0; i < n; i++) {
            rooms[i] = new Room();
            rooms[i].id = i;
            rooms[i].area = in.readInt();
        }
        Arrays.sort(rooms, (a, b) -> Integer.compare(a.area, b.area));
        Req[] reqs = new Req[n];
        for (int i = 0; i < n; i++) {
            reqs[i] = new Req();
            reqs[i].l = in.readInt();
            reqs[i].r = in.readInt();
        }
        Req[] originalReqs = reqs.clone();
        Arrays.sort(reqs, (x, y) -> Integer.compare(x.l, y.l));
        Range2DequeAdapter<Req> dq = new Range2DequeAdapter<>(i -> reqs[i], 0, n - 1);
        PriorityQueue<Req> pq = new PriorityQueue<>(n, (x, y) -> Integer.compare(x.r, y.r));
        for (int i = 0; i < n; i++) {
            while (!dq.isEmpty() && dq.peekFirst().l <= rooms[i].area) {
                pq.add(dq.removeFirst());
            }
            if (pq.isEmpty() || pq.peek().r < rooms[i].area) {
                out.println("Let's search for another office.");
                return;
            }
            pq.remove().room = rooms[i];
        }

        edges = new boolean[n][n];
        visited = new boolean[n];
        instk = new boolean[n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }
                if (reqs[j].room.area >= reqs[i].l && reqs[j].room.area <= reqs[i].r) {
                    edges[i][j] = true;
                }
            }
        }

        for (int i = 0; i < n; i++) {
            if (dfs(i)) {
                out.println("Ask Shiftman for help.");
                return;
            }
        }

        out.println("Perfect!");
        for(int i = 0; i < n; i++){
            out.append(originalReqs[i].room.id + 1).append(' ');
        }
    }

public boolean dfs(int root) {
        if (visited[root]) {
            return instk[root];
        }
        visited[root] = instk[root] = true;
        for (int i = 0; i < edges.length; i++) {
            if (!edges[root][i]) {
                continue;
            }
            if (dfs(i)) {
                return true;
            }
        }
        instk[root] = false;
        return false;
    }

}
static interface SimplifiedDeque<T> extends SimplifiedStack<T>  {
}
static class FastOutput  implements AutoCloseable, Closeable, Appendable {
private StringBuilder cache = new StringBuilder(1 << 20);
private   final Writer os;

public FastOutput append(CharSequence csq) {
        cache.append(csq);
        return this;
    }

public FastOutput append(CharSequence csq, int start, int end) {
        cache.append(csq, start, end);
        return this;
    }

public FastOutput(Writer os) {
        this.os = os;
    }

public FastOutput(OutputStream os) {
        this(new OutputStreamWriter(os));
    }

public FastOutput append(char c) {
        cache.append(c);
        return this;
    }

public FastOutput append(int c) {
        cache.append(c);
        return this;
    }

public FastOutput append(String c) {
        cache.append(c);
        return this;
    }

public FastOutput println(String c) {
        return append(c).println();
    }

public FastOutput println() {
        cache.append(System.lineSeparator());
        return this;
    }

public FastOutput flush() {
        try {
            os.append(cache);
            os.flush();
            cache.setLength(0);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

public void close() {
        flush();
        try {
            os.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

public String toString() {
        return cache.toString();
    }

}
static interface SimplifiedStack<T> extends Iterable<T>  {
}
static class Range2DequeAdapter<T>  implements SimplifiedDeque<T> {
IntFunction<T> data;
int l;
int r;

public Range2DequeAdapter(IntFunction<T> data, int l, int r) {
        this.data = data;
        this.l = l;
        this.r = r;
    }

public boolean isEmpty() {
        return l > r;
    }

public T peekFirst() {
        return data.apply(l);
    }

public T removeFirst() {
        return data.apply(l++);
    }

public Iterator<T> iterator() {
        return new Iterator<T>() {
            int iter = l;


            public boolean hasNext() {
                return iter <= r;
            }


            public T next() {
                return data.apply(iter++);
            }
        };
    }

}
static class Room   {
int area;
int id;

}
static class FastInput   {
private   final InputStream is;
private byte[] buf = new byte[1 << 13];
private int bufLen;
private int bufOffset;
private int next;

public FastInput(InputStream is) {
        this.is = is;
    }

private int read() {
        while (bufLen == bufOffset) {
            bufOffset = 0;
            try {
                bufLen = is.read(buf);
            } catch (IOException e) {
                bufLen = -1;
            }
            if (bufLen == -1) {
                return -1;
            }
        }
        return buf[bufOffset++];
    }

public void skipBlank() {
        while (next >= 0 && next <= 32) {
            next = read();
        }
    }

public int readInt() {
        int sign = 1;

        skipBlank();
        if (next == '+' || next == '-') {
            sign = next == '+' ? 1 : -1;
            next = read();
        }

        int val = 0;
        if (sign == 1) {
            while (next >= '0' && next <= '9') {
                val = val * 10 + next - '0';
                next = read();
            }
        } else {
            while (next >= '0' && next <= '9') {
                val = val * 10 - next + '0';
                next = read();
            }
        }

        return val;
    }

}
static class Req   {
int l;
int r;
Room room;

}
}

