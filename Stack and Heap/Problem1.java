import java.util.Scanner;

class Student {
    int age;                                   // instance variable (lives on the heap, inside the object)

    Student(int age) {
        this.age = age;
    }

    void display(Scanner sc) {
        System.out.print("Marks: ");
        int marks = sc.nextInt();              // local variable (lives on the stack, in display()'s frame)
        System.out.println("Instance Variable (age): " + age);
        System.out.println("Local Variable (marks): " + marks);
    }
}

public class Problem1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Age: ");
        int age = sc.nextInt();

        Student s = new Student(age);          // object is created on the heap, reference 's' is on the stack
        System.out.println("Student Object Created");
        System.out.println("Object Identity Hash Code: " + System.identityHashCode(s));
        s.display(sc);
    }
}
