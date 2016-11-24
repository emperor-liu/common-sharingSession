package com.lljqiu.session;

import com.lljqiu.session.utils.UUID;

public class DefaultSessionIdMaker extends UUID implements SessionIDMaker {

    public String makeNewId() {
        return super.nextID();
    }

}
