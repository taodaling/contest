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


using std::cin;
using std::cout;

bool less(int x, int y) {
  cout << "? " << x << " " <<  y << endl;
  cout.flush();
  string ans;
  cin >> ans;
  if (ans == ("e")) {
    exit(-1);
  }
  return ans == ("y");
}

bool leq(int x, int y) { return !less(y, x); }

bool geq(int x, int y) { return less(y, x); }

void answer(int a) {
  cout << "! " << a << endl;
  cout.flush();
}

int nextInt() { return uniform_int_distribution<int>(1, 1e9)(rng); }

int pow(int x, int n) { return n == 0 ? 1 : x * pow(x, n - 1); }

int log(int x, int y) {
  int ans = 0;
  while (y % x == 0) {
    y /= x;
    ans++;
  }
  return ans;
}

void solve() {
  int x = 0;
  int y = 0;
  while (true) {
    x = nextInt();
    y = x + nextInt();
    if (leq(y, x)) {
      break;
    }
  }

  int parents;
  {
    ll l = x;
    ll r = y;
    while (r - l > 1) {
      ll m = (l + r) >> 1;
      if (geq(m, r)) {
        l = m;
      } else {
        // m > l
        r = m;
      }
    }
    if (r > l) {
      if (geq(r, l)) {
        r = l;
      } else {
        l = r;
      }
    }
    parents = l;
  }

  vector<int> primes;
  int tmp = parents;
  for (int i = 2; i * i <= tmp; i++) {
    if (tmp % i != 0) {
      continue;
    }
    primes.push_back(i);
    while (tmp % i == 0) {
      tmp /= i;
    }
  }

  if (tmp > 1) {
    primes.push_back(tmp);
  }

  int ans = 1;
  for (int p : primes) {
    int l = 0;
    int r = log(p, parents);

    int remain = binary_search::BinarySearch<int>(l, r, [&](int mid) {
      int cand = parents / pow(p, r - mid);
      return leq(cand, 0);
    });
    ans *= pow(p, remain);
  }

  answer(ans);
}

void solve(int testId, istream &in, ostream &out) {
  while (true) {
    string cmd;
    in >> cmd;
    if (cmd == ("start")) {
      solve();
      continue;
    }
    if (cmd == ("mistake")) {
      exit(-1);
    }
    if (cmd == ("end")) {
      return;
    }
  }
}

RUN_ONCE