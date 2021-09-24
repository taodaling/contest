package on2021_07.on2021_07_25_Single_Round_Match_810.ToddlerToy;



public class ToddlerToy {
    char[] source = ("###########" +
            "#.........#" +
            "##.#.#.#.##" +
            "##.#.#.#.##" +
            "##.#.#.#.##" +
            "##.#.#.#.##" +
            "###########").toCharArray();

    char[] target = source.clone();
    private int C = 11;
    private int R = 7;

    public int id(int i, int j) {
        return i * R + j;
    }

    StringBuilder ans = new StringBuilder();

    private void apply(int r, int c, char d) {
        ans.append(r).append((char) ('A' + c)).append(d);
        assert Character.isDigit(source[id(r, c)]);
        char v = source[id(r, c)];
        source[id(r, c)] = '.';
        switch (d) {
            case 'L':
                c--;
                break;
            case 'R':
                c++;
                break;
            case 'U':
                r--;
                break;
            case 'D':
                r++;
                break;
        }
        source[id(r, c)] = v;
    }

    public void move(int x0, int y0, int x1, int y1) {
        if (x0 == y0 && x1 == y1) {
            return;
        }
        if (x0 == 1) {
            //move by direction
            if (y0 == y1) {
                apply(x0, y0, 'D');
                x0++;
            } else if (y0 < y1) {
                apply(x0, y0, 'R');
                y0++;
            } else {
                apply(x0, y0, 'L');
                y0--;
            }
        } else if (y0 == y1) {
            apply(x0, y0, 'U');
            x0--;
        }
        move(x0, y0, x1, y1);
    }

    public int[] findSpaceExcept(int a, int b) {
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        for (int i = 0; i < R - 1; i++) {
            for (int j = 0; j < C; j++) {
                if (i == 1 && a <= j && j <= b) {
                    continue;
                }
                if (j == a || j == b) {
                    continue;
                }
                if (source[id(i, j)] == '.' && source[id(i + 1, j)] == '#') {
                    return new int[]{i, j};
                }
            }
        }
        throw new RuntimeException();
    }

    public void whateverMove(int x0, int y0, int x1, int y1) {
        //clean top
        for (int i = 0; i < x0; i++) {
            if (Character.isDigit(source[id(i, y0)])) {
                int[] space = findSpaceExcept(y0, y1);
                move(i, y0, space[0], space[1]);
            }
        }
        for (int i = 0; i < x1; i++) {
            if (Character.isDigit(source[id(i, y1)])) {
                int[] space = findSpaceExcept(y0, y1);
                move(i, y1, space[0], space[1]);
            }
        }
        move(x0, y0, x1, y1);
    }

    public String solve(String[] start, String[] goal) {
        for (int i = 0; i < 4; i++) {
            int y = 2 * (i + 1);
            for (int j = 0; j < 3; j++) {
                int x = 3 + j;
                source[id(x, y)] = start[i].charAt(j);
                target[id(x, y)] = goal[i].charAt(j);
            }
        }
//        for(int i = 0; i < ; i++){
//
//        }
        return "";
    }
}
