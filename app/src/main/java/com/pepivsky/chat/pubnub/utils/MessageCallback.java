package com.pepivsky.chat.pubnub.utils;

import com.pepivsky.chat.model.Message;

public interface MessageCallback {
    void received (Message message);
}
