package com.babushka.slav_squad.persistence.database.listeners;

import android.support.annotation.NonNull;

/**
 * Created by iliyan on 28.05.17.
 */

public interface DatabaseListener<T> extends DatabaseErrorCallback {
    void onAdded(@NonNull T data);

    void onChanged(@NonNull T data);

    void onRemoved(@NonNull T data);
}
