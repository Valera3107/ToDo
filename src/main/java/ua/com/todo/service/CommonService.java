package ua.com.todo.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommonService<T, I> {
  T save(T object);

  T update(T object, I id);

  void delete(Long id);

  List<T> getAll();

  T getById(I id);
}
