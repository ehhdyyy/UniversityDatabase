package university.model;

public class Enrollment {
    
    private int enrollmentID;
    private int studentID;
    private int sectionID;

    public Enrollment() {}

    public Enrollment(int studentID, int sectionID){
        //this.enrollmentID = enrollmentID;
        this.studentID = studentID;
        this.sectionID = sectionID;
    }

    // public int getEnrollmentID() {
    //     return enrollmentID;
    // }

    // public void setEnrollmentID(int enrollmentID) {
    //     this.enrollmentID = enrollmentID;
    // }

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
    
}
