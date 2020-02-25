#ifndef FWT_H
#define FWT_H

namespace fwt {
template <class T>
void OrFWT(vector<T> &p, int l, int r) {
  if (l == r) {
    return;
  }
  int m = (l + r) >> 1;
  OrFWT(p, l, m);
  OrFWT(p, m + 1, r);
  for (int i = 0, until = m - l; i <= until; i++) {
    T a = p[l + i];
    T b = p[m + 1 + i];
    p[m + 1 + i] = a + b;
  }
}

template <class T>
void OrIFWT(vector<T> &p, int l, int r) {
  if (l == r) {
    return;
  }
  int m = (l + r) >> 1;
  for (int i = 0, until = m - l; i <= until; i++) {
    T a = p[l + i];
    T b = p[m + 1 + i];
    p[m + 1 + i] = b - a;
  }
  OrIFWT(p, l, m);
  OrIFWT(p, m + 1, r);
}

template <class T>
void AndFWT(vector<T> &p, int l, int r) {
  if (l == r) {
    return;
  }
  int m = (l + r) >> 1;
  AndFWT(p, l, m);
  AndFWT(p, m + 1, r);
  for (int i = 0, until = m - l; i <= until; i++) {
    T a = p[l + i];
    T b = p[m + 1 + i];
    p[l + i] = a + b;
  }
}

template <class T>
void AndIFWT(vector<T> &p, int l, int r) {
  if (l == r) {
    return;
  }
  int m = (l + r) >> 1;
  for (int i = 0, until = m - l; i <= until; i++) {
    T a = p[l + i];
    T b = p[m + 1 + i];
    p[l + i] = a - b;
  }
  AndIFWT(p, l, m);
  AndIFWT(p, m + 1, r);
}

template <class T>
void XorFWT(vector<T> &p, int l, int r) {
  if (l == r) {
    return;
  }
  int m = (l + r) >> 1;
  XorFWT(p, l, m);
  XorFWT(p, m + 1, r);
  for (int i = 0, until = m - l; i <= until; i++) {
    T a = p[l + i];
    T b = p[m + 1 + i];
    p[l + i] = a + b;
    p[m + 1 + i] = a - b;
  }
}

template <class T>
void XorIFWT(vector<T> &p, int l, int r) {
  if (l == r) {
    return;
  }
  int m = (l + r) >> 1;
  for (int i = 0, until = m - l; i <= until; i++) {
    T a = p[l + i];
    T b = p[m + 1 + i];
    p[l + i] = (a + b) / 2;
    p[m + 1 + i] = (a - b) / 2;
  }
  XorIFWT(p, l, m);
  XorIFWT(p, m + 1, r);
}

template <class T>
void DotMul(vector<T> &a, vector<T> &b, int n) {
  for (int i = 0; i < n; i++) {
    a[i] = a[i] * b[i];
  }
}
}  // namespace fwt

#endif