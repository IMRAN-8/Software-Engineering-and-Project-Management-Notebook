import java.util.Scanner;

class Employee {
    int salary;                                // instance variable (heap)

    Employee(int salary) {
        this.salary = salary;
    }

    void display(Scanner sc) {
        System.out.println("Inside display() Method:");
        System.out.print("Enter bonus: ");
        int bonus = sc.nextInt();              // local variable (stack frame of display())
        System.out.println("Salary (Instance Variable): " + salary);
        System.out.println("Bonus (Local Variable): " + bonus);
        System.out.println();

        // Local inner class: declared inside the method
        class Report {
            void show() {
                System.out.println("Report: Employee salary is " + salary);
            }
        }

        Report r = new Report();               // Report object is created on the heap
        System.out.println("Report Object Created");
        System.out.println("Report Identity Hash Code: " + System.identityHashCode(r));
        r.show();                              // show() runs in its own stack frame
    }
}

public class Problem2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter salary: ");
        int salary = sc.nextInt();

        Employee e = new Employee(salary);
        System.out.println("Employee Object Created");
        System.out.println("Employee Identity Hash Code: " + System.identityHashCode(e));
        System.out.println();

        e.display(sc);

        System.out.println();
        System.out.println("Memory Concept:");
        System.out.println("Employee object -> Heap");
        System.out.println("Report object -> Heap");
        System.out.println("salary -> Instance field");
        System.out.println("bonus -> Local variable in display()");
        System.out.println("show() -> Executes in its own method frame");
    }
}
