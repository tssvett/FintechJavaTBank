package org.example.task10.service.crud;

import java.util.List;

public interface CrudService<I, E, C, R, U> {

    /**
     * @param createDto Дто для создания события
     * @return id созданного события
     */
    I create(C createDto);

    /**
     * @param id id события, которое необходимо прочитать
     * @return сущность события
     */
    E read(I id);

    /**
     * @param searchCriteria критерии поиска для событий
     * @return список событий
     */
    List<E> readAll(R searchCriteria);

    /**
     * @param updateDto дто для обновления события
     * @return обновленное событие
     */
    E update(U updateDto);

    /**
     * @param id id события, которое необходимо удалить
     */
    void delete(I id);
}
