package com.aloha.todo.service;

import java.util.List;

import com.aloha.todo.domain.Todos;
import com.github.pagehelper.PageInfo;

public interface TodoService {

    List<Todos> list();

    // 페이징 목록
    PageInfo<Todos> list(int page, int size);

    Todos select(Long no);
    Todos selectById(String id);

    boolean insert(Todos todo);

    boolean update(Todos todo);
    boolean updateById(Todos todo);

    boolean delete(Long no);
    boolean deleteById(String id);

    // 전체 완료
    boolean completeAll();

    // 전체 삭제
    boolean deleteAll();
}
