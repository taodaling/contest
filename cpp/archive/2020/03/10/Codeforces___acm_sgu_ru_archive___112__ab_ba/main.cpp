#include "../../libs/common.h"
#include "../../libs/fft.h"

vector<int> Pow(const vector<int> &x, int n) {
  if (n == 0) {
    return vector<int>{1};
  }
  vector<int> y = Pow(x, n / 2);
  y = fast_fourie_transform::MultiplyBigInt(y, y, 10);
  if (n & 1) {
    y = fast_fourie_transform::MultiplyBigInt(y, x, 10);
  }
  return y;
}

bool operator<(vector<int> &a, vector<int> &b) {
  fast_fourie_transform::Normalize(a);
  fast_fourie_transform::Normalize(b);
  if (a.size() != b.size()) {
    return a.size() < b.size();
  }
  for (int i = a.size() - 1; i >= 0; i--) {
    if (a[i] != b[i]) {
      return a[i] < b[i];
    }
  }
  return false;
}

void AsBase10(vector<int> &a) {
  int remain = 0;
  for (int i = 0; i < a.size(); i++) {
    a[i] -= remain;
    remain = 0;
    while (a[i] < 0) {
      a[i] += 10;
      remain += 1;
    }
  }
}

void solve(int testId, istream &in, ostream &out) {
  int a, b;
  in >> a >> b;

  vector<int> x{a % 10, a / 10 % 10, a / 100 % 10};
  vector<int> y{b % 10, b / 10 % 10, b / 100 % 10};
  fast_fourie_transform::Normalize(x);
  fast_fourie_transform::Normalize(y);
  x = Pow(x, b);
  y = Pow(y, a);

  if (x < y) {
    x.swap(y);
    out << '-';
  }

  for (int i = 0; i < y.size(); i++) {
    x[i] -= y[i];
  }
  AsBase10(x);
  fast_fourie_transform::Normalize(x);
  reverse(x.begin(), x.end());
  for (int t : x) {
    out << t;
  }
}

RUN_ONCE