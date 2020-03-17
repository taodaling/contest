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

typedef unsigned int ui;
typedef long long ll;
typedef long double ld;
typedef unsigned long long ull;
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
#ifndef GCD_H
#define GCD_H



namespace gcd {
template <typename T>
T Gcd0(T a, T b) {
  return b ? Gcd0(b, a % b) : a;
}

template <typename T>
T Gcd(T a, T b) {
  if (a < b) {
    swap(a, b);
  }
  return Gcd0(a, b);
}

template <typename T>
T Extgcd0(T a, T b, T &x, T &y) {
  if (!b) {
    x = 1;
    y = 0;
    return a;
  }
  T ans = Extgcd0(b, a % b, y, x);
  y = y - x * (a / b);
  return ans;
}

/**
 * Find gcd(a, b) and expression xa+yb=g
 */
template <typename T>
T Extgcd(T a, T b, T &x, T &y) {
  if (a >= b) {
    return Extgcd0(a, b, x, y);
  }
  return Extgcd0(b, a, y, x);
}

/**
 * O(n + logn)
 */
template <class T>
T Extgcd(vector<T> &arg, vector<T> &coes) {
  int n = arg.size();
  if (n == 0) {
    return 0;
  }
  coes.resize(n);
  vector<T> gs(n);
  gs[0] = arg[0];
  for (int i = 1; i < n; i++) {
    gs[i] = Gcd(gs[i - 1], arg[i]);
  }
  T prod = 1;
  for (int i = n - 1; i >= 1; i--) {
    T a, b;
    Extgcd0(gs[i - 1], arg[i], a, b);
    coes[i] = b * prod;
    prod *= a;
  }
  coes[0] = prod;
  return gs[n - 1];
}

}  // namespace gcd

#endif
#ifndef MODULAR_H
#define MODULAR_H




namespace modular {
template <class T>
inline T Mod(T x, T m) {
  x %= m;
  if (x < 0) {
    x += m;
  }
  return x;
}

template <class T>
inline T Modmul(T a, T b, T m) {
  T k = (T)((long double)a / m * b + 0.5);
  return Mod<T>(a * b - k * m, m);
}

template <>
inline int Modmul<int>(int a, int b, int m) {
  return Mod<long long>((long long)a * b, m);
}

template <class T>
inline T Modpow(T x, long long n, T m) {
  if (n == 0) {
    return Mod<T>(1, m);
  }
  T ans = Modpow<T>(x, n >> 1, m);
  ans = Modmul<T>(ans, ans, m);
  if (n & 1) {
    ans = Modmul<T>(ans, x, m);
  }
  return ans;
}

template <typename T>
T Inverse(T a, T m) {
  int x, y;
  gcd::Extgcd(a, m, x, y);
  return Mod(x, m);
}

/**
 * O(n + logn)
 */
template <class T>
T Extgcd(vector<T> &arg, vector<T> &coes, T mod) {
  int n = arg.size();
  if (n == 0) {
    return 0;
  }
  coes.resize(n);
  vector<T> gs(n);
  gs[0] = arg[0];
  for (int i = 1; i < n; i++) {
    gs[i] = gcd::Gcd(gs[i - 1], arg[i]);
  }
  T prod = 1;
  for (int i = n - 1; i >= 1; i--) {
    T a, b;
    gcd::Extgcd0(gs[i - 1], arg[i], a, b);
    coes[i] = Modmul(b, prod, mod);
    prod = Modmul(prod, a, mod);
  }
  coes[0] = prod;
  return gs[n - 1];
}

template <class T, T M>
class Modular {
 public:
  Modular() { set(0); }
  Modular(const T &val) { set(val); }
  void set(const T &x) { _v = Mod(x, M); }
  Modular(const Modular<T, M> &val) { _v = val._v; }
  Modular<T, M> &operator=(const Modular<T, M> &y) {
    _v = y._v;
    return *this;
  }
  const T &operator()() const { return _v; }
  T &operator()() { return _v; }
  Modular<T, M> &operator-=(const Modular<T, M> &y) {
    _v = Mod(_v - y._v, M);
    return *this;
  }
  Modular<T, M> &operator+=(const Modular<T, M> &y) {
    _v = Mod(_v + y._v, M);
    return *this;
  }

  Modular<T, M> &operator*=(const Modular<T, M> &y) {
    _v = Modmul(_v, y._v, M);
    return *this;
  }
  Modular<T, M> &operator/=(const Modular<T, M> &y) {
    (*this) *= y.inverse();
    return *this;
  }
  Modular<T, M> pow(long long exp) const { return Modpow(_v, exp, M); }
  Modular<T, M> inverse() const { return modular::Inverse(_v, M); }

