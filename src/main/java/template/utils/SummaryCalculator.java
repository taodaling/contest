package template.utils;

public interface SummaryCalculator {
    long add(long prev, long contrib);

    long remove(long prev, long contrib);

    public static class NIL implements SummaryCalculator {
        static final NIL instance = new NIL();

        public static NIL getInstance() {
            return instance;
        }

        @Override
        public long add(long prev, long contrib) {
            return 0;
        }

        @Override
        public long remove(long prev, long contrib) {
            return 0;
        }
    }
}
