package university.model;

public class Enrollment {
    
    private int enrollmentID;
    private int studentID;
    private int sectionID;
    
    // Display fields
    private String studentName;
    private String courseName;
    private String dayTime;
    private String term;

    public Enrollment() {}

    public Enrollment(int studentID, int sectionID){
        this.studentID = studentID;
        this.sectionID = sectionID;
    }

    public Enrollment(int enrollmentID, int studentID, int sectionID){
        this.enrollmentID = enrollmentID;
        this.studentID = studentID;
        this.sectionID = sectionID;
    }

    public int getEnrollmentID() {
        return enrollmentID;
    }

    public void setEnrollmentID(int enrollmentID) {
        this.enrollmentID = enrollmentID;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public int getSectionID() {
        return sectionID;
    }

    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDayTime() {
        return dayTime;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
