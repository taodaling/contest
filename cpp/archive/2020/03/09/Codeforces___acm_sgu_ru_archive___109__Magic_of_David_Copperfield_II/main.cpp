#include "../../libs/common.h"
int n;

int Cell(int i, int j) { return i * n + j + 1; }

void solve(int testId, istream &in, ostream &out) {
  in >> n;
  int next = 101;
  int l = 0;
  int r = n - 1;

  if (n == 2) {
    out << next << ' ' << Cell(r, r) << endl;
    out << next + 2 << ' ' << Cell(l, r) << ' '
        << Cell(r, l) << endl;
    return;
  }

  if (n % 2 == 0) {
    out << next << ' ';
    next += 2;
    for (int i = l; i <= r; i++) {
      for (int j = l; j <= r; j++) {
        if (i == l || j == l) {
          if ((i + j) % 2 == 0) {
            out << Cell(i, j) << ' ';
          }
        }
      }
    }
    out << endl;
    out << next << ' ';
    next += 2;
    for (int i = l; i <= r; i++) {
      for (int j = l; j <= r; j++) {
        if (i == l || j == l) {
          if ((i + j) % 2 == 1) {
            out << Cell(i, j) << ' ';
          }
        }
      }
    }
    out << endl;
    l++;
  }

  for (; l < r; l++, r--) {
    out << next << ' ';
    next += 2;
    for (int j = l; j <= r; j++) {
      for (int k = l; k <= r; k++) {
        if (j == l || j == r || k == l || k == r) {
          if ((j + k) % 2 == 0) {
            out << Cell(j, k) << ' ';
          }
        }
      }
    }
    out << endl;
    out << next << ' ';
    next += 2;
    for (int j = l; j <= r; j++) {
      for (int k = l; k <= r; k++) {
        if (j == l || j == r || k == l || k == r) {
          if ((j + k) % 2 == 1) {
            out << Cell(j, k) << ' ';
          }
        }
      }
    }
    out << endl;
  }
}

RUN_ONCE