package university.model;

public class Section {
    
    private int sectionID;
    private int courseID;
    private String dayTime;
    private String term;

    public Section() {}

    public Section(int courseID, String dayTime, String term){
        //this.sectionID = sectionID;
        this.courseID = courseID;
        this.dayTime = dayTime;
        this.term = term;
    }

    // public int getSectionID() {
    //     return sectionID;
    // }

    // public void setSectionID(int sectionID) {
    //     this.sectionID = sectionID;
    // }

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
    
}
