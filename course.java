
public class Course {

    private String courseCode;
    private String courseTitle;
    private int units;

    // Constructor
    public Course(String courseCode, String courseTitle, int units) {
        this.courseCode = courseCode.trim().toUpperCase();
        this.courseTitle = courseTitle.trim();
        this.units = units;
    }

    // Getters
    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public int getUnits() {
        return units;
    }

    // Setters
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode.trim().toUpperCase();
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle.trim();
    }

    public void setUnits(int units) {
        this.units = units;
    }

    // Display course details
    @Override
    public String toString() {
        return courseCode + " - " + courseTitle
                + " (" + units + " units)";
    }
}
