package template.utils;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.function.Supplier;

public class Debug {
    private boolean offline;
    private PrintStream out = System.err;
    private long time = System.currentTimeMillis();
    private long begin = System.currentTimeMillis();

    public Debug(boolean enable) {
        offline = enable && System.getSecurityManager() == null;
    }

    public boolean enable() {
        return offline;
    }

    public void run(Runnable task) {
        if (offline) {
            task.run();
        }
    }


    public void log(String name) {
        if (offline) {
            out.println(name);
        }
    }

    public Debug debugMatrix(String name, int[][] matrix) {
        if (offline) {
            StringBuilder content = new StringBuilder("\n");
            for (int[] row : matrix) {
                for (int cell : row) {
                    content.append(cell).append(' ');
                }
                content.append(System.lineSeparator());
            }
            debug(name, content);
        }
        return this;
    }

    public Debug debugMatrix(String name, boolean[][] matrix) {
        if (offline) {
            StringBuilder content = new StringBuilder("\n");
            for (boolean[] row : matrix) {
                for (boolean cell : row) {
                    content.append(cell ? 1 : 0).append(' ');
                }
                content.append(System.lineSeparator());
            }
            debug(name, content);
        }
        return this;
    }

    public Debug debugMatrix(String name, char[][] matrix) {
        if (offline) {
            StringBuilder content = new StringBuilder("\n");
            for (char[] row : matrix) {
                for (char cell : row) {
                    content.append(cell);
                }
                content.append(System.lineSeparator());
            }
            debug(name, content);
        }
        return this;
    }

    public Debug debugMatrix(String name, long[][] matrix) {
        if (offline) {
            StringBuilder content = new StringBuilder("\n");
            for (long[] row : matrix) {
                for (long cell : row) {
                    content.append(cell).append(' ');
                }
                content.append(System.lineSeparator());
            }
            debug(name, content);
        }
        return this;
    }

    public Debug debugMatrix(String name, double[][] matrix) {
        if (offline) {
            StringBuilder content = new StringBuilder("\n");
            for (double[] row : matrix) {
                for (double cell : row) {
                    content.append(cell).append(' ');
                }
                content.append(System.lineSeparator());
            }
            debug(name, content);
        }
        return this;
    }

    public Debug debugArray(String name, int[] matrix) {
        if (offline) {
            debug(name, Arrays.toString(matrix));
        }
        return this;
    }

    public Debug debugArray(String name, boolean[] matrix) {
        if (offline) {
            debug(name, Arrays.toString(matrix));
        }
        return this;
    }

    public Debug debugArray(String name, Object[] matrix) {
        if (offline) {
            debug(name, Arrays.toString(matrix));
        }
        return this;
    }

    public Debug debugArray(String name, char[] matrix) {
        if (offline) {
            debug(name, String.valueOf(matrix));
        }
        return this;
    }

    public Debug debugArray(String name, long[] matrix) {
        if (offline) {
            debug(name, Arrays.toString(matrix));
        }
        return this;
    }

    public Debug debugArray(String name, double[] matrix) {
        if (offline) {
            debug(name, Arrays.toString(matrix));
        }
        return this;
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

    public Debug summary() {
        if (offline) {
            debug("used time", (System.currentTimeMillis() - begin) + "ms");
            debug("used memory", (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) >> 20);
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
            debug(name, "" + x);
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
