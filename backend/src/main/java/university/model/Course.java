package university.model;

public class Course {
    
    private int courseID;
    private String courseName;
    private String description;

    public Course() {}

    public Course(String courseName, String description){
        this.courseName = courseName;
        this.description = description;
    }

   public Course(int courseID, String courseName, String description){
        this.courseID = courseID;
        this.courseName = courseName;
        this.description = description;
   }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
}

