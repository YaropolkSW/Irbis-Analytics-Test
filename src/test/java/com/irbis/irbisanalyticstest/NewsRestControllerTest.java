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

import java.nio.file.Files;
import java.nio.file.Paths;

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
public class NewsRestControllerTest {
    private final static String EMPTY_LINE = "";
    private final static String PATH_TO_ALL_SOURCES_JSON = "src/test/resources/all-sources-test.json";
    private final static String PATH_TO_ALL_SUBJECTS_JSON = "src/test/resources/all-subjects-test.json";
    private final static String PATH_TO_ALL_NEWS_JSON = "src/test/resources/all-news-test.json";
    private final static String PATH_TO_NEWS_BY_SOURCE_JSON = "src/test/resources/news-by-source.json";
    private final static String PATH_TO_NEWS_BY_SUBJECT_JSON = "src/test/resources/news-by-subject.json";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllSourcesShouldReturnStatus200AndCorrectJSON() throws Exception {
        final String json = String.join(EMPTY_LINE, Files.readAllLines(Paths.get(PATH_TO_ALL_SOURCES_JSON)));

        this.mockMvc
            .perform(get("/api/sources"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(json));
    }

    @Test
    public void getAllSubjectsShouldReturnStatus200AndCorrectJSON() throws Exception {
        final String json = String.join(EMPTY_LINE, Files.readAllLines(Paths.get(PATH_TO_ALL_SUBJECTS_JSON)));

        this.mockMvc
            .perform(get("/api/subjects"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(json));
    }

    @Test
    public void getAllNewsShouldReturnStatus200AndCorrectJSON() throws Exception {
        final String json = String.join(EMPTY_LINE, Files.readAllLines(Paths.get(PATH_TO_ALL_NEWS_JSON)));

        this.mockMvc
            .perform(get("/api/all-news/page=1/size=10"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(json));
    }

    @Test
    public void getNewsBySourceShouldReturnStatus200AndCorrectJSON() throws Exception {
        final String json = String.join(EMPTY_LINE, Files.readAllLines(Paths.get(PATH_TO_NEWS_BY_SOURCE_JSON)));

        this.mockMvc
            .perform(get("/api/source=praktika.irbis.plus/page=1/size=10"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(json));
    }

    @Test
    public void getNewsBySubjectShouldReturnStatus200AndCorrectJSON() throws Exception {
        final String json = String.join(EMPTY_LINE, Files.readAllLines(Paths.get(PATH_TO_NEWS_BY_SUBJECT_JSON)));

        this.mockMvc
            .perform(get("/api/subject=Помощь физ. лицам/page=1/size=10"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(json));
    }
}
