#ifndef HASH_H
#define HASH_H

#include "common.h"
#include "modular.h"

namespace hash {
struct CustomHash {
  static uint64_t splitmix64(uint64_t x) {
    // http://xorshift.di.unimi.it/splitmix64.c
    x += 0x9e3779b97f4a7c15;
    x = (x ^ (x >> 30)) * 0xbf58476d1ce4e5b9;
    x = (x ^ (x >> 27)) * 0x94d049bb133111eb;
    return x ^ (x >> 31);
  }

  size_t operator()(uint64_t x) const {
    static const uint64_t FIXED_RANDOM =
        std::chrono::steady_clock::now().time_since_epoch().count();
    return splitmix64(x + FIXED_RANDOM);
  }
};

template <int N, int X>
struct HashData {
  using Mint = modular::Modular<int, (int)1e9 + 7>;
  Mint inv[N + 1];
  Mint pow[N + 1];
  HashData() {
    pow[0] = inv[0] = 1;
    Mint x(X);
    Mint invX = 1 / x;
    for (int i = 1; i <= N; i++) {
      inv[i] = inv[i - 1] * invX;
      pow[i] = pow[i - 1] * x;
    }
  }
};

template <int N, int M, int X>
class PartialHash {
 private:
  using Mint = modular::Modular<int, (int)1e9 + 7>;
  Mint _h[N + 1];
  const HashData<M, X> &_hd;

 public:
  PartialHash(const HashData<M, X> &hd) : _hd(hd) { assert(N <= M); }

  void reset(const function<int(int)> &func, int l, int r) {
    assert(l <= r);
    if (l > 0) {
      _h[l - 1] = 0;
    }
    Mint x = _h[l] = func(l) * _hd.pow[l];
    for (int i = l + 1; i <= r; i++) {
      _h[i] = _h[i - 1] + func(i) * _hd.pow[i];
    }
  }

  int hash(int l, int r, bool verbose = false) {
    Mint h = _h[r];
    if (l > 0) {
      h -= _h[l - 1];
      h *= _hd.inv[l];
    }
    if (verbose) {
      h += _hd.pow[r - l + 1];
    }
    return h();
  }
};

template <int N, int M, int X>
class RollingHash {
 private:
  using Mint = modular::Modular<int, (int)1e9 + 7>;
  deque<int> _dq;
  Mint _h;
  const HashData<M, X> &_hd;

 public:
  RollingHash(const HashData<M, X> &hd) : _hd(hd) { assert(N <= M); }

  void reset(const function<int(int)> &func, int l, int r) {
    _h = 0;
    _dq.clear();
  }

  void removeFirst() {
    _h -= _dq.front();
    _h *= _hd.inv[1];
    _dq.pop_front();
  }

  void addLast(int v) {
    _h += _hd.pow[_dq.size()] * v;
    _dq.push_back(v);
  }

  int hash(bool verbose = false) {
    Mint h = _h;
    if (verbose) {
      h += _hd.pow[_dq.size()];
    }
    return h();
  }
};

template <int N, int M, int X>
class ModifiableHash {
 private:
  using Mint = modular::Modular<int, (int)1e9 + 7>;
  Mint _vals[N];
  Mint _h;
  const HashData<M, X> &_hd;

 public:
  ModifiableHash(const HashData<M, X> &hd) : _hd(hd) {}

  void reset() {
    for (int i = 0; i < N; i++) {
      _vals[i] = 0;
    }
    _h = 0;
  }

  void set(int i, int x) {
    _h += (x - _vals[i]) * _hd.pow[i];
    _vals[i] = x;
  }

  int hash() { return _h(); }
};

}  // namespace hash

#endif