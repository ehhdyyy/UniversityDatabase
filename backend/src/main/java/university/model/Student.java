package university.model;

public class Student {
    
    private int studentID;
    private String lastName;
    private String firstName;
    private String major;

    public Student() {}

    public Student(String lastName, String firstName, String major){
        //this.studentID = studentID;
        this.lastName = lastName;
        this.firstName = firstName;
        this.major = major;
    }

    // public int getStudentID() {
    //     return studentID;
    // }

    // public void setStudentID(int studentID) {
    //     this.studentID = studentID;
    // }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    

}
