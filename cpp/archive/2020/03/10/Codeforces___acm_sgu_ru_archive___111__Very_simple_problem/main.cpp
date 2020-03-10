#include "../../libs/common.h"

void copy(vector<int> &a, vector<int> &b, int n) {
  for (int i = 0; i < n; i++) {
    b[i] = a[i];
  }
}

bool operator<(vector<int> &a, vector<int> &b) {
  for (int i = a.size() - 1; i >= 0; i--) {
    if (a[i] != b[i]) {
      return a[i] < b[i];
    }
  }
  return false;
}

void normalize(vector<int> &x) {
  int exceed = 0;
  for (int i = 0, end = x.size(); i < end; i++) {
    exceed = exceed / 10 + x[i];
    x[i] = exceed % 10;
  }
}

void solve(int testId, istream &in, ostream &out) {
  string s;
  in >> s;
  reverse(s.begin(), s.end());

  int len = s.length() + 10;
  vector<int> y(len);
  for (int i = 0; i < s.length(); i++) {
    y[i] = s[i] - '0';
  }

  vector<int> x2(len);
  vector<int> tmp(len);
  vector<int> x(len);

  for (int i = s.length() / 2 + 1; i >= 0; i--) {
    while (x[i] < 9) {
      for (int j = 0; j < len; j++) {
        tmp[j] = x2[j];
        if (j >= i) {
          tmp[j] += 2 * x[j - i];
        }
      }
      tmp[i + i]++;
      normalize(tmp);
      if (y < tmp) {
        break;
      }
      x2.swap(tmp);
      x[i]++;
    }
  }

  int r = x.size() - 1;
  while (x[r] == 0) {
    r--;
  }
  for (int i = r; i >= 0; i--) {
    out << x[i];
  }
}

RUN_ONCE