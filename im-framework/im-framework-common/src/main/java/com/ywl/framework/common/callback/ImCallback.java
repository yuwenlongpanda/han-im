package com.ywl.framework.common.callback;

public interface ImCallback {

    void onSuccess();

    default void onFailure(String errorMessage) {
        // 默认的失败处理逻辑
        System.err.println("Operation failed: " + errorMessage);
    }

}
