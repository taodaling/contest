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


struct Box {
  int xyz[3];
  int max;
  int min;
};

Box boxes[20];
int sorted[20];
vector<int> ranks;
int bit[51];
int n;

void clear() { C0(bit); }

/**
 * 查询A[1]+A[2]+...+A[i]
 */

int query(int i) {
  int sum = 0;
  for (; i > 0; i -= i & -i) {
    sum = max(sum, bit[i]);
  }
  return sum;
}

/**
 * 将A[i]更新为A[i]+mod
 */
void update(int i, int mod) {
  if (i <= 0) {
    return;
  }
  for (; i <= 50; i += i & -i) {
    bit[i] = max(bit[i], mod);
  }
}

int rankOf(int x) {
  return std::lower_bound(ranks.begin(), ranks.end(), x) - ranks.begin();
}

class BoxTower {
 public:
  int tallestTower(vector<int> x, vector<int> y, vector<int> z) {
    n = x.size();
    for (int i = 0; i < n; i++) {
      sorted[i] = i;
    }
    for (auto v : x) {
      ranks.push_back(v);
    }
    for (auto v : y) {
      ranks.push_back(v);
    }
    for (auto v : z) {
      ranks.push_back(v);
    }
    ranks.push_back(0);
    sort(ranks.begin(), ranks.end());
    ranks.resize(unique(ranks.begin(), ranks.end()) - ranks.begin());

    for (int i = 0; i < n; i++) {
      x[i] = rankOf(x[i]);
      y[i] = rankOf(y[i]);
      z[i] = rankOf(z[i]);
    }
    //dbg(ranks, x, y, z);

    for (int i = 0; i < n; i++) {
      boxes[i].xyz[0] = x[i];
      boxes[i].xyz[1] = y[i];
      boxes[i].xyz[2] = z[i];
    }

    int ans = dfs(n - 1);
    return ans;
  }

  int solve() {
    for (int i = 0; i < n; i++) {
      if (boxes[i].xyz[0] < boxes[i].xyz[1]) {
        boxes[i].min = boxes[i].xyz[0];
        boxes[i].max = boxes[i].xyz[1];
      } else {
        boxes[i].min = boxes[i].xyz[1];
        boxes[i].max = boxes[i].xyz[0];
      }
    }
    sort(sorted, sorted + n, [&](int a, int b) {
      return boxes[a].max == boxes[b].max ? boxes[a].min < boxes[b].min
                                          : boxes[a].max < boxes[b].max;
    });

    for (int b : sorted) {
      int h = query(boxes[b].min) + ranks[boxes[b].xyz[2]];
      update(boxes[b].min, h);
    }
    int ans = query(50);
    clear();
    return ans;
  }

  int dfs(int t) {
    if (t == -1) {
      return solve();
    }
    int ans = 0;
    for (int i = 2; i >= 0; i--) {
      swap(boxes[t].xyz[i], boxes[n].xyz[2]);
      ans = max(ans, dfs(t - 1));
    }
    return ans;
  }
};

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  vector<vector<int>> x(3, vector<int>(n));
  for(int i = 0; i < 3; i++){
    for(int j = 0; j < n; j++){
      in >> x[i][j];
    }
  }

  BoxTower solution;
  int ans = solution.tallestTower(x[0], x[1], x[2]);
  out << ans;
}

RUN_ONCE