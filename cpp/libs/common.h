#ifndef COMMON_H
#define COMMON_H

#include"compiler_macro.h"
#include <bits/stdc++.h>
#include <chrono>
#include <random>
#include<ext/rope>
#include <iostream>
#include <fstream>
#include <iomanip>
#include<queue>

using std::function;
using __gnu_cxx::rope;
using std::cerr;
using std::deque;
using std::queue;
using std::endl;
using std::fill;
using std::ios_base;
using std::istream;
using std::iterator;
using std::make_pair;
using std::map;
using std::max;
using std::min;
using std::numeric_limits;
using std::ostream;
using std::pair;
using std::priority_queue;
using std::set;
using std::sort;
using std::string;
using std::swap;
using std::unordered_map;
using std::vector;
using std::bitset;
using std::uniform_int_distribution;
using std::uniform_real_distribution;
using std::stringstream;
using std::istream_iterator;

typedef unsigned int ui;
typedef long long ll;
typedef long double ld;
typedef unsigned long long ull;
std::mt19937 rng(std::chrono::steady_clock::now().time_since_epoch().count());

#define error(args...) { string _s = #args; replace(_s.begin(), _s.end(), ',', ' '); stringstream _ss(_s); istream_iterator<string> _it(_ss); err(_it, args); }
void err(std::istream_iterator<string> it) {}
template<typename T, typename... Args>
void err(std::istream_iterator<string> it, T a, Args... args) {
	cerr << *it << " = " << a << endl;
	err(++it, args...);
}

#define mp make_pair

#endif

#ifdef LOCAL
#define PREPARE_INPUT {std::cout << "Input file name:"; string file; std::cin >> file; file = string(__FILE__) + "/../" + file + ".in"; std::cout << "Open file:" << file << std::endl; freopen(file.data(),"r",stdin); }
#else
#define PREPARE_INPUT
#endif


#define RUN_ONCE \
int main()\
{\
    PREPARE_INPUT;\
    std::ios_base::sync_with_stdio(false);\
    std::cin.tie(0);\
    std::cout << std::setiosflags(std::ios::fixed);\
    std::cout << std::setprecision(15);\
    solve(1, std::cin, std::cout);\
    return 0;\
}

#define RUN_MULTI \
int main()\
{\
    PREPARE_INPUT;\
    std::ios_base::sync_with_stdio(false);\
    std::cin.tie(0);\
    std::cout << std::setiosflags(std::ios::fixed);\
    std::cout << std::setprecision(15);\
    int t;\
    std::cin >> t;\
    for (int i = 1; i <= t; i++)\
    {\
        solve(i, std::cin, std::cout);\
    }\
    return 0;\
}