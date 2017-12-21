package com.babushka.slav_squad.persistence.database.listeners;

/**
 * Created by iliyan on 21.12.17.
 */

public interface ValueListener<T> extends DatabaseErrorCallback {
    void onChanged(T value);
}
