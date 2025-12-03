package university.model;


//Section class
public class Section {
    
    private int sectionID;
    private int courseID;
    private String dayTime;
    private String term;
    private String courseName; // For display purposes

    public Section() {}

    public Section(int courseID, String dayTime, String term){
        this.courseID = courseID;
        this.dayTime = dayTime;
        this.term = term;
    }

    public Section(int sectionID, int courseID, String dayTime, String term){
        this.sectionID = sectionID;
        this.courseID = courseID;
        this.dayTime = dayTime;
        this.term = term;
    }
    
    public int getSectionID() {
        return sectionID;
    }

    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
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

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
