package ua.com.todo.service;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommonService<T, I> {
  void save(T object);

  List<T> update(T object, I id);

  void delete(Long id);

  List<T> getAll();

  T getById(I id);
}
