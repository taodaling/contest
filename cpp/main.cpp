#include <iostream>
#include <fstream>
#include <iomanip>
#include <bits/stdc++.h>
#include <chrono>
#include <random>
#include<ext/rope>

using __gnu_cxx::rope;
using std::cerr;
using std::deque;
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

typedef unsigned int ui;
typedef long long ll;
typedef long double ld;
typedef unsigned long long ull;
std::mt19937 rng(std::chrono::steady_clock::now().time_since_epoch().count());

#define trace(...) __f(#__VA_ARGS__, __VA_ARGS__)
template <typename Arg1>
void __f(const char* name, Arg1&& arg1){
  cerr << name << ": " << arg1 << endl;
}
template <typename Arg1, typename... Args>
void __f(const char* names, Arg1&& arg1, Args&&... args){
  const char* comma = strchr(names + 1, ',');
  cerr.write(names, comma - names) << ": " << arg1 << " |";
  __f(comma + 1, args...);
}

namespace dalt
{
};
namespace other
{
};

using namespace dalt;

void solve(istream &in, ostream &out)
{
}

int main()
{
    std::ios_base::sync_with_stdio(false);
    std::cin.tie(NULL);
    std::cout << std::setiosflags(std::ios::fixed);
    std::cout << std::setprecision(15);

    solve(std::cin, std::cout);

    return 0;
}
