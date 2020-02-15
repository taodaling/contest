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
} // namespace bits

#endif