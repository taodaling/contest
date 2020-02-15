package template.utils;

import java.io.PrintStream;
import java.util.List;

public class StringUtils {
    public static void printLine(StringBuilder builder, Object... vals) {
        for (Object val : vals) {
            builder.append(val).append(' ');
        }
        builder.append(System.lineSeparator());
    }

    public static void printLine(PrintStream ps, Object... vals) {
        for (Object val : vals) {
            ps.print(val);
            ps.print(' ');
        }
        ps.append(System.lineSeparator());
    }

    public static void debug(String name, Object val) {
        printLine(System.err, name, "=", val);
    }

}
