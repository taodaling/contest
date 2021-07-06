package template.polynomial;

public class FWTLayers {
    private static abstract class Level {
        Level next;

        public abstract void apply(long[] a, int l, int r);

        public abstract void inverse(long[] a, int l, int r);
    }

    private Level top = NIL.INSTANCE;

    public void apply(long[] a, int l, int r) {
        top.apply(a, l, r);
    }

    public void inverse(long[] a, int l, int r) {
        top.inverse(a, l, r);
    }

    public void addAndLayer() {
        addLayer(new And());
    }

    public void addOrLayer() {
        addLayer(new Or());
    }

    public void addXorLayer() {
        addLayer(new Xor());
    }

    private void addLayer(Level newTop) {
        newTop.next = top;
        top = newTop;
    }


    private static class NIL extends Level {
        static final NIL INSTANCE = new NIL();

        @Override
        public void apply(long[] a, int l, int r) {
        }

        @Override
        public void inverse(long[] a, int l, int r) {
        }
    }

    private static class And extends Level {
        @Override
        public void apply(long[] p, int l, int r) {
            int m = (l + r) / 2;
            next.apply(p, l, m);
            next.apply(p, m + 1, r);
            for (int i = 0, until = m - l; i <= until; i++) {
                long a = p[l + i];
                long b = p[m + 1 + i];
                p[l + i] = a + b;
            }
        }

        @Override
        public void inverse(long[] p, int l, int r) {
            int m = (l + r) / 2;
            for (int i = 0, until = m - l; i <= until; i++) {
                long a = p[l + i];
                long b = p[m + 1 + i];
                p[l + i] = a - b;
            }
            next.inverse(p, l, m);
            next.inverse(p, m + 1, r);
        }
    }

    private static class Or extends Level {
        @Override
        public void apply(long[] p, int l, int r) {
            int m = (l + r) / 2;
            next.apply(p, l, m);
            next.apply(p, m + 1, r);
            for (int i = 0, until = m - l; i <= until; i++) {
                long a = p[l + i];
                long b = p[m + 1 + i];
                p[m + 1 + i] = a + b;
            }
        }

        @Override
        public void inverse(long[] p, int l, int r) {
            int m = (l + r) / 2;
            for (int i = 0, until = m - l; i <= until; i++) {
                long a = p[l + i];
                long b = p[m + 1 + i];
                p[m + 1 + i] = b - a;
            }
            next.inverse(p, l, m);
            next.inverse(p, m + 1, r);
        }
    }

    private static class Xor extends Level {
        @Override
        public void apply(long[] p, int l, int r) {
            int m = (l + r) / 2;
            next.apply(p, l, m);
            next.apply(p, m + 1, r);
            for (int i = 0, until = m - l; i <= until; i++) {
                long a = p[l + i];
                long b = p[m + 1 + i];
                p[l + i] = a + b;
                p[m + 1 + i] = a - b;
            }
        }

        @Override
        public void inverse(long[] p, int l, int r) {
            int m = (l + r) / 2;
            for (int i = 0, until = m - l; i <= until; i++) {
                long a = p[l + i];
                long b = p[m + 1 + i];
                p[l + i] = (a + b) / 2;
                p[m + 1 + i] = (a - b) / 2;
            }
            next.inverse(p, l, m);
            next.inverse(p, m + 1, r);
        }
    }
}
