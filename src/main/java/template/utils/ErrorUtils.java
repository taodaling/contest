package template.utils;

public class ErrorUtils {
    public static void understand(Throwable t) {
        if (t instanceof ArithmeticException) {
            while (true) {

            }
        }
        if (t instanceof StackOverflowError) {
            return;
        }
    }
}
