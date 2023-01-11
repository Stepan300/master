import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "subscriptions")

public class Subscription
{
      @EmbeddedId
    private SKey id;
      @Column(name = "student_id", insertable = false, updatable = false)
    private int studentId;
      @Column(name = "course_id", insertable = false, updatable = false)
    private int courseId;
      @Column(name = "subscription_date")
    private Date subscriptionDate;

 // =============================================================================
    public SKey getId() {return id;}
    public void setId(SKey id) {this.id = id;}

    public int getStudentId() {
        return studentId;
    }
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Date getSubscriptionDate() {return subscriptionDate;}
    public void setSubscriptionDate(Date subscriptionDate) {this.subscriptionDate = subscriptionDate;}
}

//    @Override
//    public String toString() {
//        return "Subscription{" +
//                "studentId=" + studentId +
//                ", subscriptionDate=" + subscriptionDate +
//                '}';
//    }
