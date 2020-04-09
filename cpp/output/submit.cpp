#ifndef COMMON_H
#define COMMON_H

#include <bits/stdc++.h>
#include <algorithm>
#include <chrono>
#include <complex>
#include <ext/rope>
#include <fstream>
#include <iomanip>
#include <iostream>
#include <queue>
#include <random>
#ifndef COMPILER_MACRO_H
#define COMPILER_MACRO_H

#ifndef LOCAL

#pragma GCC target("avx")
#pragma GCC optimize(3)
#pragma GCC optimize("Ofast")
#pragma GCC target("sse4.2")
#pragma GCC optimize("inline")

#endif

#endif

using __gnu_cxx::rope;
using std::array;
using std::bitset;
using std::cerr;
using std::complex;
using std::deque;
using std::endl;
using std::fill;
using std::function;
using std::ios_base;
using std::istream;
using std::istream_iterator;
using std::iterator;
using std::make_pair;
using std::make_tuple;
using std::map;
using std::max;
using std::min;
using std::numeric_limits;
using std::ostream;
using std::pair;
using std::priority_queue;
using std::queue;
using std::reverse;
using std::rotate;
using std::set;
using std::sort;
using std::string;
using std::stringstream;
using std::swap;
using std::uniform_int_distribution;
using std::uniform_real_distribution;
using std::unique;
using std::unordered_map;
using std::vector;
using std::tuple;
using std::get;
using std::multiset;
using std::multimap;

typedef unsigned int ui;
typedef long long ll;
typedef long double ld;
typedef unsigned long long ull;
typedef unsigned char byte;
std::mt19937 rng(std::chrono::steady_clock::now().time_since_epoch().count());

const double E = 2.7182818284590452354;
const double PI = 3.14159265358979323846;

#define X(x) (x).first
#define Y(x) (x).second


#ifdef LOCAL
#define PREPARE_INPUT                                  \
  {                                                    \
    std::cout << "Input file name:";                   \
    string file;                                       \
    std::cin >> file;                                  \
    if (file != "stdin") {                             \
      file = string(__FILE__) + "/../" + file + ".in"; \
      std::cout << "Open file:" << file << std::endl;  \
      freopen(file.data(), "r", stdin);                \
    }                                                  \
  }
#else
#define PREPARE_INPUT
#endif

#define RUN_ONCE                                    \
  int main() {                                      \
    PREPARE_INPUT;                                  \
    std::ios_base::sync_with_stdio(false);          \
    std::cin.tie(0);                                \
    std::cout << std::setiosflags(std::ios::fixed); \
    std::cout << std::setprecision(15);             \
    solve(1, std::cin, std::cout);                  \
    return 0;                                       \
  }

#define RUN_MULTI                                   \
  int main() {                                      \
    PREPARE_INPUT;                                  \
    std::ios_base::sync_with_stdio(false);          \
    std::cin.tie(0);                                \
    std::cout << std::setiosflags(std::ios::fixed); \
    std::cout << std::setprecision(15);             \
    int t;                                          \
    std::cin >> t;                                  \
    for (int i = 1; i <= t; i++) {                  \
      solve(i, std::cin, std::cout);                \
    }                                               \
    return 0;                                       \
  }

#endif

#define C0(x) memset(x, 0, sizeof(x))
#define C1(x) memset(x, -1, sizeof(x))
#ifndef DEBUG_H
#define DEBUG_H
//          Copyright Louis Delacroix 2010 - 2014.
// Distributed under the Boost Software License, Version 1.0.
//    (See accompanying file LICENSE_1_0.txt or copy at
//          http://www.boost.org/LICENSE_1_0.txt)
//
// A pretty printing library for C++
//
// Usage:
// Include this header, and operator<< will "just work".

#ifndef H_PRETTY_PRINT
#define H_PRETTY_PRINT

#include <cstddef>
#include <iterator>
#include <memory>
#include <ostream>
#include <set>
#include <tuple>
#include <type_traits>
#include <unordered_set>
#include <utility>
#include <valarray>

namespace pretty_print
{
    namespace detail
    {
        // SFINAE type trait to detect whether T::const_iterator exists.

        struct sfinae_base
        {
            using yes = char;
            using no  = yes[2];
        };

        template <typename T>
        struct has_const_iterator : private sfinae_base
        {
        private:
            template <typename C> static yes & test(typename C::const_iterator*);
            template <typename C> static no  & test(...);
        public:
            static const bool value = sizeof(test<T>(nullptr)) == sizeof(yes);
            using type =  T;
        };

