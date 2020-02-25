#ifndef COMMON_H
#define COMMON_H

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
#include <bits/stdc++.h>
#include <chrono>
#include <ext/rope>
#include <fstream>
#include <iomanip>
#include <iostream>
#include <queue>
#include <random>

using __gnu_cxx::rope;
using std::bitset;
using std::cerr;
using std::deque;
using std::endl;
using std::fill;
using std::function;
using std::ios_base;
using std::istream;
using std::istream_iterator;
using std::iterator;
using std::make_pair;
using std::map;
using std::max;
using std::min;
using std::numeric_limits;
using std::ostream;
using std::pair;
using std::priority_queue;
using std::queue;
using std::set;
using std::sort;
using std::string;
using std::stringstream;
using std::swap;
using std::uniform_int_distribution;
using std::uniform_real_distribution;
using std::unordered_map;
using std::vector;

typedef unsigned int ui;
typedef long long ll;
typedef long double ld;
typedef unsigned long long ull;
std::mt19937 rng(std::chrono::steady_clock::now().time_since_epoch().count());

#define error(args...)                       \
  {                                          \
    string _s = #args;                       \
    replace(_s.begin(), _s.end(), ',', ' '); \
    stringstream _ss(_s);                    \
    istream_iterator<string> _it(_ss);       \
    err(_it, args);                          \
  }
void err(std::istream_iterator<string> it) {}
template <typename T, typename... Args>
void err(std::istream_iterator<string> it, T a, Args... args) {
  cerr << *it << " = " << a << endl;
  err(++it, args...);
}

#define mp make_pair

#endif

