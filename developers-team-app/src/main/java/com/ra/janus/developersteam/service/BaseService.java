package com.ra.janus.developersteam.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BaseService<T> {
    T create(final T entity);

    List<T> getAll();
}
