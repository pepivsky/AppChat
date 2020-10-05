package com.pepivsky.chat.pubnub.utils;

import com.pepivsky.chat.model.User;

public interface UserPresenceCallback {

    void userJoin(User user);
    void userLeave(User user);

}
