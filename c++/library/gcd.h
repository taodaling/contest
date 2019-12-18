//
// Created by daltao on 2019/12/10.
//

#ifndef GCD_H
#define GCD_H

#include "common.h"

namespace dalt {
    template<typename T>
    T gcd(T a, T b) {
        if (a < b) {
            swap(a, b);
        }
        return gcd0(a, b);
    }

    template<typename T>
    T gcd0(T a, T b) {
        return b ? gcd0(b, a % b) : a;
    }

    template<typename T>
    T extgcd(T a, T b, pair<T, T> &exp) {
        if (a >= b) {
            return extgcd0(a, b, exp);
        }
        T ans = extgcd0(b, a, exp);
        swap(exp.first, exp.second);
        return ans;
    }

    template<typename T>
    T extgcd0(T a, T b, pair<T, T> &exp) {
        if (!b) {
            exp.first = 1;
            exp.second = 0;
            return a;
        }
        T ans = extgcd0(b, a % b, exp);
        T n = exp.first;
        T m = exp.second;
        exp.first = m;
        exp.second = n - m * (a / b);
        return ans;
    }
}

#endif //GCD_H
