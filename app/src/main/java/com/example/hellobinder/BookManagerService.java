package com.example.hellobinder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BookManagerService extends Service {
    private final BookManagerImp bookManagerImp;
    public BookManagerService() {
       bookManagerImp = new BookManagerImp();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return bookManagerImp;
    }
}
