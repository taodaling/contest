#ifndef MATCH_H
#define MATCH_H

#include "common.h"

namespace match {
class BipartiteMatch {
 private:
  vector<int> _partner[2];
  vector<vector<int>> _g[2];
  vector<int> _time[2];
  int _now;

  bool release(int side, int i) {
    if (_time[side][i] == _now) {
      return false;
    }
    _time[side][i] = _now;
    if (_partner[side][i] == -1) {
      return true;
    }
    return bind(1 ^ side, _partner[side][i]);
  }
  bool bind(int side, int i) {
    if (_time[side][i] == _now) {
      return false;
    }
    _time[side][i] = _now;
    for (int node : _g[side][i]) {
      if (release(1 ^ side, node)) {
        _partner[side][i] = node;
        _partner[1 ^ side][node] = i;
        return true;
      }
    }
    return false;
  }

  void dfs(int side, int node) {
    if (_time[side][node] == _now) {
      return;
    }
    _time[side][node] = _now;
    for (int next : _g[side][node]) {
      dfs(1 ^ side, next);
    }
  }

 public:
  BipartiteMatch(int l, int r) {
    int size[]{l, r};
    _now = 0;
    for (int i = 0; i < 2; i++) {
      _partner[i].resize(size[i]);
      _time[i].resize(size[i]);
      _g[i].resize(size[i]);
      fill(_partner[i].begin(), _partner[i].end(), -1);
    }
  }

  void addEdge(int a, int b, bool greedy) {
    _g[0][a].push_back(b);
    _g[1][b].push_back(a);
    if (greedy && _partner[0][a] == -1 && _partner[1][b] == -1) {
      _partner[0][a] = b;
      _partner[1][b] = a;
    }
  }

  int matchLeft(int i) {
    if (_partner[0][i] == -1) {
      _now++;
      bind(0, i);
    }
    return _partner[0][i];
  }
  int matchRight(int i) {
    if (_partner[1][i] == -1) {
      _now++;
      bind(1, i);
    }
    return _partner[1][i];
  }

  void minVertexCover(vector<vector<bool>> &status) {
    _now++;
    for (int i = 0; i < _time[1].size(); i++) {
      if (_partner[1][i] == -1) {
        dfs(1, i);
      }
    }
    for (int i = 0; i < 2; i++) {
      fill(status[i].begin(), status[i].end(), false);
    }
    for (int i = 0; i < _time[0].size(); i++) {
      status[0][i] = _time[0][i] == _now;
    }
    for (int i = 0; i < _time[1].size(); i++) {
      status[1][i] = _time[1][i] != _now;
    }
  }
};

template <int N>
struct GeneralMatch {
 private:
  int pre[N + 1];
  bool edges[N + 1][N + 1];
  int mate[N + 1];
  int link[N + 1];
  int vis[N + 1];
  int fa[N + 1];
  int que[N + 1];
  int hd;
  int tl;
  int ss[N + 1];
  int tim;
  int find(int x) { return fa[x] == x ? x : (fa[x] = find(fa[x])); }

  int lca(int x, int y) {
    ++tim;
    while (ss[x] != tim) {
      if (x != 0) {
        ss[x] = tim;
        x = find(link[mate[x]]);
      }
      int tmp = x;
      x = y;
      y = tmp;
    }
    return x;
  }

  void flower(int x, int y, int p) {
    while (find(x) != p) {
      link[x] = y;
      fa[y = mate[x]] = fa[x] = p;
      if (vis[y] == 1) vis[que[tl++] = y] = 2;
      x = link[y];
    }
  }

  bool match(int x) {
    hd = tl = 0;
    for (int i = 1; i <= N; ++i) vis[fa[i] = i] = 0;
    vis[que[tl++] = x] = 2;
    while (hd < tl) {
      x = que[hd++];
      for (int u = 1; u <= N; u++) {
        if (!edges[x][u]) {
          continue;
        }
        if (0 == vis[u]) {
          vis[u] = 1;
          link[u] = x;
          if (0 == mate[u]) {
            while (0 != x) {
              x = mate[link[u]];
              mate[mate[u] = link[u]] = u;
              u = x;
            }
            return true;
          } else
            vis[que[tl++] = mate[u]] = 2;
        } else if (vis[u] == 2 && find(u) != find(x)) {
          int p = lca(x, u);
          flower(x, u, p);
          flower(u, x, p);
        }
      }
    }
    return false;
  }

 public:
  void reset() {
    C0(pre);
    C0(edges);
    C0(mate);
    C0(link);
    C0(vis);
    C0(fa);
    C0(que);
    C0(ss);
    hd = 0;
    tl = 0;
    tim = 0;
  }

  /**
   * -1 represent no mate
   */
  inline int mateOf(int i) { return mate[i + 1] - 1; }

  inline void addEdge(int x, int y) {
    edges[x + 1][y + 1] = edges[y + 1][x + 1] = true;
  }

  int maxMatch() {
    int total = 0;
    for (int i = 1; i <= N; i++) {
      for (int j = i + 1; j <= N; j++) {
        if (edges[i][j] && mate[i] == 0 && mate[j] == 0) {
          mate[i] = j;
          mate[j] = i;
          total++;
        }
      }
    }

    for (int i = 1; i <= N; i++) {
      if (mate[i] == 0 && match(i)) {
        total++;
      }
    }
    return total;
  }
};
}  // namespace match

#endif