#ifdef LOCAL
#define PREPARE_INPUT                                \
  {                                                  \
    std::cout << "Input file name:";                 \
    string file;                                     \
    std::cin >> file;                                \
    file = string(__FILE__) + "/../" + file + ".in"; \
    std::cout << "Open file:" << file << std::endl;  \
    freopen(file.data(), "r", stdin);                \
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
#ifndef TREE_PATH_BRUTEFORCE_H
#define TREE_PATH_BRUTEFORCE_H

#ifndef LCA_H
#define LCA_H

#ifndef BITS_H
#define BITS_H

namespace bits {
template <class T>
inline bool BitAt(T x, int i) {
  return (x >> i) & 1;
}
template <class T>
inline T SetBit(T x, int i) {
  return x |= T(1) << i;
}
template <class T>
inline T RemoveBit(T x, int i) {
  return x &= ~(T(1) << i);
}
template <class T>
inline T LowestOneBit(T x) {
  return x & -x;
}
inline int FloorLog2(unsigned int x) {
  if (x == 0) {
    return 0;
  }
  return (sizeof(unsigned int) * 8) - 1 - __builtin_clz(x);
}
inline int FloorLog2(unsigned long long x) {
  if (x == 0) {
    return 0;
  }
  return (sizeof(unsigned long long) * 8) - 1 - __builtin_clzll(x);
}
inline int CeilLog2(unsigned int x) {
  if (x == 0) {
    return 0;
  }
  return (sizeof(unsigned int) * 8) - __builtin_clz(x - 1);
}
inline int CeilLog2(unsigned long long x) {
  if (x == 0) {
    return 0;
  }
  return (sizeof(unsigned long long) * 8) - __builtin_clzll(x - 1);
}
template <class T>
inline T HighestOneBit(T x) {
  return T(1) << FloorLog2(x);
}
inline int CountOne(unsigned int x) { return __builtin_popcount(x); }
inline int CountOne(unsigned long long x) { return __builtin_popcountll(x); }
}  // namespace bits
#endif

namespace lca {
using namespace bits;
class Lca {
 private:
  vector<int> parent;
  vector<unsigned> pre_order;
  vector<unsigned> I;
  vector<int> head;
  vector<unsigned> A;
  unsigned time;
  unsigned highest_one_bit(unsigned x) const {
    return x ? 1u << FloorLog2(x) : 0;
  }
  void dfs1(const vector<vector<int>> &tree, int u, int p) {
    parent[u] = p;
    I[u] = pre_order[u] = time++;
    for (int v : tree[u]) {
      if (v == p) continue;
      dfs1(tree, v, u);
      if (LowestOneBit(I[u]) < LowestOneBit(I[v])) {
        I[u] = I[v];
      }
    }
    head[I[u]] = u;
  }

  void dfs2(const vector<vector<int>> &tree, int u, int p, unsigned up) {
    A[u] = up | LowestOneBit(I[u]);
    for (int v : tree[u]) {
      if (v == p) continue;
      dfs2(tree, v, u, A[u]);
    }
  }

  int enter_into_strip(int x, int hz) const {
    if (LowestOneBit(I[x]) == hz) return x;
    int hw = highest_one_bit(A[x] & (hz - 1));
    return parent[head[(I[x] & -hw) | hw]];
  }

 public:
  // lca in O(1)
  int operator()(int x, int y) const {
    int hb = I[x] == I[y] ? LowestOneBit(I[x]) : highest_one_bit(I[x] ^ I[y]);
    int hz = LowestOneBit(A[x] & A[y] & -hb);
    int ex = enter_into_strip(x, hz);
    int ey = enter_into_strip(y, hz);
    return pre_order[ex] < pre_order[ey] ? ex : ey;
  }

  void init(const vector<vector<int>> &tree, int root) {
    int n = tree.size();
    parent.resize(n);
    pre_order.resize(n);
    I.resize(n);
    head.resize(n);
    A.resize(n);

    time = 0;
    dfs1(tree, root, -1);
    dfs2(tree, root, -1, 0);
  }
};
}  // namespace lca

#endif

namespace tree_path_bruteforce {
template <class T>
struct Node {
  int r;
  int node;
  T data;
};

template <class T, int MAX_N>
class TreePathBF {
 public:
  void init(vector<vector<int>> &tree) {
    _seqTail = 0;
    _lca.init(tree, 0);
    dfs(0, -1, tree);
    compress(0, -1, tree);
  }

  void operator()(int u, int v, const function<void(Node<T> &)> &consumer) {
    int lca = _lca(u, v);
    consumer(_seq[_nodeToSeq[lca]]);
    upToBottom(lca, u, consumer);
    upToBottom(lca, v, consumer);
  }

 private:
  void upToBottom(int ancestor, int v,
                  const function<void(Node<T> &)> &consumer) {
    int l = _nodeToSeq[v];
    int r = _seq[l].r;
    for (int i = _nodeToSeq[ancestor] + 1; i <= l; i++) {
      if (_seq[i].r >= l) {
        consumer(_seq[i]);
      } else {
        i = _seq[i].r;
      }
    }
  }

  void dfs(int root, int p, vector<vector<int>> &tree) {
    _size[root] = 1;
    for (int node : tree[root]) {
      if (node == p) {
        continue;
      }
      dfs(node, root, tree);
      _size[root] += _size[node];
    }
    sort(tree[root].begin(), tree[root].end(),
         [this](auto &a, auto &b) { return _size[a] > _size[b]; });
  }

  void compress(int root, int p, vector<vector<int>> &tree) {
    int id = _seqTail++;
    _seq[id].r = id;
    _seq[id].node = root;
    _nodeToSeq[root] = id;
    for (int node : tree[root]) {
      if (node == p) {
        continue;
      }
      compress(node, root, tree);
    }
    _seq[id].r = _seqTail - 1;
  }

  lca::Lca _lca;
  Node<T> _seq[MAX_N];
  int _nodeToSeq[MAX_N];
  int _size[MAX_N];
  int _seqTail;
};
};  // namespace tree_path_bruteforce

#endif

struct LinearFunction {
  ll a;
  ll b;
};
tree_path_bruteforce::TreePathBF<LinearFunction, 100000 + 5> treePath;

void solve(int testId, istream &in, ostream &out) {
  int n, q;
  in >> n >> q;
  vector<LinearFunction> funcs(n);
  for (int i = 0; i < n; i++) {
    in >> funcs[i].a;
  }
  for (int i = 0; i < n; i++) {
    in >> funcs[i].b;
  }
  vector<vector<int>> edges(n);
  for (int i = 1; i < n; i++) {
    int u, v;
    in >> u >> v;
    u--;
    v--;
    edges[u].push_back(v);
    edges[v].push_back(u);
  }
  treePath.init(edges);
  for (int i = 0; i < n; i++) {
    treePath(i, i, [&](auto &node) { node.data = funcs[i]; });
  }
  for (int i = 0; i < q; i++) {
    int t, u, v;
    in >> t >> u >> v;
    u--;
    v--;
    if (t == 1) {
      ll a, b;
      in >> a >> b;
      treePath(u, v, [&](auto &node) {
        node.data.a += a;
        node.data.b += b;
      });
    } else {
      ll z;
      ll ans = -2e18;
      in >> z;
      treePath(u, v, [&](auto &node) {
        ans = max(ans, node.data.a * z + node.data.b);
      });
      out << ans << endl;
    }
  }
}

RUN_ONCE