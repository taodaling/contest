#include "../../libs/common.h"

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  vector<int> cand(n);

  int sum = 0;
  for (int i = 0; i < n; i++) {
    in >> cand[i];
    sum += cand[i];
  }

  vector<int> ratio(n);
  vector<bool> flexible(n);
  int sumOfRatio = 0;
  for (int i = 0; i < n; i++) {
    int v = cand[i] * 100;
    ratio[i] = v / sum;
    flexible[i] = v % sum != 0;
    sumOfRatio += ratio[i];
  }

  for (int i = 0; i < n && sumOfRatio < 100; i++) {
    if (flexible[i]) {
      ratio[i]++;
      sumOfRatio++;
    }
  }

  if(sumOfRatio < 100){
    out << "No solution";
    return;
  }

  for(int r : ratio){
    out << r << ' ';
  }
}

RUN_ONCE