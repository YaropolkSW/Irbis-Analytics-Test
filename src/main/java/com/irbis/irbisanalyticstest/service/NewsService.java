package com.irbis.irbisanalyticstest.service;

import com.irbis.irbisanalyticstest.dao.NewsDAO;
import com.irbis.irbisanalyticstest.dto.NewsDTO;
import com.irbis.irbisanalyticstest.dto.ProjectionDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class NewsService {
    private final static String EXCEPTION_MESSAGE = "Something gone wrong!";
    private final static String FILE_EXTENSION = ".csv";
    private final static String SUBJECT = "Тематика";
    private final static String COUNT_OF_NEWS = "Количество новостей";

    private final NewsDAO newsDAO;

    @Autowired
    public NewsService(final NewsDAO newsDAO) {
        this.newsDAO = newsDAO;
    }

    public Set<String> getAllSources() {
        return new HashSet<>(newsDAO.getAllSources());
    }

    public Set<String> getAllSubjects() {
        return new HashSet<>(newsDAO.getAllSubjects());
    }

    public Page<NewsDTO> getAllNews(final Pageable pageable) {
        final List<NewsDTO> news = newsDAO.findAll()
            .stream()
            .map(NewsDTO::of)
            .collect(Collectors.toList());

        return pages(
            news,
            pageable.getPageSize(),
            pageable.getPageNumber(),
            (pageable.getPageSize() * pageable.getPageNumber()));
    }

    public Page<NewsDTO> getNewsBySource(final Pageable pageable, final String source) {
        final List<NewsDTO> newsBySource = newsDAO.findBySource(source)
            .stream()
            .map(NewsDTO::of)
            .collect(Collectors.toList());

        return pages(
            newsBySource,
            pageable.getPageSize(),
            pageable.getPageNumber(),
            (pageable.getPageSize() * pageable.getPageNumber()));
    }

    public Page<NewsDTO> getNewsBySubject(final Pageable pageable, final String subject) {
        final List<NewsDTO> newsBySubject = newsDAO.findBySubject(subject)
            .stream()
            .map(NewsDTO::of)
            .collect(Collectors.toList());

        return pages(
            newsBySubject,
            pageable.getPageSize(),
            pageable.getPageNumber(),
            (pageable.getPageSize() * pageable.getPageNumber()));
    }

    public PageImpl<NewsDTO> pages(
        final List<NewsDTO> news,
        final int pageSize,
        final int currentPage,
        final int startItem
    ) {
        final List<NewsDTO> list;

        if (news.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, news.size());
            list = news.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), news.size());
    }

    @Scheduled(cron = "10 * * * * *")
    @Async
    public void cronDataUpload() {
        final ReentrantLock lock = new ReentrantLock();
        final Set<String> sources = getAllSources();

        for (final String source : sources) {
            lock.lock();
            try (
                final BufferedWriter writer = Files.newBufferedWriter(Paths.get(source + FILE_EXTENSION));
                final CSVPrinter csvPrinter =
                    new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(SUBJECT, COUNT_OF_NEWS))
            ) {
                final List<ProjectionDTO> news =
                    newsDAO.findDistinctSubjectAndCountNewsWhereSourceLike(source);

                for (final ProjectionDTO projection : news) {
                    csvPrinter.printRecord(projection.getSubject(), projection.getCount());
                    csvPrinter.flush();
                }
            } catch (IOException e) {
                System.out.println(EXCEPTION_MESSAGE);
            }
            lock.unlock();
        }
    }

    public void addPagination(final Page<NewsDTO> newsPage, final Model model) {
        model.addAttribute("newsPage", newsPage);

        final int totalPages = newsPage.getTotalPages();

        if (totalPages > 0) {
            final List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());

            model.addAttribute("pageNumbers", pageNumbers);
        }
    }
}
