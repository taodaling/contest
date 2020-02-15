#ifndef BIT_H
#define BIT_H

namespace rmq
{
/**
 * Description: $N$-S range sum query with point updateate
 * Source: https://codeforces.com/blog/entry/64914
 * Verification: SPOJ matsum
 * Usage: \texttt{BIT<int,10,10>} gives a 2D BIT
 * Time: O((\log N)^S)
 */

template <class T, int... Ns>
struct BIT
{
    T val = 0;
    void update(T v) { val += v; }
    T query() { return val; }
};
template <class T, int N, int... Ns>
struct BIT<T, N, Ns...>
{
    BIT<T, Ns...> bit[N + 1];
    template <typename... Args>
    void update(int pos, Args... args)
    {
        for (; pos <= N; pos += (pos & -pos))
            bit[pos].update(args...);
    }
    template <typename... Args>
    T sum(int r, Args... args)
    {
        T res = 0;
        for (; r; r -= (r & -r))
            res += bit[r].query(args...);
        return res;
    }
    template <typename... Args>
    T query(int l, int r, Args... args) { return sum(r, args...) - sum(l - 1, args...); }
};
} // namespace rmq

#endif //BIT_H
