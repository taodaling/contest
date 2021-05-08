import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Deque;
import java.util.ArrayList;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Collection;
import java.io.IOException;
import java.util.stream.Collectors;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.List;
import java.util.stream.Stream;
import java.io.Closeable;
import java.io.Writer;
import java.util.BitSet;
import java.util.ArrayDeque;
import java.util.Collections;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(null, new TaskAdapter(), "", 1 << 29);
        thread.start();
        thread.join();
    }

    static class TaskAdapter implements Runnable {
        @Override
        public void run() {
            InputStream inputStream = System.in;
            OutputStream outputStream = System.out;
            FastInput in = new FastInput(inputStream);
            FastOutput out = new FastOutput(outputStream);
            Task solver = new Task();
            try {
                int testNumber = 1;
                while (true)
                    solver.solve(testNumber++, in, out);
            } catch (UnknownError e) {
                out.close();
            }
        }
    }

    static class Task {
        boolean[] forbidden;

        {
            forbidden = new boolean[128];
            Arrays.fill(forbidden, true);
            for (char c : "|*+()".toCharArray()) {
                forbidden[c] = false;
            }
        }

        public void solve(int testNumber, FastInput in, FastOutput out) {
            if (!in.hasMore()) {
                throw new UnknownError();
            }
            String s = in.rs();
            String t = in.rs();
            boolean res = Parser.parse(s, forbidden).newMatcher().match(t, 0, t.length());
            out.println(res ? "Yes" : "No");
        }

    }

    static interface State {
        Transfer next(int c);

        Collection<Transfer> adj();

        int id();

    }

    static class Subgraph {
        State head;
        Transfer outbound;

        public Subgraph(State head, Transfer outbound) {
            this.head = head;
            this.outbound = outbound;
        }

    }

    static class MatchAnyState extends AbstractState {
        Transfer next;

        public MatchAnyState(Transfer next) {
            this.next = next;
        }

        public Transfer next(int c) {
            return next;
        }

        public String toString() {
            return super.toString() + id() + "-*->" + next.toString() + "\n";
        }

    }

    static class InvalidState implements State {
        Transfer self = new TransferImpl(this);

        public Transfer next(int c) {
            return self;
        }

        public Collection<Transfer> adj() {
            return Collections.emptyList();
        }

        public int id() {
            return 0;
        }

        public String toString() {
            return "";
        }

    }

    static abstract class AbstractState implements State {
        List<Transfer> adj = new ArrayList<>();
        int id = -1;

        void setId(int id) {
            this.id = id;
        }

        public List<Transfer> adj() {
            return adj;
        }

        public void register(Transfer s) {
            adj.add(s);
        }

        public int id() {
            assert id >= 2;
            return id;
        }

        public String toString() {
            if (adj.isEmpty()) {
                return "";
            }
            return adj.stream().map(x -> id() + "-->" + x.toString()).collect(Collectors.joining("\n")) + "\n";
        }

    }

    static class Matcher {
        State head;
        State[] states;
        BitSet prev;
        BitSet next;
        int[] leftPrev;
        int[] leftNext;

        public Matcher(State[] states, State head) {
            this.states = states;
            this.head = head;
            prev = new BitSet(states.length);
            next = new BitSet(states.length);
        }

        void dfsUpdate(State root) {
            if (next.get(root.id())) {
                return;
            }
            next.set(root.id());
            for (Transfer t : root.adj()) {
                dfsUpdate(t.get());
            }
        }

        void swap() {
            {
                BitSet tmp = prev;
                prev = next;
                next = tmp;
            }
            {
                int[] tmp = leftNext;
                leftNext = leftPrev;
                leftPrev = tmp;
            }
        }

        public boolean match(CharSequence s, int start, int end) {
            next.clear();
            dfsUpdate(head);
            swap();
            for (int i = start; i < end; i++) {
                next.clear();
                char c = s.charAt(i);
                for (int j = prev.nextSetBit(0); j >= 0; j = prev.nextSetBit(j + 1)) {
                    dfsUpdate(states[j].next(c).get());
                }
                swap();
            }
            return prev.get(1);
        }

    }

    static interface Transfer {
        State get();

        void set(State state);

    }

    static class AcceptState implements State {
        Transfer invalid;

        public AcceptState(Transfer invalid) {
            this.invalid = invalid;
        }

        public Transfer next(int c) {
            return invalid;
        }

        public Collection<Transfer> adj() {
            return Collections.emptyList();
        }

        public int id() {
            return 1;
        }

        public String toString() {
            return "";
        }

    }

    static class FastOutput implements AutoCloseable, Closeable, Appendable {
        private static final int THRESHOLD = 32 << 10;
        private final Writer os;
        private StringBuilder cache = new StringBuilder(THRESHOLD * 2);

        public FastOutput append(CharSequence csq) {
            cache.append(csq);
            return this;
        }

        public FastOutput append(CharSequence csq, int start, int end) {
            cache.append(csq, start, end);
            return this;
        }

        private void afterWrite() {
            if (cache.length() < THRESHOLD) {
                return;
            }
            flush();
        }

        public FastOutput(Writer os) {
            this.os = os;
        }

        public FastOutput(OutputStream os) {
            this(new OutputStreamWriter(os));
        }

        public FastOutput append(char c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput append(String c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput println(String c) {
            return append(c).println();
        }

        public FastOutput println() {
            return append('\n');
        }

        public FastOutput flush() {
            try {
//            boolean success = false;
//            if (stringBuilderValueField != null) {
//                try {
//                    char[] value = (char[]) stringBuilderValueField.get(cache);
//                    os.write(value, 0, cache.length());
//                    success = true;
//                } catch (Exception e) {
//                }
//            }
//            if (!success) {
                os.append(cache);
//            }
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

    static class TransferImpl implements Transfer {
        State state;

        public TransferImpl(State state) {
            this.state = state;
        }

        public TransferImpl() {
        }

        public State get() {
            return state;
        }

        public void set(State state) {
            assert this.state == null;
            this.state = state;
        }

        public String toString() {
            if (state == null) {
                return null;
            }
            return "" + state.id();
        }

    }

    static class RegularExpression {
        State head;
        State[] all;

        public RegularExpression(State[] all, State head) {
            this.all = all;
            this.head = head;
        }

        public Matcher newMatcher() {
            return new Matcher(all, head);
        }

        public String toString() {
            return Arrays.stream(all).map(Objects::toString).collect(Collectors.joining());
        }

    }

    static class MatchNothing extends AbstractState {
        Transfer invalid;

        public MatchNothing(Transfer invalid) {
            this.invalid = invalid;
        }

        public Transfer next(int c) {
            return invalid;
        }

    }

    static class Parser {
        public static RegularExpression parse(CharSequence cs, boolean[] forbidden) {
            assert forbidden.length >= 128;
            return new Parser.ParserHelper(cs, forbidden).parse();
        }

        private static class ParserHelper {
            CharSequence str;
            int rpos = 0;
            State accept;
            State invalid;
            int n;
            Subgraph or = new Subgraph(null, null);
            Subgraph bracket = new Subgraph(null, null);
            List<State> all;
            boolean[] forbidden;
            Deque<Subgraph> dq;

            public ParserHelper(CharSequence str, boolean[] forbidden) {
                this.forbidden = forbidden;
                this.str = str;
                this.n = str.length();
                invalid = new InvalidState();
                accept = new AcceptState(new TransferImpl(invalid));
                all = new ArrayList<>(n + 2);
                all.add(invalid);
                all.add(accept);
            }

            private int read() {
                if (rpos >= n) {
                    return -1;
                }
                return str.charAt(rpos++);
            }

            private void throwExceptionBecauseOfLastCharacter() {
                throw new IllegalArgumentException("Invalid regular expression because of the " + rpos + "-th character");
            }

            private void nextId(AbstractState state) {
                state.setId(all.size());
                all.add(state);
            }

            private Subgraph eval(Subgraph s) {
                if (s == null) {
                    AbstractState state = new MatchNothing(new TransferImpl(invalid));
                    nextId(state);
                    Transfer t = new TransferImpl();
                    state.register(t);
                    s = new Subgraph(state, t);
                }
                return s;
            }

            private Subgraph and(Subgraph a, Subgraph b) {
                if (a == null) {
                    return b;
                }
                if (b == null) {
                    return a;
                }
                a.outbound.set(b.head);
                return new Subgraph(a.head, b.outbound);
            }

            private Subgraph or(Subgraph a, Subgraph b) {
                a = eval(a);
                if (b == null) {
                    return a;
                }
                AbstractState branch = new MatchNothing(new TransferImpl(invalid));
                nextId(branch);
                Transfer t1 = new TransferImpl(a.head);
                Transfer t2 = new TransferImpl(b.head);
                branch.register(t1);
                branch.register(t2);
                return new Subgraph(branch, new MultiTransfer(a.outbound, b.outbound));
            }

            private void compressTail(boolean removeBracket) {
                Subgraph lastOr = null;
                Subgraph lastAnd = null;

                while (!dq.isEmpty()) {
                    Subgraph tail = dq.removeLast();
                    if (tail == or || tail == bracket) {
                        lastOr = or(lastAnd, lastOr);
                        lastAnd = null;

                        if (tail == bracket) {
                            if (!removeBracket) {
                                dq.addLast(tail);
                            }
                            removeBracket = false;
                            break;
                        }
                        continue;
                    }
                    lastAnd = and(tail, lastAnd);
                }
                if (removeBracket) {
                    throwExceptionBecauseOfLastCharacter();
                }
                dq.addLast(eval(lastOr));
            }

            private Subgraph assertRemoveLast() {
                if (dq.isEmpty()) {
                    throwExceptionBecauseOfLastCharacter();
                }
                Subgraph tail = dq.removeLast();
                if (tail == or || tail == bracket) {
                    throwExceptionBecauseOfLastCharacter();
                }
                return tail;
            }

            public RegularExpression parse() {
                dq = new ArrayDeque<>(n + 2);
                dq.addLast(bracket);
                int nc;
                boolean escape = false;
                while ((nc = read()) >= 0) {
                    Subgraph next = null;
                    if (escape) {
                        escape = false;
                        Transfer outbound = new TransferImpl();
                        AbstractState state = new CharacterState(new TransferImpl(invalid), outbound, nc);
                        nextId(state);
                        next = new Subgraph(state, outbound);
                    } else if (nc == '\\' && !forbidden[nc]) {
                        escape = true;
                        continue;
                    } else if (nc == '.' && !forbidden[nc]) {
                        Transfer outbound = new TransferImpl();
                        AbstractState state = new MatchAnyState(outbound);
                        nextId(state);
                        next = new Subgraph(state, outbound);
                    } else if (nc == '|' && !forbidden[nc]) {
                        //cool
                        compressTail(false);
                        dq.addLast(or);
                        continue;
                    } else if (nc == '(' && !forbidden[nc]) {
                        dq.addLast(bracket);
                        continue;
                    } else if (nc == ')' && !forbidden[nc]) {
                        compressTail(true);
                        continue;
                    } else if (nc == '*' && !forbidden[nc]) {
                        Subgraph tail = assertRemoveLast();
                        AbstractState branch = new MatchNothing(new TransferImpl(invalid));
                        nextId(branch);
                        Transfer t1 = new TransferImpl();
                        Transfer t2 = new TransferImpl();
                        branch.register(t1);
                        branch.register(t2);
                        t1.set(tail.head);
                        tail.outbound.set(branch);
                        next = new Subgraph(branch, t2);
                    } else if (nc == '+' && !forbidden[nc]) {
                        Subgraph tail = assertRemoveLast();
                        AbstractState branch = new MatchNothing(new TransferImpl(invalid));
                        nextId(branch);
                        Transfer t1 = new TransferImpl();
                        Transfer t2 = new TransferImpl();
                        branch.register(t1);
                        branch.register(t2);
                        t1.set(tail.head);
                        tail.outbound.set(branch);
                        next = new Subgraph(tail.head, t2);
                    } else if (nc == '?' && !forbidden[nc]) {
                        Subgraph tail = assertRemoveLast();
                        AbstractState branch = new MatchNothing(new TransferImpl(invalid));
                        nextId(branch);
                        Transfer t1 = new TransferImpl();
                        Transfer t2 = new TransferImpl();
                        branch.register(t1);
                        branch.register(t2);
                        t1.set(tail.head);
                        next = new Subgraph(branch, new MultiTransfer(tail.outbound, t2));
                    } else {
                        Transfer outbound = new TransferImpl();
                        AbstractState state = new CharacterState(new TransferImpl(invalid), outbound, nc);
                        nextId(state);
                        next = new Subgraph(state, outbound);
                    }

                    dq.addLast(next);
                }

                compressTail(true);
                if (dq.size() != 1) {
                    throwExceptionBecauseOfLastCharacter();
                }
                if (escape) {
                    throwExceptionBecauseOfLastCharacter();
                }

                Subgraph g = assertRemoveLast();
                g.outbound.set(accept);
                return new RegularExpression(all.toArray(new State[0]), g.head);
            }

        }

    }

    static class MultiTransfer implements Transfer {
        Collection<Transfer> ts;

        public MultiTransfer(Collection<Transfer> ts) {
            this.ts = ts;
        }

        public MultiTransfer(Transfer... ts) {
            this(Arrays.asList(ts));
        }

        public State get() {
            return ts.iterator().next().get();
        }

        public void set(State state) {
            for (Transfer t : ts) {
                t.set(state);
            }
        }

    }

    static class CharacterState extends AbstractState {
        Transfer invalid;
        Transfer next;
        int character;

        public CharacterState(Transfer invalid, Transfer next, int character) {
            this.invalid = invalid;
            this.next = next;
            this.character = character;
        }

        public Transfer next(int c) {
            return c == character ? next : invalid;
        }

        public String toString() {
            return super.toString() + id() + "-" + ((char) character) + "->" + next.toString() + "\n";
        }

    }

    static class FastInput {
        private final InputStream is;
        private StringBuilder defaultStringBuf = new StringBuilder(1 << 13);
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

        public String rs() {
            return readString();
        }

        public String readString(StringBuilder builder) {
            skipBlank();

            while (next > 32) {
                builder.append((char) next);
                next = read();
            }

            return builder.toString();
        }

        public String readString() {
            defaultStringBuf.setLength(0);
            return readString(defaultStringBuf);
        }

        public boolean hasMore() {
            skipBlank();
            return next != -1;
        }

    }
}

