import org.hibernate.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main
{
    private static final String DATE_PAT = "dd/MM/yyyy";

    public static void main(String[] args)
    {
        SessionFactory factory = HibernateSessionFactory.getSessionFactory();
        Session session = factory.getCurrentSession();

        try {
            session.getTransaction().begin();

            Student student = session.get(Student.class, 10);
   // Составной ключ для subscription entity
            Subscription sub10_9 = session.get(Subscription.class, new SKey(10, 9));
            Subscription sub10_19 = session.get(Subscription.class, new SKey(10, 19));

   // Здесь из студента достаём все табличные значения для курса, на который он подписан
            System.out.println("\nStudent: " + student.getName() + "; age: " + student.getAge() + "\n");
            System.out.println("Name of student: " + student.getName() + "; SizeCourse: " + student
                    .getCourses().size() + "; Student's courses: " + student.getCourses());

   // А здесь по составному ключу даты подписок на курсы студента с id = 10
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_PAT);
            System.out.printf("%52s%14s%n%52s%14s%n", "Subscription date of student id-10, course id-19:",
                    sdf.format(sub10_19.getSubscriptionDate()), "Subscription date of student id-10, " +
                            "course id-9:", sdf.format(sub10_9.getSubscriptionDate()));
            System.out.println("Subscription Id:" + sub10_19.getId());

            Course course = session.get(Course.class, 5);
            System.out.println("\n\tname - " + course.getName() + "; type - " + course.getType() + " " +
                    "- price: " + course.getPrice() +", student count: "+course.getStudentsCount()+"\n");
            System.out.println("============================================\n");

            List<Student> students = course.getStudents();
            for (Student st : students) {
                System.out.printf("%-10s%18s;%20s%10tD%n", "Student:", st.getName(),
                        "Registration date:", st.getRegistrationDate());
            }
            Teacher teacher = session.get(Teacher.class, 9);
            System.out.println("\n\tid - " + teacher.getId() + ", Teacher's name - " + teacher.getName()
                    + ", salary - " + teacher.getSalary() + "\n");

            System.out.println("======================");

            List<Course> courses = student.getCourses();
            for (Course cour : courses) {
                    System.out.printf("%-10s%25s;%11s%3d;%10s%20s;%n", "Course:", cour.getName(),
                            "Duration:", cour.getDuration(), "Teacher:", cour.getTeacher().getName());
            }
            System.out.println("\n=========================");

            PurchaseList pL = session.get(PurchaseList.class, new PLKey("Бойков Максим",
                    "Figma"));
            System.out.println("\nPurchaseList: " + pL.getId() + "Price: " + pL.getPrice() +
                    ";\nSubscription Date: " + pL.getSubscriptionDate());
            System.out.println("\n========================+==");

   // Заполнение созданной через hibernate таблицы LinkedPurchaseList
            String hqlLin = "from LinkedPurchaseList order by studentId";
            List<LinkedPurchaseList> linPurList = session.createQuery(hqlLin).list();
            if (linPurList.isEmpty()) {
                String hqlPur1 = "from PurchaseList order by studentName";
                List<PurchaseList> purList = session.createQuery(hqlPur1).list();
                String hqlSt ="from Student order by name";
                List<Student> stList = session.createQuery(hqlSt).list();
                fillingLinkedPurchaseList(purList, stList);
            }
            else {
                deleteDataIf();
            }
            System.out.println("\n========================+");

   // Проверка заполнения
            if (!linPurList.isEmpty()) {
                LinkedPurchaseList lPL = session.get(LinkedPurchaseList.class, new
                        LPLKey(54, 8));
                System.out.printf("%n%15s%-30s%n%-14s%-40s%n%-8s%-6d%n%-20s%tF", "Student name - ",
                        lPL.getStudentName(), "Course name - ", lPL.getCourseName(), "Price - ",
                        lPL.getPrice(), "Subscription Date - ", lPL.getSubscriptionDate());
            }

            session.getTransaction().commit();

        } catch (Exception ex) {ex.printStackTrace();
            if (session.getTransaction() != null) session.getTransaction().rollback();
        } finally {
            if (session.isOpen()) {
                session.close();
                HibernateSessionFactory.shutdown();
            }
        }
    }

    public static void fillingLinkedPurchaseList (List<PurchaseList> purList, List<Student> stList)
    {
        ArrayList<Integer> prices = new ArrayList<>();
        ArrayList<Date> subDates = new ArrayList<>();
        for (PurchaseList pL : purList) {
            prices.add(pL.getPrice());
            subDates.add(pL.getSubscriptionDate());
        }
        int i = 0;
        Session session = HibernateSessionFactory.getCurrentSession();
        for (Student st : stList) {
            List<Course> courList = st.getCourses();
            for (Course co : courList) {
                LinkedPurchaseList linkedPurchaseList = new LinkedPurchaseList();
                linkedPurchaseList.setId(new LPLKey(st.getId(), co.getId()));
                linkedPurchaseList.setStudentId(st.getId());
                linkedPurchaseList.setCourseId(co.getId());
                linkedPurchaseList.setStudentName(st.getName());
                linkedPurchaseList.setCourseName(co.getName());
                linkedPurchaseList.setPrice(prices.get(i));
                linkedPurchaseList.setSubscriptionDate(subDates.get(i));
                session.save(linkedPurchaseList);
                i++;
            }
        }
        prices.clear();
        subDates.clear();
    }
   /* Метод написан для проверки корректности введённых данных. Надписи на кириллице попадали
    исключительно в виде "?". Пришлось немного помучиться... В результате, в hibernate.cfg
    добавил строку <property name="hibernate.connection.characterEncoding">utf8</property> и
    всё заработало, как надо. */
    private static void deleteDataIf() {
        Session session = HibernateSessionFactory.getCurrentSession();
        LinkedPurchaseList lPL = session.get(LinkedPurchaseList.class, new
                LPLKey(54, 8));
        if (!lPL.getStudentName().equals("Яшнов Владимир")) {
            session.createQuery("delete from LinkedPurchaseList").executeUpdate();
        }
        else {
            System.out.println("\tMaybe everything is alright?");
            System.out.println("\tPerhaps, the table LinkedPurchaseList is full!? It's very very" +
                    " strange, but it's true!!!");
        }
    }
}