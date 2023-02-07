package com.irbis.irbisanalyticstest.rest;

import com.irbis.irbisanalyticstest.dto.NewsDTO;
import com.irbis.irbisanalyticstest.response.GetAllSourcesResponse;
import com.irbis.irbisanalyticstest.response.GetAllSubjectsResponse;
import com.irbis.irbisanalyticstest.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class NewsRestController {
    private final NewsService newsService;

    @Autowired
    public NewsRestController(final NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/sources")
    public List<GetAllSourcesResponse> getAllSources() {
        final List<GetAllSourcesResponse> sourcesResponse = new ArrayList<>();
        final Set<String> sources = newsService.getAllSources();

        for (final String source : sources) {
            sourcesResponse.add(new GetAllSourcesResponse(source));
        }

        return sourcesResponse;
    }

    @GetMapping("/subjects")
    public List<GetAllSubjectsResponse> getAllSubjects() {
        final List<GetAllSubjectsResponse> subjectsResponse = new ArrayList<>();
        final Set<String> subjects = newsService.getAllSubjects();

        for (final String subject : subjects) {
            subjectsResponse.add(new GetAllSubjectsResponse(subject));
        }

        return subjectsResponse;
    }

    @GetMapping("/all-news/page={page}/size={size}")
    public List<NewsDTO> getAllNews(
        @PathVariable("page") final int page,
        @PathVariable("size") final int size
    ) {
        return newsService.getAllNews(PageRequest.of(page - 1, size)).getContent();
    }

    @GetMapping("/source={source}/page={page}/size={size}")
    public List<NewsDTO> getNewsBySource(
        @PathVariable("source") final String source,
        @PathVariable("page") final int page,
        @PathVariable("size") final int size
    ) {
        return newsService.getNewsBySource(PageRequest.of(page - 1, size), source).getContent();
    }

    @GetMapping("/subject={subject}/page={page}/size={size}")
    public List<NewsDTO> getNewsBySubject(
        @PathVariable("subject") final String subject,
        @PathVariable("page") final int page,
        @PathVariable("size") final int size
    ) {
        return newsService.getNewsBySubject(PageRequest.of(page - 1, size), subject).getContent();
    }
}
