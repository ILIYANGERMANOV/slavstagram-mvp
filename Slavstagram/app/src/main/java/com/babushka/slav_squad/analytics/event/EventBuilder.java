package com.babushka.slav_squad.analytics.event;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Iliyan on 14.07.17.
 */
public class EventBuilder {
    private static final int MIN_EVENT_NAME_LENGTH = 2;
    //Firebase limit - 32 characters; Appsee limit - 40
    private static final int MAX_EVENT_NAME_LENGTH = 32;
    //Firebase limit - 36 characters; Appsee limit - 100
    private static final int MAX_PARAM_LENGTH = 36;

    private String mEventName;
    private Map<String, Object> mParams;

    public static Event simpleEvent(@NonNull String eventName) {
        return new EventBuilder().setEventName(eventName)
                .build();
    }

    public EventBuilder setEventName(@NonNull String eventName) {
        mEventName = eventName;
        return this;
    }

    public EventBuilder addParam(@NonNull String key, boolean value) {
        return addParam(key, value ? EventValues.TRUE : EventValues.FALSE);
    }

    public EventBuilder addParam(@NonNull String key, @Nullable String value) {
        if (value == null) return this; //value is null, do nothing
        if (mParams == null) {
            mParams = new HashMap<>();
        }
        mParams.put(key, value);
        return this;
    }

    /**
     * @return
     * @throws InvalidEventNameException
     * @throws InvalidEventParamException
     */
    public Event build() {
        validateEventName();
        validateParams();
        return new Event(mEventName, mParams);
    }

    private void validateEventName() {
        if (mEventName == null || mEventName.length() < MIN_EVENT_NAME_LENGTH ||
                mEventName.length() > MAX_EVENT_NAME_LENGTH) {
            throw new InvalidEventNameException();
        }
    }

    private void validateParams() {
        if (mParams != null) {
            for (Map.Entry<String, Object> param : mParams.entrySet()) {
                validateParam(param);
            }
        }
    }

    private void validateParam(@NonNull Map.Entry<String, Object> param) {
        String key = param.getKey();
        if (key == null || key.length() > MAX_PARAM_LENGTH) {
            throw new InvalidEventParamException();
        }
        Object value = param.getValue();
        if (value instanceof String) {
            if (((String) value).length() > MAX_PARAM_LENGTH) {
                throw new InvalidEventParamException();
            }
        }
    }

    private class InvalidEventNameException extends RuntimeException {
    }

    private class InvalidEventParamException extends RuntimeException {
    }
}
