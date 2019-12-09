#include<iostream>
using namespace std;

class LibMat
{
public:
    LibMat(){cout << "LibMat::LibMat()" << endl;}
    virtual ~LibMat(){cout << "LibMat::~LibMat()" << endl;}
    virtual void print() const = 0; //pure virtual function
};
class Book : public LibMat
{
public:
    Book(const string &title, const string &author): _title(title), _author(author) {cout << "Book::Book()" << endl;}
    ~Book(){cout << "Book::~Book()" << endl;}
    void print() const{cout << "Book::print()" << endl;}
protected:
    string _title;
    string _author;
};
class AudioBook: public Book
{
public:
    AudioBook(const string &title, const string &author, const string narrator)
        :Book(title, author), _narrator(narrator){}
    ~AudioBook(){cout << "AudioBook::~AudioBook()" << endl;}
protected:
    string _narrator;
};

int main()
{
	AudioBook book("example", "dalt", "nothing");
	LibMat &ref = book;
	ref.print();
	return 0;
}