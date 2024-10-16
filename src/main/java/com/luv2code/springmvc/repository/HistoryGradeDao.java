package com.luv2code.springmvc.repository;

import com.luv2code.springmvc.models.HistoryGrade;
import org.springframework.data.repository.CrudRepository;

public interface HistoryGradeDao extends CrudRepository<HistoryGrade,Integer> {
    public Iterable<HistoryGrade> findGradeByStudentId(int id);
    public void deleteByStudentId(int id);
}
