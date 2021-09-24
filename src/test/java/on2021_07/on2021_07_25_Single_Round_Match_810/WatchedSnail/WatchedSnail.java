package on2021_07.on2021_07_25_Single_Round_Match_810.WatchedSnail;



public class WatchedSnail {
    public double maxDistance(int raceTime, int observerCount, int observationTime, int observationDistance) {
        if (raceTime == observationTime) {
            return observationDistance;
        }
        double ans = solve(raceTime - 1, observerCount, observationTime, observationDistance);
        return ans;
    }

    public double solve(int raceTime, int observerCount, int observationTime, int observationDistance) {
        if (observationTime > raceTime) {
            return 0;
        }
        int enable = raceTime / observationTime;
        int max = enable * 2;
        observerCount = Math.min(observerCount, max);
        return (double) observerCount * observationDistance;
    }
}
