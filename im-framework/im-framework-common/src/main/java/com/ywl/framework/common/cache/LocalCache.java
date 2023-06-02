package com.ywl.framework.common.cache;

import org.springframework.context.SmartLifecycle;

/**
 * 本地缓存
 *
 * @author luozhan
 * @date 2022-01
 */

public interface LocalCache extends SmartLifecycle {

    /**
     * 初始化缓存
     */
    void initCache();

    @Override
    default void start() {
        initCache();
    }

    @Override
    default void stop() {

    }

    @Override
    default boolean isRunning() {
        return false;
    }
}