        template <typename T>
        struct has_begin_end : private sfinae_base
        {
        private:
            template <typename C>
            static yes & f(typename std::enable_if<
                std::is_same<decltype(static_cast<typename C::const_iterator(C::*)() const>(&C::begin)),
                             typename C::const_iterator(C::*)() const>::value>::type *);

            template <typename C> static no & f(...);

            template <typename C>
            static yes & g(typename std::enable_if<
                std::is_same<decltype(static_cast<typename C::const_iterator(C::*)() const>(&C::end)),
                             typename C::const_iterator(C::*)() const>::value, void>::type*);

            template <typename C> static no & g(...);

        public:
            static bool const beg_value = sizeof(f<T>(nullptr)) == sizeof(yes);
            static bool const end_value = sizeof(g<T>(nullptr)) == sizeof(yes);
        };

    }  // namespace detail


    // Holds the delimiter values for a specific character type

    template <typename TChar>
    struct delimiters_values
    {
        using char_type = TChar;
        const char_type * prefix;
        const char_type * delimiter;
        const char_type * postfix;
    };


    // Defines the delimiter values for a specific container and character type

    template <typename T, typename TChar>
    struct delimiters
    {
        using type = delimiters_values<TChar>;
        static const type values; 
    };


    // Functor to print containers. You can use this directly if you want
    // to specificy a non-default delimiters type. The printing logic can
    // be customized by specializing the nested template.

    template <typename T,
              typename TChar = char,
              typename TCharTraits = ::std::char_traits<TChar>,
              typename TDelimiters = delimiters<T, TChar>>
    struct print_container_helper
    {
        using delimiters_type = TDelimiters;
        using ostream_type = std::basic_ostream<TChar, TCharTraits>;

        template <typename U>
        struct printer
        {
            static void print_body(const U & c, ostream_type & stream)
            {
                using std::begin;
                using std::end;

                auto it = begin(c);
                const auto the_end = end(c);

                if (it != the_end)
                {
                    for ( ; ; )
                    {
                        stream << *it;

                    if (++it == the_end) break;

                    if (delimiters_type::values.delimiter != NULL)
                        stream << delimiters_type::values.delimiter;
                    }
                }
            }
        };

        print_container_helper(const T & container)
        : container_(container)
        { }

        inline void operator()(ostream_type & stream) const
        {
            if (delimiters_type::values.prefix != NULL)
                stream << delimiters_type::values.prefix;

            printer<T>::print_body(container_, stream);

            if (delimiters_type::values.postfix != NULL)
                stream << delimiters_type::values.postfix;
        }

    private:
        const T & container_;
    };

    // Specialization for pairs

    template <typename T, typename TChar, typename TCharTraits, typename TDelimiters>
    template <typename T1, typename T2>
    struct print_container_helper<T, TChar, TCharTraits, TDelimiters>::printer<std::pair<T1, T2>>
    {
        using ostream_type = typename print_container_helper<T, TChar, TCharTraits, TDelimiters>::ostream_type;

        static void print_body(const std::pair<T1, T2> & c, ostream_type & stream)
        {
            stream << c.first;
            if (print_container_helper<T, TChar, TCharTraits, TDelimiters>::delimiters_type::values.delimiter != NULL)
                stream << print_container_helper<T, TChar, TCharTraits, TDelimiters>::delimiters_type::values.delimiter;
            stream << c.second;
        }
    };

    // Specialization for tuples

    template <typename T, typename TChar, typename TCharTraits, typename TDelimiters>
    template <typename ...Args>
    struct print_container_helper<T, TChar, TCharTraits, TDelimiters>::printer<std::tuple<Args...>>
    {
        using ostream_type = typename print_container_helper<T, TChar, TCharTraits, TDelimiters>::ostream_type;
        using element_type = std::tuple<Args...>;

        template <std::size_t I> struct Int { };

        static void print_body(const element_type & c, ostream_type & stream)
        {
            tuple_print(c, stream, Int<0>());
        }

        static void tuple_print(const element_type &, ostream_type &, Int<sizeof...(Args)>)
        {
        }

        static void tuple_print(const element_type & c, ostream_type & stream,
                                typename std::conditional<sizeof...(Args) != 0, Int<0>, std::nullptr_t>::type)
        {
            stream << std::get<0>(c);
            tuple_print(c, stream, Int<1>());
        }

