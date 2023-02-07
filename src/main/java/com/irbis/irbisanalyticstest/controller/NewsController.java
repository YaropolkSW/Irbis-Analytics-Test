package com.irbis.irbisanalyticstest.controller;

import com.irbis.irbisanalyticstest.dto.NewsDTO;
import com.irbis.irbisanalyticstest.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@EnableScheduling
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(final NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/")
    public String firstPage() {
        return "index";
    }

    @GetMapping("/sources")
    public String getAllSources(final Model model) {
        model.addAttribute("sources", newsService.getAllSources());

        return "sources";
    }

    @GetMapping("/subjects")
    public String getAllSubjects(final Model model) {
        model.addAttribute("subjects", newsService.getAllSubjects());

        return "subjects";
    }

    @GetMapping("/all-news")
    public String getAllNews(
        final Model model,
        @RequestParam(value = "page", defaultValue = "1") final int page,
        @RequestParam(value = "size", defaultValue = "5") final int size
    ) {
        final Page<NewsDTO> newsPage = newsService.getAllNews(PageRequest.of(page - 1, size));

        newsService.addPagination(newsPage, model);

        return "all-news";
    }

    @GetMapping("/select-source")
    public String selectSource(final Model model) {
        model.addAttribute("sources", newsService.getAllSources());

        return "select-source";
    }

    @GetMapping("/news-by-source")
    public String getNewsBySource(
        @RequestParam("source") final String source,
        @RequestParam(value = "page", defaultValue = "1") final int page,
        @RequestParam(value = "size", defaultValue = "5") final int size,
        final Model model
    ) {
        final Page<NewsDTO> newsPage = newsService.getNewsBySource(PageRequest.of(page - 1, size), source);

        model.addAttribute("source", source);
        newsService.addPagination(newsPage, model);

        return "news-by-source";
    }

    @GetMapping("/select-subject")
    public String selectSubject(final Model model) {
        model.addAttribute("subjects", newsService.getAllSubjects());

        return "select-subject";
    }

    @GetMapping("/news-by-subject")
    public String getNewsBySubject(
        @RequestParam("subject") final String subject,
        @RequestParam(value = "page", defaultValue = "1") final int page,
        @RequestParam(value = "size", defaultValue = "5") final int size,
        final Model model
    ) {
        final Page<NewsDTO> newsPage = newsService.getNewsBySubject(PageRequest.of(page - 1, size), subject);

        model.addAttribute("subject", subject);
        newsService.addPagination(newsPage, model);

        return "news-by-subject";
    }
}
