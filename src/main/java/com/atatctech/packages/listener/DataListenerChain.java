package com.atatctech.packages.listener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class DataListenerChain<T> implements DataListener<T> {
    private @Nullable DataListenerChain<T> chain;

    public final void addChain(@NotNull DataListenerChain<T> chain) {
        if (this.chain == null) this.chain = chain;
        else this.chain.addChain(chain);
    }

    public final @Nullable DataListenerChain<T> skip() {
        return chain;
    }

    protected void onChangeImpl(@NotNull T from, @NotNull T to) {
    }

    @Override
    public final void onChange(@NotNull T from, @NotNull T to) {
        if (chain != null) chain.onChange(from, to);
        onChangeImpl(from, to);
    }

    protected abstract void postChangeImpl(@NotNull T from, @NotNull T to);

    @Override
    public final void postChange(@NotNull T from, @NotNull T to) {
        if (chain != null) chain.postChange(from, to);
        postChangeImpl(from, to);
    }

    protected void onDeleteImpl(@NotNull T target) {
    }

    @Override
    public final void onDelete(@NotNull T target) {
        if (chain != null) chain.onDelete(target);
        onDeleteImpl(target);
    }

    protected abstract void postDeleteImpl(@NotNull T target);

    @Override
    public final void postDelete(@NotNull T target) {
        if (chain != null) chain.postDelete(target);
        postDeleteImpl(target);
    }
}