 private:
  T _v;
};

template <class T, T M>
Modular<T, M> operator+(const Modular<T, M> &a, const Modular<T, M> &b) {
  Modular<T, M> ans = a;
  ans += b;
  return ans;
}
template <class T, T M>
Modular<T, M> operator+(const T &a, const Modular<T, M> &b) {
  Modular<T, M> ans = a;
  ans += b;
  return ans;
}
template <class T, T M>
Modular<T, M> operator+(const Modular<T, M> &a, const T &b) {
  Modular<T, M> ans = a;
  ans += b;
  return ans;
}
template <class T, T M>
Modular<T, M> operator-(const Modular<T, M> &a, const Modular<T, M> &b) {
  Modular<T, M> ans = a;
  ans -= b;
  return ans;
}
template <class T, T M>
Modular<T, M> operator-(const T &a, const Modular<T, M> &b) {
  Modular<T, M> ans = a;
  ans -= b;
  return ans;
}
template <class T, T M>
Modular<T, M> operator-(const Modular<T, M> &a, const T &b) {
  Modular<T, M> ans = a;
  ans -= b;
  return ans;
}
template <class T, T M>
Modular<T, M> operator*(const Modular<T, M> &a, const Modular<T, M> &b) {
  Modular<T, M> ans = a;
  ans *= b;
  return ans;
}
template <class T, T M>
Modular<T, M> operator*(const T &a, const Modular<T, M> &b) {
  Modular<T, M> ans = a;
  ans *= b;
  return ans;
}
template <class T, T M>
Modular<T, M> operator*(const Modular<T, M> &a, const T &b) {
  Modular<T, M> ans = a;
  ans *= b;
  return ans;
}
template <class T, T M>
Modular<T, M> operator/(const Modular<T, M> &a, const Modular<T, M> &b) {
  Modular<T, M> ans = a;
  ans /= b;
  return ans;
}
template <class T, T M>
Modular<T, M> operator/(const T &a, const Modular<T, M> &b) {
  Modular<T, M> ans = a;
  ans /= b;
  return ans;
}
template <class T, T M>
Modular<T, M> operator/(const Modular<T, M> &a, const T &b) {
  Modular<T, M> ans = a;
  ans /= b;
  return ans;
}
template <class T, T M>
Modular<T, M> operator==(const Modular<T, M> &a, const Modular<T, M> &b) {
  return a() == b();
}
template <class T, T M>
Modular<T, M> operator==(const Modular<T, M> &a, const T &b) {
  return a() == Modular<T, M>(b);
}
template <class T, T M>
Modular<T, M> operator==(const T &a, const Modular<T, M> &b) {
  return Modular<T, M>(a) == b;
}
template <class T, T M>
Modular<T, M> operator!=(const Modular<T, M> &a, const Modular<T, M> &b) {
  return a() != b();
}
template <class T, T M>
Modular<T, M> operator!=(const Modular<T, M> &a, const T &b) {
  return a() != Modular<T, M>(b);
}
template <class T, T M>
Modular<T, M> operator!=(const T &a, const Modular<T, M> &b) {
  return Modular<T, M>(a) != b;
}
template <class T, T M>
std::ostream &operator<<(std::ostream &out, const Modular<T, M> &v) {
  return out << v();
}
template <class T, T M>
std::istream &operator>>(std::istream &in, const Modular<T, M> &v) {
  T x;
  in >> x;
  v.set(x);
  return in;
}
}  // namespace modular

#endif

bool solve(ll x1, ll x2, ll a, ll b, ll k, ostream &out) {
  ll r = k - abs(a) - abs(b);
  if (r < 0 || (r % 2 == 1)) {
    return false;
  }
  out << "YES" << endl;

  ll p1 = 0;
  ll n1 = 0;
  ll p2 = 0;
  ll n2 = 0;
  if (a > 0) {
    p1 += a;
  } else {
    n1 -= a;
  }
  if (b > 0) {
    p2 += b;
  } else {
    n2 -= b;
  }

  p1 += r / 2;
  n1 += r / 2;

  out << p1 << ' ' << n1 << ' ' << p2 << ' ' << n2;
  return true;
}

void solve(int testId, istream &in, ostream &out) {
  ll x1, x2, p, k;
  in >> x1 >> x2 >> p >> k;
  ll a, b;
  ll g = gcd::Extgcd(x1, x2, a, b);
  if (p % g != 0) {
    out << "NO" << endl;
    return;
  }
  a *= p / g;
  b *= p / g;
  x1 /= g;
  x2 /= g;

  // case 1
  vector<pair<ll, ll>> possible;
  {
    ll ta = a;
    ll tb = b;
    ta = modular::Mod(ta, x2);
    tb -= (ta - a) / x2 * x1;
    possible.emplace_back(ta, tb);
  }

  {
    ll ta = a;
    ll tb = b;
    tb = modular::Mod(tb, x1);
    ta -= (tb - b) / x1 * x2;
    possible.emplace_back(ta, tb);
  }

  for (auto &ab : possible) {
    if (solve(x1, x2, ab.first, ab.second, k, out)) {
      return;
    }
    if (solve(x1, x2, ab.first + x2, ab.second - x1, k, out)) {
      return;
    }
    if (solve(x1, x2, ab.first - x2, ab.second + x1, k, out)) {
      return;
    }
  }

  out << "NO";
  return;
}

RUN_ONCE