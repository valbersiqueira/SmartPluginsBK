package com.smartplugins.smartplugins.utils;

import java.util.Objects;

public  class AbstraticId<T> {


    protected T id;


    public T getId() {
        return id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstraticId)) return false;
        AbstraticId<?> that = (AbstraticId<?>) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }
}
