package com.example.hellobinder;

import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;

public class BookManagerImp extends BookManagerStub {
    List<Book> bookList = new ArrayList<>();
    public  BookManagerImp() {
        bookList.add(new Book("woshinidaye", 10));
        bookList.add(new Book("tashinidama", 20));
    }
    @Override
    public void addBook(Book book) throws RemoteException {
        bookList.add(book);
    }

    @Override
    public List<Book> getBooks() throws RemoteException {
        return bookList;
    }
}
