package com.ywl.im.common.config;

public interface ImProperties {
    int getPort();
    int getBossNthreads();
    int getWorkerNthreads();
    int getClientReaderIdleTimeSeconds();
    int getMaxContentLength();
    int getSoBackLog();
    String getWebSocketPrefix();
}
