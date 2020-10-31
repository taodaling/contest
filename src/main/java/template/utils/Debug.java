package template.utils;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.function.Supplier;

public class Debug {
    private boolean offline;
    private PrintStream out = System.err;
    private long time = System.currentTimeMillis();

    public Debug(boolean enable) {
        offline = enable && System.getSecurityManager() == null;
    }

    public void run(Runnable task) {
        if (offline) {
            task.run();
        }
    }

    public Debug debug(String name, Supplier<Object> supplier) {
        if (offline) {
            debug(name, supplier.get());
        }
        return this;
    }

    public Debug elapse(String name) {
        if (offline) {
            debug(name, System.currentTimeMillis() - time);
            time = System.currentTimeMillis();
        }
        return this;
    }

    public Debug debug(String name, int x) {
        if (offline) {
            debug(name, "" + x);
        }
        return this;
    }

    public Debug debug(String name, long x) {
        if (offline) {
            debug(name, "" + x);
        }
        return this;
    }


    public Debug debug(String name, double x) {
        if (offline) {
            debug(name, "" + x);
        }
        return this;
    }

    public Debug debug(String name, char x) {
        if (offline) {
            debug(name, Integer.toString(x));
        }
        return this;
    }


    public Debug debug(String name, boolean x) {
        if (offline) {
            debug(name, "" + x);
        }
        return this;
    }

    public Debug debug(String name, String x) {
        if (offline) {
            out.printf("%s=%s", name, x);
            out.println();
        }
        return this;
    }


    static int[] empty = new int[0];

    public Debug debug(String name, Object x) {
        return debug(name, x, empty);
    }

    public Debug debug(String name, Object x, ArrayIndex ai) {
        if (offline) {
            if (x == null) {
                debug(name, x);
                return this;
            }
            if (!x.getClass().isArray()) {
                throw new IllegalArgumentException();
            }
            if (x instanceof byte[]) {
                byte[] arr = (byte[]) x;
                for (int i = 0; i < arr.length; i++) {
                    int[] indexes = ai.inverse(i);
                    debug(name, arr[i], indexes);
                }
            } else if (x instanceof short[]) {
                short[] arr = (short[]) x;
                for (int i = 0; i < arr.length; i++) {
                    int[] indexes = ai.inverse(i);
                    debug(name, arr[i], indexes);
                }
            } else if (x instanceof boolean[]) {
                boolean[] arr = (boolean[]) x;
                for (int i = 0; i < arr.length; i++) {
                    int[] indexes = ai.inverse(i);
                    debug(name, arr[i], indexes);
                }
            } else if (x instanceof char[]) {
                char[] arr = (char[]) x;
                for (int i = 0; i < arr.length; i++) {
                    int[] indexes = ai.inverse(i);
                    debug(name, arr[i], indexes);
                }
            } else if (x instanceof int[]) {
                int[] arr = (int[]) x;
                for (int i = 0; i < arr.length; i++) {
                    int[] indexes = ai.inverse(i);
                    debug(name, arr[i], indexes);
                }
            } else if (x instanceof float[]) {
                float[] arr = (float[]) x;
                for (int i = 0; i < arr.length; i++) {
                    int[] indexes = ai.inverse(i);
                    debug(name, arr[i], indexes);
                }
            } else if (x instanceof double[]) {
                double[] arr = (double[]) x;
                for (int i = 0; i < arr.length; i++) {
                    int[] indexes = ai.inverse(i);
                    debug(name, arr[i], indexes);
                }
            } else if (x instanceof long[]) {
                long[] arr = (long[]) x;
                for (int i = 0; i < arr.length; i++) {
                    int[] indexes = ai.inverse(i);
                    debug(name, arr[i], indexes);
                }
            } else {
                Object[] arr = (Object[]) x;
                for (int i = 0; i < arr.length; i++) {
                    int[] indexes = ai.inverse(i);
                    debug(name, arr[i], indexes);
                }
            }
        }
        return this;
    }


    public Debug debug(String name, Object x, int... indexes) {
        if (offline) {
            if (x == null || !x.getClass().isArray()) {
                out.append(name);
                for (int i : indexes) {
                    out.printf("[%d]", i);
                }
                out.append("=").append("" + x);
                out.println();
            } else {
                indexes = Arrays.copyOf(indexes, indexes.length + 1);
                if (x instanceof byte[]) {
                    byte[] arr = (byte[]) x;
                    for (int i = 0; i < arr.length; i++) {
                        indexes[indexes.length - 1] = i;
                        debug(name, arr[i], indexes);
                    }
                } else if (x instanceof short[]) {
                    short[] arr = (short[]) x;
                    for (int i = 0; i < arr.length; i++) {
                        indexes[indexes.length - 1] = i;
                        debug(name, arr[i], indexes);
                    }
                } else if (x instanceof boolean[]) {
                    boolean[] arr = (boolean[]) x;
                    for (int i = 0; i < arr.length; i++) {
                        indexes[indexes.length - 1] = i;
                        debug(name, arr[i], indexes);
                    }
                } else if (x instanceof char[]) {
                    char[] arr = (char[]) x;
                    for (int i = 0; i < arr.length; i++) {
                        indexes[indexes.length - 1] = i;
                        debug(name, arr[i], indexes);
                    }
                } else if (x instanceof int[]) {
                    int[] arr = (int[]) x;
                    for (int i = 0; i < arr.length; i++) {
                        indexes[indexes.length - 1] = i;
                        debug(name, arr[i], indexes);
                    }
                } else if (x instanceof float[]) {
                    float[] arr = (float[]) x;
                    for (int i = 0; i < arr.length; i++) {
                        indexes[indexes.length - 1] = i;
                        debug(name, arr[i], indexes);
                    }
                } else if (x instanceof double[]) {
                    double[] arr = (double[]) x;
                    for (int i = 0; i < arr.length; i++) {
                        indexes[indexes.length - 1] = i;
                        debug(name, arr[i], indexes);
                    }
                } else if (x instanceof long[]) {
                    long[] arr = (long[]) x;
                    for (int i = 0; i < arr.length; i++) {
                        indexes[indexes.length - 1] = i;
                        debug(name, arr[i], indexes);
                    }
                } else {
                    Object[] arr = (Object[]) x;
                    for (int i = 0; i < arr.length; i++) {
                        indexes[indexes.length - 1] = i;
                        debug(name, arr[i], indexes);
                    }
                }
            }
        }
        return this;
    }
}
