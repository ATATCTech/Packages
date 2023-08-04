package com.atatctech.packages.listener;

import org.jetbrains.annotations.NotNull;

public interface DataListener<T> {
    void onChange(@NotNull T from, @NotNull T to);

    void postChange(@NotNull T from, @NotNull T to);

    void onDelete(@NotNull T target);

    void postDelete(@NotNull T target);
}
