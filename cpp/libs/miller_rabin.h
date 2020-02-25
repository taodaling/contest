#ifndef FACTORIZATION_H
#define FACTORIZATION_H
#include "common.h"
#include "modular.h"

namespace miller_rabin {
using modular::Mod;
using modular::Modmul;
using modular::Modpow;

template <class T>
bool Test(T y, T exp, T n) {
  T y2 = Modmul(y, y, n);
  if (!(exp == n - 1 || Test(y2, exp * 2, n))) {
    return false;
  }
  if (exp != n - 1 && y2 != 1) {
    return true;
  }
  if (y != 1 && y != n - 1) {
    return false;
  }
  return true;
}

/**
 * O(log n)
 */
template <class T>
bool Witeness(T x, T n, T m) {
  return Test(Modpow(x, m, n), m, n);
}

template <class T>
bool MillerRabin(T n, int s) {
  if (n <= 1) {
    return false;
  }
  if (n == 2) {
    return true;
  }
  if (n % 2 == 0) {
    return false;
  }
  T m = n - 1;
  while (m % 2 == 0) {
    m /= 2;
  }
  uniform_int_distribution<T> random(2, n - 1);
  for (int i = 0; i < s; i++) {
    T x = random(rng);
    if (!Witeness(x, n, m)) {
      return false;
    }
  }
  return true;
}
}  // namespace miller_rabin

#endif