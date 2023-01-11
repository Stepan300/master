import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PLKey implements Serializable
{
        @Column(name = "student_name")
    private String studentName;
        @Column(name = "course_name")
    private String courseName;

 // ====================================================
    public PLKey() {}

    public PLKey(String studentName, String courseName) {
        this.studentName = studentName;
        this.courseName = courseName;
    }

    public String getStudentName() {return studentName;}
    public void setStudentName(String studentName) {this.studentName = studentName;}
    public String getCourseName() {return courseName;}
    public void setCourseName(String courseName) {this.courseName = courseName;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PurchaseList)) return false;
        PLKey that = (PLKey) o;
        return Objects.equals(getStudentName(), that.getStudentName()) && Objects
                .equals(getCourseName(), that.getCourseName());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getStudentName(), getCourseName());
    }

    @Override
    public String toString() {
        return "\n\tPLKey: " + "student Name - " + getStudentName() + "\n" +
                "\t\t\tcourse Name - " + getCourseName() + "\n";
    }
}
