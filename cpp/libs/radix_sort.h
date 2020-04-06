#ifndef RADIX_SORT_H
#define RADIX_SORT_H

#include "common.h"

namespace radix_sort {

const int BUF_SIZE = 1 << 16;
const int MASK = BUF_SIZE - 1;
int BUCKET[BUF_SIZE];

template <class T>
void RadixSort0(vector<int> &data, const function<T(int)> &func, int rshift,
                vector<int> &output) {
  C0(BUCKET);
  int n = data.size();
  for (int i = 0; i < n; i++) {
    BUCKET[(func(data[i]) >> rshift) & MASK]++;
  }
  for (int i = 1; i < BUF_SIZE; i++) {
    BUCKET[i] += BUCKET[i - 1];
  }
  for (int i = n - 1; i >= 0; i--) {
    output[--BUCKET[(func(data[i]) >> rshift) & MASK]] = data[i];
  }
}

template <class T>
void RadixSort(vector<int> &data, const function<T(int)> &func, int l, int r) {
  vector<int> a(data.begin() + l, data.begin() + r + 1);
  vector<int> b(r - l + 1);
  for (int i = 0; i < sizeof(T); i += 4) {
    RadixSort0(a, func, i * 8, b);
    RadixSort0(b, func, (i + 2) * 8, a);
  }
  for (int i = l; i <= r; i++) {
    data[i] = a[i - l];
  }
}

};  // namespace radix_sort

#endif