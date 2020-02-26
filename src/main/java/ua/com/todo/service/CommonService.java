package ua.com.todo.service;

import org.springframework.stereotype.Service;

import java.util.List;

/*
* Basic Data Access Object interface.
* Provides CRUD operations with {@link Persistence} objects.
*
* @author Valera
* */

@Service
public interface CommonService<T, I> {

  /**
   * Save  the persistent object.
   * @param object object to save
   * @return saved object
   */
  T save(T object);

  /**
   * Update the persistent object.
   * @param object and id object to update
   * @return updated object
   */
  T update(T object, I id);

  /**
   * Delete the object by it's id.
   * @param id the id
   */
  void delete(Long id);

  /**
   * Get the list of objects.
   * @return list of objects
   */
  List<T> getAll();

  /**
   * Get the object by id.
   * @param id
   * @return task or null
   */
  T getById(I id);
}
