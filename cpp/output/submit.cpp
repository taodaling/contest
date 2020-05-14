#ifndef READER_H
#define READER_H

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


namespace reader {

class Input {
 private:
  istream &_in;

 public:
  Input(istream &is) : _in(is) {}
  template <class T>
  T read() {
    T tmp;
    _in >> tmp;
    return tmp;
  }
  int ri() { return read<int>(); }
  ll rl() { return read<ll>(); }
  double rd() { return read<double>(); }
  char rc() { return read<char>(); }
  string rs() { return read<string>(); }
};

class StringReader {
 private:
  const string &_s;
  int _offset;

 public:
  StringReader(const string &s) : _s(s), _offset(0) {}
  void skip() {
    while (_offset < _s.size() && _s[_offset] <= 32) {
      _offset++;
    }
  }
  bool hasMore() {
    skip();
    return _offset < _s.size();
  }
  int readInt() {
    skip();
    int ans = 0;
    bool sign = true;
    if (_s[_offset] == '-' || _s[_offset] == '+') {
      sign = _s[_offset] == '+';
    }
    while (_offset < _s.size() && '0' <= _s[_offset] && _s[_offset] <= '9') {
      ans = ans * 10 + _s[_offset] - '0';
      _offset++;
    }
    return ans;
  }
};

}  // namespace reader

#endif

#ifndef radix_h
#define radix_h


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

namespace radix {
template <class T>
class Radix {
 public:
  Radix(int radix) : _radix(radix) {
    assert(radix > 1);
    T limit = numeric_limits<T>::max();
    _digits.push_back(1);
    while (!decimal::IsMultiplicationOverflow<T>(_digits.back(), radix, limit)) {
      _digits.push_back(_digits.back() * radix);
    }
  }

  inline int operator()(int i) { return _digits[i]; }

  inline int get(T x, int i) { return x / _digits[i] % _radix; }
  inline T set(T x, int i, int v) { return x + (v - get(x, i)) * _digits[i]; }

 private:
  vector<T> _digits;
  int _radix;
};
}  // namespace radix

#endif

radix::Radix<ll> rd(10);
int lBits[19];
int rBits[19];
int cnts[10];
vector<ll> all;

bool test(int sum, int i, bool ceil, bool floor) {
  if (sum > i + 1) {
    return false;
  }
  if (i < 0 || !ceil && !floor) {
    return true;
  }

  int l = lBits[i];
  int r = rBits[i];
  int start = floor ? l : 0;
  int end = ceil ? r : 9;
  for (int j = start; j <= end; j++) {
    if (cnts[j] == 0) {
      continue;
    }
    cnts[j]--;
    if (test(sum - (j > 0 ? 1 : 0), i - 1, ceil && j == r, floor && j == l)) {
      return true;
    }
    cnts[j]++;
  }
  return false;
}

void dfs(int val, int cnt, ll built) {
  if (val == 10) {
    all.push_back(built);
    return;
  }
  for (; cnt <= 18; cnt++, built = built * 10 + val) {
    dfs(val + 1, cnt, built);
  }
}

void solve(int testId, istream &in, ostream &out) {
  all.reserve(5000000);

  ll l, r;
  in >> l >> r;
  ll lVal = l;
  ll rVal = r;
  for (int i = 0; i < 19; i++) {
    lBits[i] = lVal % 10;
    rBits[i] = rVal % 10;
    lVal /= 10;
    rVal /= 10;
  }

  dfs(1, 0, 0);

  int ans = 0;

  // all.unique();
  for (ll val : all) {
    int sum = 0;

    if (val >= l && val <= r) {
      ans++;
      continue;
    }
    for (int i = 1; i < 10; i++) {
      cnts[i] = 0;
    }
    while (val > 0) {
      cnts[(int)(val % 10)]++;
      val /= 10;
      sum++;
    }
    cnts[0] = (int)1e9;
    if (test(sum, 18, true, true)) {
      ans++;
      // debug.debug("val", all.get(i));
    }
  }

  out << ans;
}

RUN_ONCE