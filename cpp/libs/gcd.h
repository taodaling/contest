#ifndef GCD_H
#define GCD_H

#include "common.h"

namespace gcd {
    template<typename T>
    T Gcd0(T a, T b) {
        return b ? Gcd0(b, a % b) : a;
    }

    template<typename T>
    T Gcd(T a, T b) {
        if (a < b) {
            swap(a, b);
        }
        return Gcd0(a, b);
    }

    template<typename T>
    T Extgcd0(T a, T b, T &x, T &y) {
        if (!b) {
            x = 1;
            y = 0;
            return a;
        }
        T ans = Extgcd0(b, a % b, y, x);
        y = y - x * (a / b);
        return ans;
    }

    /**
     * Find gcd(a, b) and expression xa+yb=g
     */
    template<typename T>
    T Extgcd(T a, T b, T &x, T &y) {
        if (a >= b) {
            return Extgcd0(a, b, x, y);
        }
        return Extgcd0(b, a, y, x);
    }
}

#endif