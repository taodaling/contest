#ifndef FACTORIAL_H
#define FACTORIAL_H

namespace factorial {
template <class T>
void Generate(vector<T> &vec) {
  int n = vec.size();
  if (n <= 0) {
    return;
  }
  vec[0] = 1;
  for (int i = 1; i < n; i++) {
    vec[i] = vec[i - 1] * i;
  }
}
template <class T>
void GenerateInverse(vector<T> &vec) {
  Generate(vec);
  int n = vec.size();
  if (n <= 0) {
    return;
  }
  vec[n - 1] = 1 / vec[n - 1];
  for (int i = n - 2; i >= 0; i--) {
    vec[i] = vec[i + 1] * T(i + 1);
  }
}
}  // namespace factorial

#endif