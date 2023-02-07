package com.irbis.irbisanalyticstest.dao;

import com.irbis.irbisanalyticstest.dto.ProjectionDTO;
import com.irbis.irbisanalyticstest.entity.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NewsDAO extends JpaRepository<NewsEntity, Integer> {
    @Query("SELECT source FROM NewsEntity")
    List<String> getAllSources();

    @Query("SELECT subject FROM NewsEntity")
    List<String> getAllSubjects();

    List<NewsEntity> findBySource(@Param("source") final String source);

    List<NewsEntity> findBySubject(@Param("subject") final String subject);

    @Query("SELECT new com.irbis.irbisanalyticstest.dto.ProjectionDTO(n.subject, COUNT(n.news)) " +
           "FROM NewsEntity n " +
           "WHERE n.source=:source " +
           "GROUP BY n.subject")
    List<ProjectionDTO> findDistinctSubjectAndCountNewsWhereSourceLike(@Param("source") final String source);
}
