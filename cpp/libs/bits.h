#ifndef BITS_H
#define BITS_H

#include "common.h"

namespace bits
{
template <class T>
inline bool BitAt(T x, int i)
{
    return (x >> i) & 1;
}
template <class T>
inline T SetBit(T x, int i)
{
    return x |= T(1) << i;
}
template <class T>
inline T RemoveBit(T x, int i)
{
    return x &= ~(T(1) << i);
}
template <class T>
inline T LowestOneBit(T x)
{
    return x & -x;
}
inline int FloorLog2(unsigned int x)
{
    if (x == 0)
    {
        return 0;
    }
    return (sizeof(unsigned int) * 8) - 1 - __builtin_clz(x);
}
inline int FloorLog2(unsigned long long x)
{
    if (x == 0)
    {
        return 0;
    }
    return (sizeof(unsigned long long) * 8) - 1 - __builtin_clzll(x);
}
inline int CeilLog2(unsigned int x)
{
    if (x == 0)
    {
        return 0;
    }
    return (sizeof(unsigned int) * 8) - __builtin_clz(x - 1);
}
inline int CeilLog2(unsigned long long x)
{
    if (x == 0)
    {
        return 0;
    }
    return (sizeof(unsigned long long) * 8) - __builtin_clzll(x - 1);
}
template <class T>
inline T HighestOneBit(T x)
{
    return T(1) << FloorLog2(x);
}
inline int CountOne(unsigned int x)
{
    return __builtin_popcount(x);
}
inline int CountOne(unsigned long long x)
{
    return __builtin_popcountll(x);
}
} // namespace bits
#endif