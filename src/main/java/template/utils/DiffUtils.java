package template.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class DiffUtils {
    private DiffUtils() {
    }

    public static <T> List<T> longestCommonSubSequence(List<T> a, List<T> b) {
        List<T> result = new ArrayList<>();
        new MyersAlgoLinearVariation<>(a, b).generateLCS(result::add);
        return result;
    }

    public static <T> void shortestEditScript(List<T> source, List<T> target, EditScriptRecorder<T> recorder) {
        List<T> lcs = longestCommonSubSequence(source, target);
        lcs.add((T) new Object());
        int sIndex = 0;
        int tIndex = 0;
        int lIndex = 0;
        int sLen = source.size();
        int tLen = target.size();
        int lLen = lcs.size();

        while (lIndex < lLen) {
            T lItem = lcs.get(lIndex);
            while (sIndex < sLen) {
                if (Objects.equals(source.get(sIndex), lItem)) {
                    sIndex++;
                    break;
                } else {
                    recorder.delete();
                    sIndex++;
                }
            }
            while (tIndex < tLen) {
                if (Objects.equals(target.get(tIndex), lItem)) {
                    recorder.accept();
                    tIndex++;
                    break;
                } else {
                    recorder.add(target.get(tIndex));
                    tIndex++;
                }
            }
            lIndex++;
        }
    }

    private static class MyersAlgoLinearVariation<T> {
        private List<T> a;
        private List<T> b;
        private RangeArray furthest00;
        private RangeArray furthestnm;
        private Consumer<T> consumer;

        public MyersAlgoLinearVariation(List<T> a, List<T> b) {
            this.a = a;
            this.b = b;
            furthest00 = new RangeArray(-b.size(), a.size());
            furthestnm = new RangeArray(-b.size(), a.size());
        }

        public void generateLCS(Consumer<T> consumer) {
            this.consumer = consumer;
            lcs(a, b);
        }

        private void output(List<T> list) {
            for (T t : list) {
                consumer.accept(t);
            }
        }

        private void lcs0(List<T> a, List<T> b, int i, int u, int x, int d) {
            int n = a.size();
            int m = b.size();
            int y = x - i;
            int v = u - i;
            if (d > 1) {
                lcs(a.subList(0, u), b.subList(0, v));
                output(a.subList(u, x));
                lcs(a.subList(x, n), b.subList(y, m));
            } else if (n < m) {
                output(a);
            } else {
                output(b);
            }
        }

        private void lcs(List<T> a, List<T> b) {
            int n = a.size();
            int m = b.size();
            if (n == 0 || m == 0) {
                return;
            }
            furthest00.fill(-1, -m, n);
            furthestnm.fill(n + 1, -m, n);
            for (int d = 0; ; d++) {
                for (int left = -d, right = d, i = left; i <= right; i += 2) {
                    if (i > n) {
                        continue;
                    }
                    if (i < -m) {
                        continue;
                    }
                    int x = 0;
                    if (i > left && i > -m) {
                        x = Math.max(x, Math.min(furthest00.get(i - 1) + 1, n));
                    }
                    if (i < right && i < n) {
                        x = Math.max(x, furthest00.get(i + 1));
                    }
                    int y = x - i;
                    while (x < n && y < m && Objects.equals(a.get(x), b.get(y))) {
                        x++;
                        y++;
                    }
                    furthest00.set(i, x);
                    if (furthest00.get(i) >= furthestnm.get(i)) {
                        lcs0(a, b, i, furthestnm.get(i), furthest00.get(i), d * 2 - 1);
                        return;
                    }
                }
                for (int left = -d + n - m, right = d + n - m, i = left; i <= right; i += 2) {
                    if (i > n) {
                        continue;
                    }
                    if (i < -m) {
                        continue;
                    }
                    int x = n;
                    if (i > left && i > -m) {
                        x = Math.min(x, furthestnm.get(i - 1));
                    }
                    if (i < right && i < n) {
                        x = Math.min(x, Math.max(0, furthestnm.get(i + 1) - 1));
                    }
                    int y = x - i;
                    while (x > 0 && y > 0 && Objects.equals(a.get(x - 1), b.get(y - 1))) {
                        x--;
                        y--;
                    }
                    furthestnm.set(i, x);
                    if (furthest00.get(i) >= furthestnm.get(i)) {
                        lcs0(a, b, i, furthestnm.get(i), furthest00.get(i), d * 2);
                        return;
                    }
                }
            }
        }
    }

    private static class RangeArray {
        private int offset;
        private int[] data;

        public RangeArray(int left, int right) {
            this.offset = -left;
            this.data = new int[right - left + 1];
        }

        public int get(int i) {
            return data[i + offset];
        }

        public void set(int i, int val) {
            data[i + offset] = val;
        }

        public void fill(int val, int l, int r) {
            Arrays.fill(data, offset + l, offset + r + 1, val);
        }
    }

    public static interface EditScriptRecorder<T> {
        void add(T t);

        void delete();

        void accept();
    }
}
