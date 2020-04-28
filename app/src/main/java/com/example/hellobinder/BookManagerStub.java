package com.example.hellobinder;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import java.util.List;

public abstract class BookManagerStub extends Binder implements BookManager {
    public static final String desc = "com.example.hellobinder.bookmanager";
    public final static int transaction_addBook = IBinder.FIRST_CALL_TRANSACTION;
    public final static int transaction_getBooks = IBinder.FIRST_CALL_TRANSACTION + 1;

    public BookManagerStub(){
        // 重要，我第一次写就缺了这个。
        // binder 千千万，每个binder各有不同
        // 那么本binder是干嘛的呢？
        attachInterface(this, desc);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    public BookManager asInterface(IBinder binder) {
       if (binder == null) return null;

        // Android OS inject 进来一个Binder。是不是自己啊？因为BookManagerStub就是binder啊。
        // 在构造函数的时候，把desc传入并保存了。
        //queryLocalInterface 的本质就是匹配一下desc啊。
        // 如果匹配，那就是自己啊，直接返回自己即可啊。
        // 否则，这个binder就是一个proxy，真正的binder在远程的进程里面，因此我就得创建proxy啊。
        IInterface iit = queryLocalInterface(desc);
        if(iit != null) {
            return (BookManager) iit;
        } else {
          return new BookManagerProxy(this);
        }
    }

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply,
                                 int flags) throws RemoteException {
        switch (code) {
            // 这里我第一次写忘记了。
            // 父类binder里面也有类似实现，如果忘了了，看看。
            case IBinder.INTERFACE_TRANSACTION:
                reply.writeString(desc);
                return true;

            case transaction_addBook:
                // 检查是需要的interface，我第一次写就忘了。
                // proxy里面，对应的，会写
                // data.writeInterfaceToken(desc)
                data.enforceInterface(desc);

                // 这里在proxy里面，对应的，如果book不是null的时候，写入 int 1
                // 如果book是null的话， 写入 int 0
                // 这里可以认为是business logic了。
                Book book = null;
                if (data.readInt() != 0) {
                    // 这里的data不需要在Stub里面反序列话，因为如何反序列号只有具体的Book才知道，
                    // 因此，这里应该就是调用其creator，把 *整个data* 传入进去。
                    book = Book.CREATOR.createFromParcel(data);
                }
                this.addBook(book);
                reply.writeNoException();

                return true;

            case transaction_getBooks:
                // 检查是需要的interface，我第一次写就忘了。
                // proxy里面，对应的，会写
                //  data.writeInterfaceToken(desc);
                data.enforceInterface(desc);

                List<Book> bookList = this.getBooks();
                // 先写 no exception, 然后是正常返回
                reply.writeNoException();
                // 有意思的是，对面proxy不是read，而是用createTypedList
                reply.writeTypedList(bookList);

                // 这里要返回， 我第一次写就没有返回
                return true;
        }
        return super.onTransact(code, data, reply, flags);
    }
}
