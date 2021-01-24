package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.problem.RectUnionArea;

import java.util.*;

public class P2745USACO53WindowArea {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        Map<String, Window> windows = new LinkedHashMap<>();
        int topTime = 0;
        int botTime = -1;
        while (in.hasMore()) {
            String cmd = in.rs();
            String body = cmd.substring(2, cmd.length() - 1).replace("\\s", "");
            String[] args = body.split(",");
            String type = cmd.substring(0, 1);
            String name = args[0];
            if (type.equals("w")) {
                int x = Integer.parseInt(args[1]);
                int y = Integer.parseInt(args[2]);
                int X = Integer.parseInt(args[3]);
                int Y = Integer.parseInt(args[4]);
                RectUnionArea.Rect rect = RectUnionArea.Rect.newInstanceByTwoPoints(x, y, X, Y);
//                rect.r++;
//                rect.t++;
                Window window = new Window();
                window.rect = rect;
                window.time = topTime++;
                windows.put(name, window);
            } else if (type.equals("t")) {
                windows.get(name).time = topTime++;
            } else if (type.equals("b")) {
                windows.get(name).time = botTime--;
            } else if (type.equals("d")) {
                windows.remove(name);
            } else if (type.equals("s")) {
                Window main = windows.get(name);
                List<RectUnionArea.Rect> cover = new ArrayList<>(windows.size());
                for (Window w : windows.values()) {
                    if (w.time <= main.time) {
                        continue;
                    }
                    RectUnionArea.Rect rect = RectUnionArea.Rect.intersect(w.rect, main.rect);
                    if (rect != null) {
                        cover.add(rect);
                    }
                }
                long coverArea = RectUnionArea.unionArea(cover.toArray(new RectUnionArea.Rect[0]));
                long totalArea = main.rect.area();
                double rate = (totalArea - coverArea) / (double) totalArea;
                out.println(String.format("%.3f", rate * 100));
            }
        }
    }
}

class Window {
    RectUnionArea.Rect rect;
    int time;
}