        template <std::size_t N>
        static void tuple_print(const element_type & c, ostream_type & stream, Int<N>)
        {
            if (print_container_helper<T, TChar, TCharTraits, TDelimiters>::delimiters_type::values.delimiter != NULL)
                stream << print_container_helper<T, TChar, TCharTraits, TDelimiters>::delimiters_type::values.delimiter;

            stream << std::get<N>(c);

            tuple_print(c, stream, Int<N + 1>());
        }
    };

    // Prints a print_container_helper to the specified stream.

    template<typename T, typename TChar, typename TCharTraits, typename TDelimiters>
    inline std::basic_ostream<TChar, TCharTraits> & operator<<(
        std::basic_ostream<TChar, TCharTraits> & stream,
        const print_container_helper<T, TChar, TCharTraits, TDelimiters> & helper)
    {
        helper(stream);
        return stream;
    }


    // Basic is_container template; specialize to derive from std::true_type for all desired container types

    template <typename T>
    struct is_container : public std::integral_constant<bool,
                                                        detail::has_const_iterator<T>::value &&
                                                        detail::has_begin_end<T>::beg_value  &&
                                                        detail::has_begin_end<T>::end_value> { };

    template <typename T, std::size_t N>
    struct is_container<T[N]> : std::true_type { };

    template <std::size_t N>
    struct is_container<char[N]> : std::false_type { };

    template <typename T>
    struct is_container<std::valarray<T>> : std::true_type { };

    template <typename T1, typename T2>
    struct is_container<std::pair<T1, T2>> : std::true_type { };

    template <typename ...Args>
    struct is_container<std::tuple<Args...>> : std::true_type { };


    // Default delimiters

    template <typename T> struct delimiters<T, char> { static const delimiters_values<char> values; };
    template <typename T> const delimiters_values<char> delimiters<T, char>::values = { "[", ", ", "]" };
    template <typename T> struct delimiters<T, wchar_t> { static const delimiters_values<wchar_t> values; };
    template <typename T> const delimiters_values<wchar_t> delimiters<T, wchar_t>::values = { L"[", L", ", L"]" };


    // Delimiters for (multi)set and unordered_(multi)set

    template <typename T, typename TComp, typename TAllocator>
    struct delimiters< ::std::set<T, TComp, TAllocator>, char> { static const delimiters_values<char> values; };

    template <typename T, typename TComp, typename TAllocator>
    const delimiters_values<char> delimiters< ::std::set<T, TComp, TAllocator>, char>::values = { "{", ", ", "}" };

    template <typename T, typename TComp, typename TAllocator>
    struct delimiters< ::std::set<T, TComp, TAllocator>, wchar_t> { static const delimiters_values<wchar_t> values; };

    template <typename T, typename TComp, typename TAllocator>
    const delimiters_values<wchar_t> delimiters< ::std::set<T, TComp, TAllocator>, wchar_t>::values = { L"{", L", ", L"}" };

    template <typename T, typename TComp, typename TAllocator>
    struct delimiters< ::std::multiset<T, TComp, TAllocator>, char> { static const delimiters_values<char> values; };

    template <typename T, typename TComp, typename TAllocator>
    const delimiters_values<char> delimiters< ::std::multiset<T, TComp, TAllocator>, char>::values = { "{", ", ", "}" };

    template <typename T, typename TComp, typename TAllocator>
    struct delimiters< ::std::multiset<T, TComp, TAllocator>, wchar_t> { static const delimiters_values<wchar_t> values; };

    template <typename T, typename TComp, typename TAllocator>
    const delimiters_values<wchar_t> delimiters< ::std::multiset<T, TComp, TAllocator>, wchar_t>::values = { L"{", L", ", L"}" };

    template <typename T, typename THash, typename TEqual, typename TAllocator>
    struct delimiters< ::std::unordered_set<T, THash, TEqual, TAllocator>, char> { static const delimiters_values<char> values; };

    template <typename T, typename THash, typename TEqual, typename TAllocator>
    const delimiters_values<char> delimiters< ::std::unordered_set<T, THash, TEqual, TAllocator>, char>::values = { "{", ", ", "}" };

    template <typename T, typename THash, typename TEqual, typename TAllocator>
    struct delimiters< ::std::unordered_set<T, THash, TEqual, TAllocator>, wchar_t> { static const delimiters_values<wchar_t> values; };

