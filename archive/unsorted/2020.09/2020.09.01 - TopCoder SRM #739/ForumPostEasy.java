package contest;

import java.util.HashMap;
import java.util.Map;

public class ForumPostEasy {
    public String getCurrentTime(String[] exactPostTime, String[] showPostTime) {
        int[] times = new int[exactPostTime.length];
        for (int i = 0; i < times.length; i++) {
            String[] split = exactPostTime[i].split(":");
            times[i] = (Integer.parseInt(split[0]) * 60 + Integer.parseInt(split[1])) * 60 + Integer.parseInt(split[2]);
        }

        String ans = "9";
        for (int i = 0; i < 24 * 60 * 60; i++) {
            boolean valid = true;
            for (int j = 0; j < times.length; j++) {
                if (!showPostTime[j].equals(msg(times[j], i))) {
                    valid = false;
                    break;
                }
            }
            if (!valid) {
                continue;
            }
            String date = format(i / 3600) + ":" + format(i % 3600 / 60) + ":" + format(i % 60);
            if (date.compareTo(ans) < 0) {
                ans = date;
            }
        }

        return ans.equals("9") ? "impossible" : ans;
    }

    String[] hourMsg = new String[24];
    String[] minuteMsg = new String[60];


    public String format(int x) {
        if (x < 10) {
            return "0" + x;
        }
        return "" + x;
    }

    public String msg(int time, int now) {
        if (now < time) {
            now += 24 * 60 * 60;
        }
        if (now - time <= 59) {
            return "few seconds ago";
        }
        if (now - time < 60 * 60) {
            int minute = (now - time) / 60;
            if (minuteMsg[minute] == null) {
                minuteMsg[minute] = minute + " minutes ago";
            }
            return minuteMsg[minute];
        }
        int hour = (now - time) / 3600;
        if (hourMsg[hour] == null) {
            hourMsg[hour] = hour + " hours ago";
        }
        return hourMsg[hour];
    }
}
