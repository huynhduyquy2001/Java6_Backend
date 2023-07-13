package com.viesonet.entity;
import java.security.Principal;

public class CustomPrincipal implements Principal {
    private String uniqueRoomId;

    public CustomPrincipal(String uniqueRoomId) {
        this.uniqueRoomId = uniqueRoomId;
    }

    @Override
    public String getName() {
        return uniqueRoomId;
    }

    public String getUniqueRoomId() {
        return uniqueRoomId;
    }

    public void setUniqueRoomId(String uniqueRoomId) {
        this.uniqueRoomId = uniqueRoomId;
    }
}
