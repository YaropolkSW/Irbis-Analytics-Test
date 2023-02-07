package com.irbis.irbisanalyticstest;

import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource("/application-test.properties")
@Sql(value = {"/schema-test-start.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/schema-test-end.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@NoArgsConstructor
public class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void firstPageShouldReturnStatus200AndExactView() throws Exception {
        this.mockMvc
            .perform(get("/"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("index"));
    }

    @Test
    public void getAllSourcesShouldReturnStatus200AndExactView() throws Exception {
        this.mockMvc
            .perform(get("/sources"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("sources"));
    }

    @Test
    public void getAllSubjectsShouldReturnStatus200AndExactView() throws Exception {
        this.mockMvc
            .perform(get("/subjects"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("subjects"));
    }

    @Test
    public void getAllNewsShouldReturnStatus200AndExactView() throws Exception {
        this.mockMvc
            .perform(get("/all-news"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("all-news"));
    }

    @Test
    public void getNewsBySourceShouldReturnStatus200AndExactView() throws Exception {
        this.mockMvc
            .perform(get("/news-by-source").param("source", "praktika.irbis.plus"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("news-by-source"));
    }

    @Test
    public void getNewsBySubjectShouldReturnStatus200AndExactView() throws Exception {
        this.mockMvc
            .perform(get("/news-by-subject").param("subject", "Помощь физ. лицам"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("news-by-subject"));
    }
}
