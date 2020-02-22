package on2020_02.on2020_02_21_.Task;



import graphs.flows.MinCostFlowPolynomial;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.LongCostFlowEdge;
import template.primitve.generated.graph.LongFlow;
import template.primitve.generated.graph.LongMinimumCostFlow;
import template.primitve.generated.graph.LongSpfaMinimumCostFlow;
import template.rand.Randomized;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Task {
    int l;
    int b;
    int d;

    public int library(int i) {
        return i;
    }

    public int library(Library i) {
        return library(i.id);
    }

    public int book(int i) {
        return l + i;
    }


    public int book(Book i) {
        return book(i.id);
    }


    public int src() {
        return l + b + 1;
    }

    public int sink() {
        return src() + 1;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        b = in.readInt();
        l = in.readInt();
        d = in.readInt();

        Book[] books = new Book[b];
        for (int i = 0; i < b; i++) {
            books[i] = new Book();
            books[i].id = i;
            books[i].score = in.readInt();
        }

        Library[] libraries = new Library[l];
        for (int i = 0; i < l; i++) {
            int bookNum = in.readInt();
            libraries[i] = new Library();
            libraries[i].books = new ArrayList<>(bookNum);
            libraries[i].daySignUp = in.readInt();
            libraries[i].perDay = in.readInt();
            libraries[i].id = i;
            for (int j = 0; j < bookNum; j++) {
                libraries[i].books.add(books[in.readInt()]);
            }
        }
        libraries = Stream.of(libraries).filter(x -> !x.books.isEmpty() && x.daySignUp < d)
                .toArray(x -> new Library[x]);
        l = libraries.length;

        long time = System.currentTimeMillis() + 10000;
        while (System.currentTimeMillis() < time) {
            Library[] clone = libraries.clone();
            Randomized.shuffle(clone, 0, clone.length);
            optimize(clone, books);
        }

        out.println(libraryOrder.size());
        for (SolutionLibrary library : libraryOrder) {
            out.append(library.library.id).append(' ').append(library.books.size()).append('\n');
            for (Book book : library.books) {
                out.append(book.id).append(' ');
            }
            out.println();
        }

        try (OutputStream os = new FileOutputStream("C:\\Users\\daltao\\IdeaProjects\\coophashcode\\src\\main\\resources\\qualification\\b.out")) {
            os.write(out.toString().getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    long curBest;
    List<SolutionLibrary> libraryOrder = new ArrayList<>();

    public void optimize(Library[] libraries, Book[] books) {
        List<LongCostFlowEdge>[] g = LongFlow.createCostFlow(sink() + 1);
        for (Library library : libraries) {
            for (Book book : library.books) {
                LongFlow.addEdge(g, library(library), book(book), 1, 0);
            }
        }
        int timeRemain = d;
        for (int i = 0; i < l; i++) {
            timeRemain -= libraries[i].daySignUp;
            if (timeRemain <= 0) {
                break;
            }
            long canSent = timeRemain * libraries[i].perDay;
            LongFlow.addEdge(g, src(), library(libraries[i]), canSent, 0);
        }
        for (int i = 0; i < b; i++) {
            LongFlow.addEdge(g, book(books[i]), sink(), 1, -books[i].score);
        }
        LongMinimumCostFlow mcmf = new MinCostFlowPolynomial();
        long[] ans = mcmf.apply(g, src(), sink(), (long)2e18);
        long score = -ans[1];
        if (score <= curBest) {
            return;
        }
        curBest = score;
        libraryOrder.clear();
        for (int i = 0; i < l; i++) {
            SolutionLibrary sol = new SolutionLibrary();
            sol.library = libraries[i];
            for (LongCostFlowEdge e : g[library(libraries[i])]) {
                if (!e.real || e.flow == 0) {
                    continue;
                }
                sol.books.add(books[e.to - l]);
            }
            libraryOrder.add(sol);
        }

        libraryOrder = libraryOrder.stream().filter(x -> !x.books.isEmpty()).collect(Collectors.toList());
        System.err.println("optimize to " + curBest);
    }
}

class SolutionLibrary {
    Library library;
    List<Book> books = new ArrayList<>();
}

class Book {
    int id;
    long score;
}

class Library {
    List<Book> books;
    long daySignUp;
    long perDay;
    int id;
}