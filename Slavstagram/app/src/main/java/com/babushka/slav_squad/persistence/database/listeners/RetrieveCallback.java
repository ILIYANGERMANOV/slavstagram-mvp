package com.babushka.slav_squad.persistence.database.listeners;

import android.support.annotation.NonNull;

/**
 * Created by iliyan on 10.12.17.
 */

public interface RetrieveCallback<T> {
    void onRetrieved(@NonNull T data);

    void onError();
}
