import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LPLKey implements Serializable
{
      @Column(name = "student_id")
    private int studentId;
      @Column(name = "course_id")
    private int courseId;

 // ====================================================
    public LPLKey() {}

    public LPLKey(int studentId, int courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    public int getStudentId() {return studentId;}
    public void setStudentId(int studentId) {this.studentId = studentId;}
    public int getCourseId() {return courseId;}
    public void setCourseId(int courseId) {this.courseId = courseId;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LinkedPurchaseList)) return false;
        LPLKey that = (LPLKey) o;
        return Objects.equals(getStudentId(), that.getStudentId()) && Objects
                .equals(getCourseId(), that.getCourseId());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getStudentId(), getCourseId());
    }

    @Override
    public String toString() {
        return "\n\tLPLKey: student id - " + getStudentId() + "\n" +
                "\t\t\tcourse id - " + getCourseId() + "\n";
    }
}
