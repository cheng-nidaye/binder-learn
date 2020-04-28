package com.example.hellobinder;

import android.os.IInterface;
import android.os.RemoteException;

import java.util.List;

public interface BookManager extends IInterface {
    void addBook(Book book) throws RemoteException;
    List<Book> getBooks() throws RemoteException;
}
