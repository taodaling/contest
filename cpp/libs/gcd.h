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
    T Extgcd0(T a, T b, pair<T, T> &exp) {
        if (!b) {
            exp.first = 1;
            exp.second = 0;
            return a;
        }
        T ans = Extgcd0(b, a % b, exp);
        T n = exp.first;
        T m = exp.second;
        exp.first = m;
        exp.second = n - m * (a / b);
        return ans;
    }

    template<typename T>
    T Extgcd(T a, T b, pair<T, T> &exp) {
        if (a >= b) {
            return Extgcd0(a, b, exp);
        }
        T ans = Extgcd0(b, a, exp);
        swap(exp.first, exp.second);
        return ans;
    }
}

#endif