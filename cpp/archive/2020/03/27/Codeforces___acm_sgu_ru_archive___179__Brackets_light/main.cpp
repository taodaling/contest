#include "../../libs/common.h"

void solve(int testId, istream &in, ostream &out) {
  string s;
  in >> s;
  int first = s.length() - 1;
  int tail = 0;
  while (first >= 0 && s[first] == ')' && s[first - 1] == '(') {
    tail++;
    first -= 2;
  }
  if (first < 0) {
    out << "No solution";
    return;
  }

  while (s[first] == ')') {
    first--;
  }
  swap(s[first], s[first + 1]);
  first++;

  int cnt = 0;
  for (int i = 0; i <= first; i++) {
    out << s[i];
    cnt++;
  }

  for (int i = 0; i < tail; i++) {
    out << '(';
    cnt++;
  }

  while (cnt < s.length()) {
    out << ')';
    cnt++;
  }
}

RUN_ONCE