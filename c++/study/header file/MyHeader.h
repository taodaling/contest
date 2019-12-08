#ifndef MY_HEADER
#define MY_HEADER
extern int id;
const double PI = 3.1415926;
template<typename T> T& min(T&a, T&b){return a < b ? a : b;}
inline void swap(int &a, int &b){int tmp = a; a = b; b = tmp;}
#endif
