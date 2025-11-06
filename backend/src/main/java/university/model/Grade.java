package university.model;

public class Grade {
    
    private int gradeID;
    private int enrollmentID;
    private String grade;

    public Grade() {}

    public Grade(int enrollmentID, String grade){
        //this.gradeID = gradeID;
        this.enrollmentID = enrollmentID;
        this.grade = grade;
    }

    // public int getGradeID() {
    //     return gradeID;
    // }

    // public void setGradeID(int gradeID) {
    //     this.gradeID = gradeID;
    // }

    public int getEnrollmentID() {
        return enrollmentID;
    }

    public void setEnrollmentID(int enrollmentID) {
        this.enrollmentID = enrollmentID;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
    
}
