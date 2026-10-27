package com.aloha.todo.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.aloha.todo.domain.Todos;
import com.aloha.todo.mapper.TodoMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoMapper todoMapper;

    @Override
    public List<Todos> list() {
        return todoMapper.list();
    }

    @Override
    public PageInfo<Todos> list(int page, int size) {
        // PageHelper.startPage(현재 페이지, 페이지당 개수) 바로 다음 쿼리에 페이징이 적용된다.
        PageHelper.startPage(page, size);
        PageInfo<Todos> pageInfo = new PageInfo<>(todoMapper.list());

        // 페이지 안에서 정렬: 미완료(false) → 완료(true), 같은 상태면 순서(seq) 오름차순
        pageInfo.getList().sort(
            Comparator.comparing(Todos::getStatus).thenComparing(Todos::getSeq)
        );
        return pageInfo;
    }

    @Override
    public Todos select(Long no) {
        return todoMapper.select(no);
    }

    @Override
    public Todos selectById(String id) {
        return todoMapper.selectById(id);
    }

    @Override
    public boolean insert(Todos todo) {
        return todoMapper.insert(todo) > 0;
    }

    @Override
    public boolean update(Todos todo) {
        return todoMapper.update(todo) > 0;
    }

    @Override
    public boolean updateById(Todos todo) {
        return todoMapper.updateById(todo) > 0;
    }

    @Override
    public boolean delete(Long no) {
        return todoMapper.delete(no) > 0;
    }

    @Override
    public boolean deleteById(String id) {
        return todoMapper.deleteById(id) > 0;
    }

    // 이미 모두 완료된 상태여도 요청 자체는 성공으로 본다.
    @Override
    public boolean completeAll() {
        todoMapper.completeAll();
        return true;
    }

    // 삭제할 데이터가 없어도 요청 자체는 성공으로 본다.
    @Override
    public boolean deleteAll() {
        todoMapper.deleteAll();
        return true;
    }
}