    template <typename T, typename THash, typename TEqual, typename TAllocator>
    const delimiters_values<wchar_t> delimiters< ::std::unordered_set<T, THash, TEqual, TAllocator>, wchar_t>::values = { L"{", L", ", L"}" };

    template <typename T, typename THash, typename TEqual, typename TAllocator>
    struct delimiters< ::std::unordered_multiset<T, THash, TEqual, TAllocator>, char> { static const delimiters_values<char> values; };

    template <typename T, typename THash, typename TEqual, typename TAllocator>
    const delimiters_values<char> delimiters< ::std::unordered_multiset<T, THash, TEqual, TAllocator>, char>::values = { "{", ", ", "}" };

    template <typename T, typename THash, typename TEqual, typename TAllocator>
    struct delimiters< ::std::unordered_multiset<T, THash, TEqual, TAllocator>, wchar_t> { static const delimiters_values<wchar_t> values; };

    template <typename T, typename THash, typename TEqual, typename TAllocator>
    const delimiters_values<wchar_t> delimiters< ::std::unordered_multiset<T, THash, TEqual, TAllocator>, wchar_t>::values = { L"{", L", ", L"}" };


    // Delimiters for pair and tuple

    template <typename T1, typename T2> struct delimiters<std::pair<T1, T2>, char> { static const delimiters_values<char> values; };
    template <typename T1, typename T2> const delimiters_values<char> delimiters<std::pair<T1, T2>, char>::values = { "(", ", ", ")" };
    template <typename T1, typename T2> struct delimiters< ::std::pair<T1, T2>, wchar_t> { static const delimiters_values<wchar_t> values; };
    template <typename T1, typename T2> const delimiters_values<wchar_t> delimiters< ::std::pair<T1, T2>, wchar_t>::values = { L"(", L", ", L")" };

    template <typename ...Args> struct delimiters<std::tuple<Args...>, char> { static const delimiters_values<char> values; };
    template <typename ...Args> const delimiters_values<char> delimiters<std::tuple<Args...>, char>::values = { "(", ", ", ")" };
    template <typename ...Args> struct delimiters< ::std::tuple<Args...>, wchar_t> { static const delimiters_values<wchar_t> values; };
    template <typename ...Args> const delimiters_values<wchar_t> delimiters< ::std::tuple<Args...>, wchar_t>::values = { L"(", L", ", L")" };


    // Type-erasing helper class for easy use of custom delimiters.
    // Requires TCharTraits = std::char_traits<TChar> and TChar = char or wchar_t, and MyDelims needs to be defined for TChar.
    // Usage: "cout << pretty_print::custom_delims<MyDelims>(x)".

    struct custom_delims_base
    {
        virtual ~custom_delims_base() { }
        virtual std::ostream & stream(::std::ostream &) = 0;
        virtual std::wostream & stream(::std::wostream &) = 0;
    };

    template <typename T, typename Delims>
    struct custom_delims_wrapper : custom_delims_base
    {
        custom_delims_wrapper(const T & t_) : t(t_) { }

        std::ostream & stream(std::ostream & s)
        {
            return s << print_container_helper<T, char, std::char_traits<char>, Delims>(t);
        }

        std::wostream & stream(std::wostream & s)
        {
            return s << print_container_helper<T, wchar_t, std::char_traits<wchar_t>, Delims>(t);
        }

    private:
        const T & t;
    };

    template <typename Delims>
    struct custom_delims
    {
        template <typename Container>
        custom_delims(const Container & c) : base(new custom_delims_wrapper<Container, Delims>(c)) { }

        std::unique_ptr<custom_delims_base> base;
    };

    template <typename TChar, typename TCharTraits, typename Delims>
    inline std::basic_ostream<TChar, TCharTraits> & operator<<(std::basic_ostream<TChar, TCharTraits> & s, const custom_delims<Delims> & p)
    {
        return p.base->stream(s);
    }


    // A wrapper for a C-style array given as pointer-plus-size.
    // Usage: std::cout << pretty_print_array(arr, n) << std::endl;

    template<typename T>
    struct array_wrapper_n
    {
        typedef const T * const_iterator;
        typedef T value_type;

        array_wrapper_n(const T * const a, size_t n) : _array(a), _n(n) { }
        inline const_iterator begin() const { return _array; }
        inline const_iterator end() const { return _array + _n; }

    private:
        const T * const _array;
        size_t _n;
    };


    // A wrapper for hash-table based containers that offer local iterators to each bucket.
    // Usage: std::cout << bucket_print(m, 4) << std::endl;  (Prints bucket 5 of container m.)

