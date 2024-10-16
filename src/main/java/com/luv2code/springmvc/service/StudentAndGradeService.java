package com.luv2code.springmvc.service;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradeDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.repository.HistoryGradeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentAndGradeService {

    @Autowired
    StudentDao studentDao;

    @Autowired
    MathGrade mathGrade;

    @Autowired
    MathGradesDao mathGradeDao;

    @Autowired
    ScienceGrade scienceGrade;

    @Autowired
    ScienceGradeDao scienceGradeDao;

    @Autowired
    HistoryGrade historyGrade;

    @Autowired
    HistoryGradeDao historyGradeDao;

    @Autowired
    StudentGrades studentGrades;

    public void createStudent(String firstName, String lastName, String emailAddress){
        CollegeStudent student = new CollegeStudent(firstName,lastName,emailAddress);
        student.setId(0);
        studentDao.save(student);
    }

    public boolean checkIfStudentIsnull(int id){
        Optional<CollegeStudent> student = studentDao.findById(id);
        return student.isPresent();
    }

    public void deleteStudent(int id){
        if(checkIfStudentIsnull(id)){
            studentDao.deleteById(id);
            mathGradeDao.deleteByStudentId(id);
            scienceGradeDao.deleteByStudentId(id);
            historyGradeDao.deleteByStudentId(id);
        }
    }

    public Iterable<CollegeStudent> getGradebook(){
        Iterable<CollegeStudent> students = studentDao.findAll();
        return students;
    }

    public boolean createGrade(double grade , int studentId , String gradeType){
        if(!checkIfStudentIsnull(studentId)){
            return false;
        }
        if(grade >= 0 && grade <= 100){
            Optional<CollegeStudent> student = studentDao.findById(studentId);
            if(gradeType.equals("math")) {
                mathGrade.setId(0);
                mathGrade.setGrade(grade);
                mathGrade.setStudentId(studentId);
                mathGradeDao.save(mathGrade);
                return true;
            }
            if(gradeType.equals("science")) {
                scienceGrade.setId(0);
                scienceGrade.setGrade(grade);
                scienceGrade.setStudentId(studentId);
                scienceGradeDao.save(scienceGrade);
                return true;
            }
            if(gradeType.equals("history")) {
                historyGrade.setId(0);
                historyGrade.setGrade(grade);
                historyGrade.setStudentId(studentId);
                historyGradeDao.save(historyGrade);
                return true;
            }
        }
        return false;
    }

    public int deleteGrade(int gradeId, String gradeType){
        int studentId = 0;
        if(gradeType.equals("math")){
            Optional<MathGrade> grade = mathGradeDao.findById(gradeId);
            if(!grade.isPresent()){
                return studentId;
            }
            studentId = grade.get().getStudentId();
            mathGradeDao.deleteById(gradeId);
        }
        if(gradeType.equals("science")){
            Optional<ScienceGrade> grade = scienceGradeDao.findById(gradeId);
            if(!grade.isPresent()){
                return studentId;
            }
            studentId = grade.get().getStudentId();
            scienceGradeDao.deleteById(gradeId);
        }
        if(gradeType.equals("history")){
            Optional<HistoryGrade> grade = historyGradeDao.findById(gradeId);
            if(!grade.isPresent()){
                return studentId;
            }
            studentId = grade.get().getStudentId();
            historyGradeDao.deleteById(gradeId);
        }
        return studentId;
    }

    public GradebookCollegeStudent studentInformation(int id){
        if(checkIfStudentIsnull(id)){
            Optional<CollegeStudent> student = studentDao.findById(id);
            Iterable<MathGrade> mathGrades = mathGradeDao.findGradeByStudentId(id);
            Iterable<ScienceGrade> scienceGrades = scienceGradeDao.findGradeByStudentId(id);
            Iterable<HistoryGrade> historyGrades = historyGradeDao.findGradeByStudentId(id);

            List<Grade> mathGradeList = new ArrayList<>();
            mathGrades.forEach(mathGradeList::add);
            List<Grade> scienceGradeList = new ArrayList<>();
            scienceGrades.forEach(scienceGradeList::add);
            List<Grade> historyGradeList = new ArrayList<>();
            historyGrades.forEach(historyGradeList::add);
            studentGrades.setHistoryGradeResults(historyGradeList);
            studentGrades.setMathGradeResults(mathGradeList);
            studentGrades.setScienceGradeResults(scienceGradeList);

            GradebookCollegeStudent gradebookCollegeStudent = new GradebookCollegeStudent(student.get().getId(), student.get().getFirstname(), student.get().getLastname(), student.get().getEmailAddress(), studentGrades);
            return gradebookCollegeStudent;

        }
        return null;
    }

    public void configureStudentInformationModel(int id, Model m){
        GradebookCollegeStudent studentEntity = studentInformation(id);
        m.addAttribute("student", studentEntity);
        if(studentEntity.getStudentGrades().getMathGradeResults() != null) {
            m.addAttribute("mathAverage", studentEntity.getStudentGrades().
                    findGradePointAverage(studentEntity.getStudentGrades().getMathGradeResults()));
        } else {
            m.addAttribute("mathAverage", "N/A");
        }
        if (studentEntity.getStudentGrades().getScienceGradeResults() != null) {
            m.addAttribute("scienceAverage", studentEntity.getStudentGrades().
                    findGradePointAverage(studentEntity.getStudentGrades().getScienceGradeResults()));
        } else {
            m.addAttribute("scienceAverage", "N/A");
        }
        if (studentEntity.getStudentGrades().getHistoryGradeResults() != null) {
            m.addAttribute("historyAverage", studentEntity.getStudentGrades().
                    findGradePointAverage(studentEntity.getStudentGrades().getHistoryGradeResults()));
        } else {
            m.addAttribute("historyAverage", "N/A");
        }
    }
}
