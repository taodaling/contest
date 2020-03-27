#ifndef COMMON_H
#define COMMON_H

#include <bits/stdc++.h>
#include <algorithm>
#include <chrono>
#include <complex>
#include <ext/rope>
#include <fstream>
#include <iomanip>
#include <iostream>
#include <queue>
#include <random>
#ifndef COMPILER_MACRO_H
#define COMPILER_MACRO_H

#ifndef LOCAL

#pragma GCC diagnostic error "-std=c++14"
#pragma GCC target("avx")
#pragma GCC optimize(3)
#pragma GCC optimize("Ofast")
#pragma GCC target("sse4.2")
#pragma GCC optimize("inline")

#endif

#endif

using __gnu_cxx::rope;
using std::array;
using std::bitset;
using std::cerr;
using std::complex;
using std::deque;
using std::endl;
using std::fill;
using std::function;
using std::ios_base;
using std::istream;
using std::istream_iterator;
using std::iterator;
using std::make_pair;
using std::make_tuple;
using std::map;
using std::max;
using std::min;
using std::numeric_limits;
using std::ostream;
using std::pair;
using std::priority_queue;
using std::queue;
using std::reverse;
using std::rotate;
using std::set;
using std::sort;
using std::string;
using std::stringstream;
using std::swap;
using std::uniform_int_distribution;
using std::uniform_real_distribution;
using std::unique;
using std::unordered_map;
using std::vector;
using std::tuple;
using std::get;
using std::multiset;
using std::multimap;

typedef unsigned int ui;
typedef long long ll;
typedef long double ld;
typedef unsigned long long ull;
typedef unsigned char byte;
std::mt19937 rng(std::chrono::steady_clock::now().time_since_epoch().count());

const double E = 2.7182818284590452354;
const double PI = 3.14159265358979323846;

#define mp make_pair

#ifdef LOCAL
#define PREPARE_INPUT                                  \
  {                                                    \
    std::cout << "Input file name:";                   \
    string file;                                       \
    std::cin >> file;                                  \
    if (file != "stdin") {                             \
      file = string(__FILE__) + "/../" + file + ".in"; \
      std::cout << "Open file:" << file << std::endl;  \
      freopen(file.data(), "r", stdin);                \
    }                                                  \
  }
#else
#define PREPARE_INPUT
#endif

#define RUN_ONCE                                    \
  int main() {                                      \
    PREPARE_INPUT;                                  \
    std::ios_base::sync_with_stdio(false);          \
    std::cin.tie(0);                                \
    std::cout << std::setiosflags(std::ios::fixed); \
    std::cout << std::setprecision(15);             \
    solve(1, std::cin, std::cout);                  \
    return 0;                                       \
  }

#define RUN_MULTI                                   \
  int main() {                                      \
    PREPARE_INPUT;                                  \
    std::ios_base::sync_with_stdio(false);          \
    std::cin.tie(0);                                \
    std::cout << std::setiosflags(std::ios::fixed); \
    std::cout << std::setprecision(15);             \
    int t;                                          \
    std::cin >> t;                                  \
    for (int i = 1; i <= t; i++) {                  \
      solve(i, std::cin, std::cout);                \
    }                                               \
    return 0;                                       \
  }

#endif

#define C0(x) memset(x, 0, sizeof(x))
#define C1(x) memset(x, -1, sizeof(x))
#ifndef MATCH_H
#define MATCH_H



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

using match::BipartiteMatch;

int n;

int IdOf(int i, int j) { return i * n + j; }

void solve(int testId, istream &in, ostream &out) {
  int p;
  in >> n >> p;
  vector<vector<bool>> mat(n, vector<bool>(n));
  for (int i = 0; i < p; i++) {
    int x, y;
    in >> x >> y;
    mat[x - 1][y - 1] = true;
  }

  BipartiteMatch bm(n * n, n * n);
  vector<vector<int>> dirs{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      if (mat[i][j] || (i + j) % 2 == 1) {
        continue;
      }
      for (auto &d : dirs) {
        int ni = i + d[0];
        int nj = j + d[1];
        if (ni < 0 || nj < 0 || ni >= n || nj >= n || mat[ni][nj]) {
          continue;
        }
        bm.addEdge(IdOf(i, j), IdOf(ni, nj), true);
      }
    }
  }

  int match = 0;
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      if (mat[i][j] || (i + j) % 2 == 1) {
        continue;
      }
      match += bm.matchLeft(IdOf(i, j)) >= 0;
    }
  }

  if (match * 2 + p != n * n) {
    out << "No";
    return;
  }
  out << "Yes" << endl;
  vector<pair<int, int>> vertical;
  vector<pair<int, int>> horizontal;
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      if (mat[i][j] || (i + j) % 2 == 1) {
        continue;
      }
      int op = bm.matchLeft(IdOf(i, j));
      int r = op / n;
      int c = op % n;
      if (r == i) {
        horizontal.emplace_back(i, min(j, c));
      } else {
        vertical.emplace_back(min(i, r), j);
      }
    }
  }

  out << vertical.size() << endl;
  for(auto &p : vertical){
    out << p.first + 1 << ' ' << p.second + 1 << endl;
  }
  out << horizontal.size() << endl;
  for (auto &p : horizontal) {
    out << p.first + 1 << ' ' << p.second + 1 << endl;
  }
}

RUN_ONCE