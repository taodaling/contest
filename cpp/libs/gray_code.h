#ifndef GRAY_CODE_H
#define GRAY_CODE_H

#include "common.h"

namespace gray_code {
inline ui Transform(ui x) { return x ^ (x >> 1); }

inline ui Inverse(ui x) {
  int y = 0;
  for (; x != 0; x >>= 1) {
    y ^= x;
  }
  return y;
}
}  // namespace gray_code

#endif