    template <typename T>
    struct bucket_print_wrapper
    {
        typedef typename T::const_local_iterator const_iterator;
        typedef typename T::size_type size_type;

        const_iterator begin() const
        {
            return m_map.cbegin(n);
        }

        const_iterator end() const
        {
            return m_map.cend(n);
        }

        bucket_print_wrapper(const T & m, size_type bucket) : m_map(m), n(bucket) { }

    private:
        const T & m_map;
        const size_type n;
    };

}   // namespace pretty_print


// Global accessor functions for the convenience wrappers

template<typename T>
inline pretty_print::array_wrapper_n<T> pretty_print_array(const T * const a, size_t n)
{
    return pretty_print::array_wrapper_n<T>(a, n);
}

template <typename T> pretty_print::bucket_print_wrapper<T>
bucket_print(const T & m, typename T::size_type n)
{
    return pretty_print::bucket_print_wrapper<T>(m, n);
}


// Main magic entry point: An overload snuck into namespace std.
// Can we do better?

namespace std
{
    // Prints a container to the stream using default delimiters

    template<typename T, typename TChar, typename TCharTraits>
    inline typename enable_if< ::pretty_print::is_container<T>::value,
                              basic_ostream<TChar, TCharTraits> &>::type
    operator<<(basic_ostream<TChar, TCharTraits> & stream, const T & container)
    {
        return stream << ::pretty_print::print_container_helper<T, TChar, TCharTraits>(container);
    }
}



#endif  // H_PRETTY_PRINT

#ifdef LOCAL
#define dbg(args...)                         \
  {                                          \
    string _s = #args;                       \
    replace(_s.begin(), _s.end(), ',', ' '); \
    stringstream _ss(_s);                    \
    istream_iterator<string> _it(_ss);       \
    err(_it, args);                          \
  }
void err(std::istream_iterator<string> it) {}
template <typename T, typename... Args>
void err(std::istream_iterator<string> it, T a, Args... args) {
  cerr << *it << " = " << a << endl;
  err(++it, args...);
}
#define dbg2(x) cerr << #x << "=" << (x) << endl
#else
#define dbg(args...) 42
#define dbg2(x) 42
#endif

#endif


namespace treap {
class Treap {
 private:
  Treap(bool x) {
    Treap();
    _left = this;
    _right = this;
    _size = 0;
  }

 public:
  Treap() {
    _left = NIL;
    _right = NIL;
    _size = 1;
    _key = 0;
  }

  static Treap *NIL;
  Treap *_left;
  Treap *_right;
  int _size;
  int _key;

  void pushDown() {
    if (this == NIL) {
      return;
    }
  }

  void pushUp() {
    if (this == NIL) {
      return;
    }
    _size = _left->_size + _right->_size + 1;
  }

  /**
   * split by rank and the node whose rank is argument will stored at result[0]
   */
  static pair<Treap *, Treap *> SplitByRank(Treap *root, int rank) {
    if (root == NIL) {
      return make_pair(NIL, NIL);
    }
    root->pushDown();
    pair<Treap *, Treap *> result;
    if (root->_left->_size >= rank) {
      result = SplitByRank(root->_left, rank);
      root->_left = result.second;
      result.second = root;
    } else {
      result =
          SplitByRank(root->_right, rank - (root->_size - root->_right->_size));
      root->_right = result.first;
      result.first = root;
    }
    root->pushUp();
    return result;
  }

  static Treap *Merge(Treap *a, Treap *b) {
    if (a == NIL) {
      return b;
    }
    if (b == NIL) {
      return a;
    }
    if (uniform_int_distribution<int>(1, a->_size + b->_size)(rng) <=
        a->_size) {
      a->pushDown();
      a->_right = Merge(a->_right, b);
      a->pushUp();
      return a;
    } else {
      b->pushDown();
      b->_left = Merge(a, b->_left);
      b->pushUp();
      return b;
    }
  }

  static Treap *DeepClone(const Treap *root) {
    if (root == NIL) {
      return NIL;
    }
    Treap *cl = new Treap(*root);
    cl->_left = DeepClone(cl->_left);
    cl->_right = DeepClone(cl->_right);
    return cl;
  }

