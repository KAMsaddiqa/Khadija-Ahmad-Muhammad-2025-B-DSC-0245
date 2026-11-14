
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class CourseManager {

    private ArrayList<Course> courses;

    // Constructor
    public CourseManager() {
        courses = new ArrayList<>();
    }

    // Add a new course
    public void addCourse(Course course) {

        if (course == null) {
            throw new IllegalArgumentException(
                    "Course cannot be empty.");
        }

        if (course.getCourseCode().trim().isEmpty()
                || course.getCourseTitle().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Course code and title are required.");
        }

        if (course.getUnits() <= 0) {
            throw new IllegalArgumentException(
                    "Units must be greater than zero.");
        }

        if (searchCourse(course.getCourseCode()) != null) {
            throw new IllegalArgumentException(
                    "This course code already exists.");
        }

        courses.add(course);
    }

    // Return all courses
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses);
    }

    // Search for a course by code
    public Course searchCourse(String code) {

        if (code == null) {
            return null;
        }

        String searchCode = code.trim().toUpperCase();

        for (Course course : courses) {
            if (course.getCourseCode().equalsIgnoreCase(searchCode)) {
                return course;
            }
        }

        return null;
    }

    // Delete a course
    public boolean deleteCourse(String code) {

        Course course = searchCourse(code);

        if (course != null) {
            courses.remove(course);
            return true;
        }

        return false;
    }

    // Calculate total units using recursion
    public int calculateTotalUnits() {
        return calculateUnitsRecursive(0);
    }

    private int calculateUnitsRecursive(int index) {

        if (index >= courses.size()) {
            return 0;
        }

        return courses.get(index).getUnits()
                + calculateUnitsRecursive(index + 1);
    }

    // Save courses to file
    public void saveCourses(String filename) throws IOException {

        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(filename),
                StandardCharsets.UTF_8)) {

            for (Course course : courses) {

                writer.write(course.getCourseCode() + "\t"
                        + course.getCourseTitle() + "\t"
                        + course.getUnits());

                writer.newLine();
            }
        }
    }

    // Load courses from file
    public void loadCourses(String filename) throws IOException {

        Path path = Paths.get(filename);

        if (!Files.exists(path)) {
            throw new FileNotFoundException(
                    "The course file does not exist.");
        }

        ArrayList<Course> loadedCourses = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(
                path, StandardCharsets.UTF_8)) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {

                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = line.split("\t", -1);

                if (data.length != 3) {
                    throw new IOException(
                            "Invalid record at line " + lineNumber);
                }

                try {
                    String code = data[0].trim().toUpperCase();
                    String title = data[1].trim();
                    int units = Integer.parseInt(data[2].trim());

                    if (code.isEmpty() || title.isEmpty() || units <= 0) {
                        throw new IllegalArgumentException(
                                "Invalid course information.");
                    }

                    // Check for duplicate course codes
                    for (Course existing : loadedCourses) {
                        if (existing.getCourseCode()
                                .equalsIgnoreCase(code)) {
                            throw new IllegalArgumentException(
                                    "Duplicate course code.");
                        }
                    }

                    loadedCourses.add(
                            new Course(code, title, units));

                } catch (NumberFormatException e) {

                    throw new IOException(
                            "Invalid course data at line "
                                    + lineNumber + ": " + e.getMessage());

                } catch (IllegalArgumentException e) {

                    throw new IOException(
                            "Invalid course data at line "
                                    + lineNumber + ": " + e.getMessage());
                }
            }
        }

        // Replace current records only after successful loading
        courses.clear();
        courses.addAll(loadedCourses);
    }
}
