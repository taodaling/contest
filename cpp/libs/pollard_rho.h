#ifndef POLLARD_RHO_H
#define POLLARD_RHO_H

#include "gcd.h"
#include "miller_rabin.h"
#include "modular.h"

namespace pollard_rho {
using namespace miller_rabin;
using namespace modular;

template <class T>
T FindFactor0(T x, T c, T n) {
  T xi = x;
  T xj = x;
  T j = 2;
  T i = 1;
  while (i < n) {
    i++;
    xi = Mod<T>(Modmul<T>(xi, xi, n) + c, n);
    int g = gcd::Gcd<T>(n, std::abs(xi - xj));
    if (g != 1 && g != n) {
      return g;
    }
    if (i == j) {
      j = j << 1;
      xj = xi;
    }
  }
  return -1;
}

template <class T>
inline T Rho(T n) {
  if (!(n % 2)) {
    return 2;
  }
  if (!(n % 3)) {
    return 3;
  }
  T x = 0, y = x, t = 1, q = 1, c = uniform_int_distribution<T>(1, n - 1)(rng);
  for (int k = 2;; k <<= 1, y = x, q = 1) {
    for (int i = 1; i <= k; ++i) {
      x = Mod(Modmul(x, x, n) + c, n);
      q = Modmul(q, std::abs(x - y), n);
      if (!(i & 127)) {
        t = gcd::Gcd(q, n);
        if (t > 1) {
          return t;
        }
      }
    }
    if ((t = gcd::Gcd(q, n)) > 1) {
      return t;
    }
  }
  return n;
}

template <class T>
T FindFactor(T n) {
  if (MillerRabin<T>(n, 10)) {
    return n;
  }
  uniform_int_distribution<T> random(0, n - 1);
  while (true) {
    T f = Rho(n);
    if (f != n) {
      return f;
    }
  }
}

template <class T>
void FindAllPrimeFactors(set<T> &ans, T n) {
  if (n == 1) {
    return;
  }
  T f = FindFactor<T>(n);
  if (f == n) {
    ans.insert(f);
    return;
  }
  FindAllPrimeFactors<T>(ans, f);
  FindAllPrimeFactors<T>(ans, n / f);
}
}  // namespace pollard_rho

#endif