package com.example.hellobinder;

import static com.example.hellobinder.BookManagerStub.desc;
import static com.example.hellobinder.BookManagerStub.transaction_getBooks;
import static com.example.hellobinder.BookManagerStub.transaction_addBook;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;

//import static com.example.hellobinder.BookManagerStub.transaction_addBook;

public class BookManagerProxy implements BookManager {
    private final IBinder remote;
    public BookManagerProxy(IBinder remote) {
       this.remote = remote;
    }

    @Override
    public void addBook(Book book) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();

        // 第一次写的时候，忘了try catch 了
        try {
            // 第一次写的时候，忘了 writeInterfaceToken了
            data.writeInterfaceToken(desc);

            if (book != null) {
                data.writeInt(1);
                book.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            remote.transact(transaction_addBook, data, reply, 0);

            // 第一次写的时候，忘了。
            // Stub里面，会 writeNoException，或者写某个exception
            // 这里需要read那个exception
            reply.readException();
        }
        finally {
            // 第一次写的时候就忘了。
            data.recycle();
            reply.recycle();
        }
    }

    @Override
    public List<Book> getBooks() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();

        List<Book> result;
        try {
            data.writeInterfaceToken(desc);
            remote.transact(transaction_getBooks, data, reply, 0);

            reply.readException();

            // 注意这里没有用read，而是createTypedArrayList
            result = reply.createTypedArrayList(Book.CREATOR);
            return result;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @Override
    public IBinder asBinder() {
        return remote;
    }
}
