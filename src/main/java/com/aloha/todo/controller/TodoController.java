package com.aloha.todo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aloha.todo.domain.Pagination;
import com.aloha.todo.domain.Todos;
import com.aloha.todo.service.TodoService;
import com.github.pagehelper.PageInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 할 일 목록 REST API
 *
 * [GET]    /todos?page=1&size=10  목록 (list + pagination)
 * [GET]    /todos/{id}            조회
 * [POST]   /todos                 등록
 * [PUT]    /todos                 수정 ({ "id": null } 이면 전체 완료)
 * [DELETE] /todos/{id}            삭제
 * [DELETE] /todos                 전체 삭제
 */
@Slf4j
@CrossOrigin("*")   // React 개발 서버 등 다른 출처의 요청 허용
@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    public ResponseEntity<?> getAll(
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        try {
            PageInfo<Todos> pageInfo = todoService.list(page, size);

            Pagination pagination = new Pagination();
            pagination.setPage(pageInfo.getPageNum());
            pagination.setSize(size);
            pagination.setTotal(pageInfo.getTotal());

            Map<String, Object> response = new HashMap<>();
            response.put("list", pageInfo.getList());
            response.put("pagination", pagination);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("할 일 목록 조회 실패", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") String id) {
        try {
            Todos todo = todoService.selectById(id);
            if (todo == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(todo, HttpStatus.OK);
        } catch (Exception e) {
            log.error("할 일 조회 실패 - id: {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Todos todo) {
        try {
            if (todo.getName() == null || todo.getName().isBlank()) {
                return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
            }
            if (todo.getId() == null) {
                todo.setId(new Todos().getId());
            }
            if (todoService.insert(todo)) {
                return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
            }
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("할 일 등록 실패", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Todos todo) {
        try {
            boolean result = (todo.getId() == null)
                ? todoService.completeAll()     // 전체 완료
                : todoService.updateById(todo); // 단일 수정
            if (result) {
                return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
            }
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("할 일 수정 실패", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping({ "", "/{id}" })
    public ResponseEntity<?> delete(@PathVariable(value = "id", required = false) String id) {
        try {
            boolean result = (id == null)
                ? todoService.deleteAll()       // 전체 삭제
                : todoService.deleteById(id);   // 단일 삭제
            if (result) {
                return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
            }
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("할 일 삭제 실패 - id: {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
