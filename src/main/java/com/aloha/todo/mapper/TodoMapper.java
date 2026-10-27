package com.aloha.todo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aloha.todo.domain.Todos;

@Mapper
public interface TodoMapper {

    // 목록
    List<Todos> list();

    // 조회
    Todos select(Long no);
    Todos selectById(String id);

    // 등록
    int insert(Todos todo);

    // 수정
    int update(Todos todo);
    int updateById(Todos todo);

    // 삭제
    int delete(Long no);
    int deleteById(String id);

    // 전체 완료
    int completeAll();

    // 전체 삭제
    int deleteAll();
}
