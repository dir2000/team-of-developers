package com.ra.janus.developersteam.dao;

import java.util.List;

public interface BaseDAO<T> {

    T get(long id);

    List<T> getAll();

    T create(T t);

    boolean update(T t);

    boolean delete(long id);
}