  /**
   * nodes with key <= arguments will stored at result[0]
   */
  static pair<Treap *, Treap *> SplitByKey(Treap *root, int key) {
    if (root == NIL) {
      return make_pair(NIL, NIL);
    }
    root->pushDown();
    pair<Treap *, Treap *> result;
    if (root->_key > key) {
      result = SplitByKey(root->_left, key);
      root->_left = result.second;
      result.second = root;
    } else {
      result = SplitByKey(root->_right, key);
      root->_right = result.first;
      result.first = root;
    }
    root->pushUp();
    return result;
  }

  static int GetKeyByRank(Treap *treap, int k) {
    while (treap->_size > 1) {
      treap->pushDown();
      if (treap->_left->_size >= k) {
        treap = treap->_left;
      } else {
        k -= treap->_left->_size;
        if (k == 1) {
          break;
        }
        k--;
        treap = treap->_right;
      }
    }
    return treap->_key;
  }

  static int GetRankByKey(Treap *treap, int key) {
    int rank = 0;
    while (treap != NIL) {
      if (treap->_key == key) {
        rank += treap->_left->_size + 1;
        return rank;
      } else if (treap->_key < key) {
        rank += treap->_left->_size + 1;
        treap = treap->_right;
      } else {
        treap = treap->_left;
      }
    }
    return rank;
  }
};

Treap *Treap::NIL = new Treap(false);

void dfs(ostream &out, Treap *root) {
  if (root == Treap::NIL) {
    return;
  }
  root->pushDown();
  dfs(out, root->_left);
  out << root->_key << ',';
  dfs(out, root->_right);
}
ostream &operator<<(ostream &out, const Treap *root) {
  dfs(out, Treap::DeepClone(root));
  return out;
}
}  // namespace treap

using namespace treap;
using pt = pair<Treap *, Treap *>;

vector<int> cubes(1000000 + 100);
vector<int> length(1000000 + 100);
vector<ll> total(1000000 + 100);
Treap *root = Treap::NIL;

int getByRank(int rank) { return Treap::GetKeyByRank(root, rank); }

int nearKey(int key) {
  int prev = -1;
  pt split = Treap::SplitByKey(root, key);
  if (X(split)->_size > 0) {
    prev = Treap::GetKeyByRank(X(split), X(split)->_size);
  }
  root = Treap::Merge(X(split), Y(split));
  return prev;
}

void removeKey(int key) {
  pt split = Treap::SplitByKey(root, key);
  pt lsplit = Treap::SplitByKey(X(split), key - 1);
  X(split) = X(lsplit);
  root = Treap::Merge(X(split), Y(split));
}

void addKey(int key) {
  Treap *t = new Treap();
  t->_key = key;
  pt split = Treap::SplitByKey(root, key);
  X(split) = Treap::Merge(X(split), t);
  root = Treap::Merge(X(split), Y(split));
}

void MergeKey(int a, int b) {
  if (a > b) {
    swap(a, b);
  }
  if (a + length[a] < b) {
    return;
  }
  removeKey(b);
  length[a] += length[b];
  total[a] += total[b];
}

void solve(int testId, istream &in, ostream &out) {
  stringstream ss;

  int n;
  in >> n;
  for (int i = 0; i < n; i++) {
    string s;
    in >> s;
    if (s == "put") {
      int x, c;
      in >> x >> c;
      cubes[x] += c;
      if (cubes[x] == c) {
        length[x] = 1;
        total[x] += c;
        addKey(x);
        if (cubes[x + 1]) {
          MergeKey(x, x + 1);
        }
        if (cubes[x - 1]) {
          MergeKey(nearKey(x - 1), x);
        }
      }else{
        int offset = nearKey(x);
        total[offset] += c;
      }
    } else if (s == "tput") {
      int t, x, c;
      in >> t >> x >> c;
      int offset = getByRank(t);
      cubes[offset + x - 1] += c;
      total[offset] += c;
    } else if (s == "towers") {
      int ans = root->_size;
      ss << ans << " towers" << endl;
    } else if (s == "length") {
      int t;
      in >> t;
      int offset = getByRank(t);
      ss << "length of " << t << "th tower is " << length[offset] << endl;
    } else if (s == "cubes") {
      int t;
      in >> t;
      int offset = getByRank(t);
      ss << total[offset] << " cubes in " << t << "th tower" << endl;
    } else if (s == "tcubes") {
      int t, x;
      in >> t >> x;
      int offset = getByRank(t);
      ss << cubes[offset + x - 1] << " cubes in " << x << "th column of " << t
         << "th tower" << endl;
    } else {
      exit(1);
    }

    dbg(root);
  }

  out << ss.str();
}

RUN_ONCE