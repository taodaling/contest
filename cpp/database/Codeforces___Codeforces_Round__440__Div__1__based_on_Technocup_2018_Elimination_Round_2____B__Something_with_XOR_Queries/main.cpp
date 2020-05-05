#include "../../libs/common.h"

vector<int> p0b;
vector<int> pb0;

int xorPB(int i, int j) { return p0b[j] ^ pb0[i] ^ p0b[0]; }

int xorPP(int i, int j) { return pb0[i] ^ pb0[j]; }

int ask(int a, int b) {
  std::cout << "? " << a << " " << b << endl;
  std::cout.flush();
  int ans;
  std::cin >> ans;
  return ans;
}

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;

  p0b.resize(n);
  pb0.resize(n);
  for (int i = 0; i < n; i++) {
    p0b[i] = ask(0, i);
    pb0[i] = ask(i, 0);
  }

  vector<int> p(n);
  vector<int> b(n);

  vector<int> ansP;
  int possible = 0;
  for (int i = 0; i < n; i++) {
    p[0] = i;
    for (int j = 1; j < n; j++) {
      p[j] = xorPP(0, j) ^ p[0];
    }
    for (int j = 0; j < n; j++) {
      b[j] = xorPB(0, j) ^ p[0];
    }

    bool valid = true;
    for (int j = 0; j < n; j++) {
      if (p[j] >= n || b[p[j]] != j) {
        valid = false;
        break;
      }
    }

    if (!valid) {
      continue;
    }
    possible++;
    if (possible == 1) {
      ansP = p;
    }
  }

  out << ("!") << endl << possible << endl;
  if (possible == 0) {
    return;
  }
  for (int x : ansP) {
    out << x << ' ';
  }
}

RUN_ONCE