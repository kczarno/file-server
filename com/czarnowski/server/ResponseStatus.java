package com.czarnowski.server;

enum ResponseStatus {
    OK(200),
    FORBIDDEN(403),
    NOT_FOUND(404);

    final int code;

    ResponseStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
