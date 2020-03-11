#include "../../libs/common.h"
#include "../../libs/debug.h"


#define MAX_N 200


vector<vector<int>> g;
vector<vector<int>> colors;

void Paint(int i, int j, int c) { colors[i][j] = colors[j][i] = c; }

vector<int> circle;
deque<int> dq;
vector<bool> visited;
vector<bool> handled;
bool SearchCircle(int root, int p) {
  if (visited[root]) {
    while (dq.front() != root) {
      dq.pop_front();
    }
    return true;
  }
  visited[root] = true;
  dq.push_back(root);
  for (int node : g[root]) {
    if (node == p) {
      continue;
    }
    if (SearchCircle(node, root)) {
      return true;
    }
  }
  dq.pop_back();
  return false;
}

void Dfs(int root, int p, int color) {
  Paint(p, root, color);
  if (handled[root]) {
    return;
  }
  handled[root] = true;
  for (int node : g[root]) {
    if (colors[node][root] != -1) {
      continue;
    }
    Dfs(node, root, 1 ^ color);
  }
}

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  g.resize(n);
  visited.resize(n);
  handled.resize(n);
  colors.resize(n, vector<int>(n, -1));
  for (int i = 0; i < n; i++) {
    while (true) {
      int to;
      in >> to;
      if (to == 0) {
        break;
      }
      to--;
      g[i].push_back(to);
    }
  }


  for (int i = 0; i < n; i++) {
    if (handled[i] || g[i].size() != 1) {
      continue;
    }
    handled[i] = true;
    Dfs(g[i][0], i, 0);
    dbg(i);
  }

  for (int i = 0; i < n; i++) {
    if (handled[i] || g[i].size() < 2) {
      continue;
    }
    dq.clear();
    SearchCircle(i, -1);
    circle.assign(dq.begin(), dq.end());
    int find = -1;
    for (int i = 0; i < circle.size(); i++) {
      int node = circle[i];
      handled[node] = true;
      if (g[node].size() > 2) {
        find = i;
      }
    }
    if (find == -1) {
      if (circle.size() % 2 == 1) {
        out << "No solution";
        return;
      }
      find = circle[0];
    }

    dbg(find);
    dbg(circle);
    int m = circle.size();
    for (int i = 0; i < m; i++) {
      int cur = circle[(find + i) % m];
      int next = circle[(find + i + 1) % m];
      Paint(cur, next, i & 1);
    }

    dbg(colors[0][4], colors[0][1]);
    dbg(colors[1][2], colors[0][1]);
    for (int node : circle) {
      for (int next : g[node]) {
        if (colors[node][next] != -1) {
          continue;
        }
        Dfs(next, node, 1);
      }
    }
  }

  for (int i = 0; i < n; i++) {
    for (int node : g[i]) {
      out << colors[i][node] + 1 << ' ';
    }
    out << 0 << endl;
  }
}

RUN_ONCE