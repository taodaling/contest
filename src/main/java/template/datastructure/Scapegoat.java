package template.datastructure;

import template.math.DigitUtils;

import java.util.ArrayList;
import java.util.List;

public class Scapegoat implements Cloneable {
        public static final Scapegoat NIL = new Scapegoat();
        public static final double FACTOR = 0.75;
        public static final List<Scapegoat> RECORDER = new ArrayList();

        Scapegoat left = NIL;
        Scapegoat right = NIL;
        int size;
        int key;
        int count;
        int total;
        int trashed;

        static {
            NIL.left = NIL.right = NIL;
            NIL.size = NIL.key = NIL.count = 0;
        }

        public void pushUp() {
            size = left.size + right.size + 1;
            trashed = left.trashed + right.trashed + (count == 0 ? 1 : 0);
            total = left.total + right.total + count;
        }

        public void pushDown() {
            left = check(left);
            right = check(right);
        }

        private Scapegoat() {
        }

        private Scapegoat(int key) {
            this.key = key;
            this.count = 1;
            pushUp();
        }

        public Scapegoat insert(int x) {
            if (this == NIL) {
                return new Scapegoat(x);
            }
            pushDown();
            if (key == x) {
                count++;
            } else if (key >= x) {
                left = left.insert(x);
            } else {
                right = right.insert(x);
            }
            pushUp();
            return this;
        }

        public Scapegoat delete(int x) {
            if (this == NIL) {
                return NIL;
            }
            pushDown();
            if (key == x) {
                count = Math.max(count - 1, 0);
            } else if (key >= x) {
                left = left.delete(x);
            } else {
                right = right.delete(x);
            }
            pushUp();
            return this;
        }

        @Override
        protected Scapegoat clone() {
            if (this == NIL) {
                return NIL;
            }
            try {
                Scapegoat scapegoat = (Scapegoat) super.clone();
                scapegoat.left = scapegoat.left.clone();
                scapegoat.right = scapegoat.right.clone();
                return scapegoat;
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        public int numberOfElementLessThanOrEqualTo(int x) {
            if (this == NIL) {
                return 0;
            }
            pushDown();
            int ans;
            if (key == x) {
                ans = total - right.total;
            } else if (key > x) {
                ans = left.numberOfElementLessThanOrEqualTo(x);
            } else {
                ans = total - right.total + right.numberOfElementLessThanOrEqualTo(x);
            }
            pushUp();
            return ans;
        }

        public int theKthSmallestElement(int k) {
            if (this == NIL) {
                return -1;
            }
            pushDown();
            int ans;
            if (k <= left.total) {
                ans = left.theKthSmallestElement(k);
            } else {
                k -= total - right.total;
                if (k <= 0) {
                    ans = key;
                } else {
                    ans = right.theKthSmallestElement(k);
                }
            }
            pushUp();
            return ans;
        }

        private static Scapegoat check(Scapegoat root) {
            double limit = (root.size - root.trashed) * FACTOR;
            if (root.left.size > limit || root.right.size > limit) {
                return refactor(root);
            }
            return root;
        }

        private static Scapegoat refactor(Scapegoat root) {
            RECORDER.clear();
            travel(root);
            return rebuild(0, RECORDER.size() - 1);
        }

        private void init() {
        }

        private static Scapegoat rebuild(int l, int r) {
            if (l > r) {
                return NIL;
            }
            int m = DigitUtils.floorAverage(l, r);
            Scapegoat root = RECORDER.get(m);
            root.init();
            root.left = rebuild(l, m - 1);
            root.right = rebuild(m + 1, r);
            root.pushUp();
            return root;
        }

        private static void travel(Scapegoat root) {
            if (root == NIL) {
                return;
            }
            travel(root.left);
            if (root.count > 0) {
                RECORDER.add(root);
            }
            travel(root.right);
        }

        public void toString(StringBuilder builder) {
            if (this == NIL) {
                return;
            }
            pushDown();
            left.toString(builder);
            for (int i = 0; i < count; i++) {
                builder.append(key).append(',');
            }
            right.toString(builder);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            clone().toString(builder);
            if (builder.length() > 0) {
                builder.setLength(builder.length() - 1);
            }
            return builder.toString();
        }
    }