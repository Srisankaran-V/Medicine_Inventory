package com.inventory.medicine.mapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @param <E> Entity type
 * @param <R> Response DTO type
 */
public interface BaseMapper<E, R> {

    R toDTO(E entity);

    default List<R> toDTOList(List<E> entities) {
        if (entities == null) return null;
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}