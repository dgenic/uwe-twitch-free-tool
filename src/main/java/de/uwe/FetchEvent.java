package de.uwe;

import java.util.List;

@FunctionalInterface
public interface FetchEvent<T> {

    boolean onFetch(List<T> list, double value, double total);
}
