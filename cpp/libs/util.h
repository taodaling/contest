#ifndef UTIL_H
#define UTIL_H

#include "common.h"

namespace util {
vector<int> Range(int l, int r) {
  vector<int> ans;
  ans.reserve(r - l + 1);
  for (int i = l; i <= r; i++) {
    ans.push_back(i);
  }
  return ans;
}
}  // namespace util

#endif