#ifndef MODULAR_H
#define MODULAR_H

#include "common.h"
#include "gcd.h"

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

/**
 * O(n), inverse 1, 2, ..., n - 1
 */
template <class T>
void InverseRange(vector<T> &vec, T mod) {
  int n = vec.size();
  if (n <= 1) {
    return;
  }
  vec[1] = 1;
  for (int i = 2; i < n; i++) {
    T k = mod / i;
    T r = mod % i;
    vec[i] = Modmul(-k, vec[r], mod);
  }
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