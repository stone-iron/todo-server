package com.aloha.todo;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * 할 일 API 통합 테스트 (H2 메모리 DB, 샘플 12건)
 * 각 테스트는 끝나면 롤백되어 서로 영향을 주지 않는다.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TodoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 목록_조회_페이징() throws Exception {
        mockMvc.perform(get("/todos").param("page", "1").param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.list", hasSize(10)))
            .andExpect(jsonPath("$.pagination.total", is(12)))
            .andExpect(jsonPath("$.pagination.last", is(2)))
            // 페이지 안에서는 미완료가 먼저 온다
            .andExpect(jsonPath("$.list[0].status", is(false)));

        mockMvc.perform(get("/todos").param("page", "2").param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.list", hasSize(2)));
    }

    @Test
    void 단건_조회() throws Exception {
        mockMvc.perform(get("/todos/todo-01"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("할일1")))
            .andExpect(jsonPath("$.createdAt").exists());

        mockMvc.perform(get("/todos/없는-아이디"))
            .andExpect(status().isNotFound());
    }

    @Test
    void 등록() throws Exception {
        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"new-todo\",\"name\":\"새 할일\"}"))
            .andExpect(status().isCreated())
            .andExpect(content().string("SUCCESS"));

        // 순서를 보내지 않으면 마지막 순서 + 1, 상태는 미완료
        mockMvc.perform(get("/todos/new-todo"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.seq", is(13)))
            .andExpect(jsonPath("$.status", is(false)));

        // id를 보내지 않아도 서버에서 생성
        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"아이디 없는 할일\"}"))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/todos"))
            .andExpect(jsonPath("$.pagination.total", is(14)));
    }

    @Test
    void 등록_할일_내용이_없으면_실패() throws Exception {
        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"  \"}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void 단건_수정() throws Exception {
        mockMvc.perform(put("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"todo-02\",\"status\":true,\"name\":\"수정된 할일\"}"))
            .andExpect(status().isOk())
            .andExpect(content().string("SUCCESS"));

        mockMvc.perform(get("/todos/todo-02"))
            .andExpect(jsonPath("$.status", is(true)))
            .andExpect(jsonPath("$.name", is("수정된 할일")));
    }

    @Test
    void 없는_할일_수정은_실패() throws Exception {
        mockMvc.perform(put("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"없는-아이디\",\"status\":true}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void 전체_완료() throws Exception {
        mockMvc.perform(put("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":null}"))
            .andExpect(status().isOk());

        mockMvc.perform(get("/todos").param("size", "20"))
            .andExpect(jsonPath("$.list[*].status", everyItem(is(true))));
    }

    @Test
    void 단건_삭제() throws Exception {
        mockMvc.perform(delete("/todos/todo-03"))
            .andExpect(status().isOk());

        mockMvc.perform(get("/todos/todo-03"))
            .andExpect(status().isNotFound());

        mockMvc.perform(delete("/todos/todo-03"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void API_문서_제공() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.paths['/todos']").exists());
    }

    @Test
    void 전체_삭제() throws Exception {
        mockMvc.perform(delete("/todos"))
            .andExpect(status().isOk());

        mockMvc.perform(get("/todos"))
            .andExpect(jsonPath("$.list", hasSize(0)))
            .andExpect(jsonPath("$.pagination.total", is(0)));
    }
}
