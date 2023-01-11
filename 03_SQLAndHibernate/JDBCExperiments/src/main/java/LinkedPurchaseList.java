import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "linkedpurchaselist")

public class LinkedPurchaseList
{
      @EmbeddedId
    private LPLKey id;
      @Column(name = "student_id", insertable = false, updatable = false)
    private int studentId;
      @Column(name = "course_id", insertable = false, updatable = false)
    private int courseId;
      @Column(name = "student_name")
    private String studentName;
      @Column(name = "course_name")
    private String courseName;
    private int price;
      @Column(name = "subscription_date")
    private Date subscriptionDate;

 // =========================================================
    public LinkedPurchaseList() {}

    public LPLKey getId() {return id;}
    public void setId(LPLKey id) {this.id = id;}

    public int getStudentId() {
     return studentId;
 }
    public void setStudentId(int studentId) {this.studentId = studentId;}

    public int getCourseId() {return courseId;}
    public void setCourseId(int courseId) {this.courseId = courseId;}

    public String getStudentName() {return studentName;}
    public void setStudentName(String studentName) {this.studentName = studentName;}

    public String getCourseName() {return courseName;}
    public void setCourseName(String courseName) {this.courseName = courseName;}

    public int getPrice() {return price;}
    public void setPrice(int price) {this.price = price;}

    public Date getSubscriptionDate() {return subscriptionDate;}
    public void setSubscriptionDate(Date subscriptionDate) {this.subscriptionDate = subscriptionDate;}

    @Override
    public String toString() {
        return "\n\tStudent ID: " + studentId + "\n" +
                "\tCourse ID:  " + courseId + "\n" +
                "\tStudent name: " + studentName + "\n" +
                "\tCourse name:  " + courseName + "\n" +
                "\tPrice: " + price + "\n" +
                "\tDate:  " + subscriptionDate;
    }
}
