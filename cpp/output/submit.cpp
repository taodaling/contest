#ifndef BINARY_SEARCH_H
#define BINARY_SEARCH_H

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

#define X(x) (x).first
#define Y(x) (x).second


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
#ifndef DECIMAL_H
#define DECIMAL_H

namespace decimal {

ll Merge(int a, int b) {
  static ll mask = (1ll << 32) - 1;
  return ((ll)a << 32) | ((ll)b & mask);
}

template <class T>
T CeilDiv(T a, T b) {
  if (b < 0) {
    a = -a;
    b = -b;
  }
  T div = a / b;
  if (b * div < a) {
    div++;
  }
  return div;
}

template <class T>
T FloorDiv(T a, T b) {
  if (b < 0) {
    a = -a;
    b = -b;
  }
  T div = a / b;
  if (b * div > a) {
    div--;
  }
  return div;
}

template <class T>
T RoundToInt(double x) {
  return x >= 0 ? x + 0.5 : x - 0.5;
}
template <class T>
int Sign(T x) {
  return x < 0 ? -1 : x > 0 ? 1 : 0;
}
template <class T>
bool IsPlusOverflow(T a, T b) {
  if (Sign(a) != Sign(b)) {
    return false;
  }
  if (a < 0) {
    return a + b > 0;
  } else {
    return a + b < 0;
  }
}
template <class T>
bool IsMultiplicationOverflow(T a, T b, T limit) {
  if (limit < 0) {
    limit = -limit;
  }
  if (a < 0) {
    a = -a;
  }
  if (b < 0) {
    b = -b;
  }
  if (a == 0 || b == 0) {
    return false;
  }
  // a * b > limit => a > limit / b
  return a > limit / b;
}
}  // namespace decimal

#endif

namespace binary_search {
template <class T>
T BinarySearch(T l, T r, const function<bool(T)> &func) {
  assert(l <= r);
  while (l < r) {
    T mid = (l + r) >> 1;
    if (func(mid)) {
      r = mid;
    } else {
      l = mid + 1;
    }
  }
  return l;
}

template <class T>
T BinarySearch(T l, T r, const function<bool(T)> &func, T absolute,
               T relative) {
  assert(l <= r);
  while (r - l > absolute) {
    if ((r < 0 && (r - l) < -r * relative) ||
        (l > 0 && (r - l) < l * relative)) {
      break;
    }

    T mid = (l + r) / 2.0;
    if (func(mid)) {
      r = mid;
    } else {
      l = mid;
    }
  }
  return (l + r) / 2.0;
}

/**
 * Used to find the maximum value of a lower convex.
 * Assume f(-inf)<...<f(ans)=f(ans+1)=...=f(ans+k)>...>f(inf)
 */
template <class T>
T TernarySearch(T l, T r, const function<T(T)> &func, T absolute, T relative) {
  while (r - l > absolute) {
    if (r < 0 && (r - l) / -r <= relative || l > 0 && (r - l) / l <= relative) {
      break;
    }
    T dist = (r - l) / 3;
    T ml = l + dist;
    T mr = r - dist;
    if (func(ml) < func(mr)) {
      l = ml;
    } else {
      r = mr;
    }
  }
  return (l + r) / 2;
}

/**
 * Used to find the maximum value of a Upper convex.
 * Assume f(-inf)<...<f(ans)=f(ans+1)=...=f(ans+k)>...>f(inf)
 */
template <class T>
T TernarySearch(T l, T r, const function<T(T)> &func) {
  while (r - l > 2) {
    T ml = l + decimal::FloorDiv(r - l, 3);
    T mr = r - decimal::CeilDiv(r - l, 3);
    if (func(ml) < func(mr)) {
      l = ml;
    } else {
      r = mr;
    }
  }
  while (l < r) {
    if (func(l) >= func(r)) {
      r--;
    } else {
      l++;
    }
  }
  return l;
}
}  // namespace binary_search

#endif


const int LOG = 7;
const double PREC = 1e-4;

struct Edge {
  int to;
  int t;
  int c;
};

vector<vector<Edge>> g;

vector<vector<vector<double>>> best;
vector<vector<vector<int>>> middle;

bool Check(double c) {
  double inf = 1e30;

  int n = g.size();
  for (int i = 0; i < LOG; i++) {
    for (int j = 0; j < n; j++) {
      for (int k = 0; k < n; k++) {
        best[i][j][k] = -inf;
        middle[i][j][k] = -1;
      }
    }
  }

  for (int i = 0; i < n; i++) {
    for (auto &e : g[i]) {
      if (best[0][i][e.to] + PREC < e.c - c * e.t) {
        best[0][i][e.to] = e.c - c * e.t;
      }
    }
  }

  for (int r = 1; r < LOG; r++) {
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        best[r][i][j] = best[r - 1][i][j];
        middle[r][i][j] = -1;
        for (int k = 0; k < n; k++) {
          if (best[r][i][j] + PREC < best[r - 1][i][k] + best[r - 1][k][j]) {
            best[r][i][j] = best[r - 1][i][k] + best[r - 1][k][j];
            middle[r][i][j] = k;
          }
        }
      }
    }
  }

  double mx = 0;
  for (int i = 0; i < n; i++) {
    mx = max(mx, best[LOG - 1][i][i]);
  }

  return mx <= 0;
}

vector<int> trace;

void Record(int r, int i, int j) {
  if (r == 0) {
    trace.push_back(i);
    trace.push_back(j);
    return;
  }

  int mid = middle[r][i][j];
  if (mid == -1) {
    return Record(r - 1, i, j);
  }
  Record(r - 1, i, mid);
  trace.pop_back();
  Record(r - 1, mid, j);
}

void solve(int testId, istream &in, ostream &out) {
  int n, m;
  in >> n >> m;
  g.resize(n);
  best.resize(LOG, vector<vector<double>>(n, vector<double>(n)));
  middle.resize(LOG, vector<vector<int>>(n, vector<int>(n)));

  for (int i = 0; i < m; i++) {
    int u, v, c, t;
    in >> u >> v >> c >> t;
    u--;
    v--;
    g[u].push_back({to : v, t : t, c : c});
  }

  double c = binary_search::BinarySearch<double>(0, 100, Check, 1e-6, 1e-6);
  Check(c);

  int index = 0;
  for (int i = 1; i < n; i++) {
    if (best[LOG - 1][i][i] > best[LOG - 1][index][index]) {
      index = i;
    }
  }

  if (best[LOG - 1][index][index] < 0) {
    out << 0;
    return;
  }

  Record(LOG - 1, index, index);
  trace.pop_back();
  out << trace.size() << endl;
  for (int x : trace) {
    out << x + 1 << ' ';
  }
}

RUN_ONCE