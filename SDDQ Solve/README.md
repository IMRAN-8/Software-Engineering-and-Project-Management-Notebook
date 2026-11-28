# Software Design – Answers

## Student Information

| Field | Details |
|---|---|
| **Name** | Md. Imran Hosen |
| **Student ID** | IT-24060 |
| **University** | Mawlana Bhashani Science and Technology University (MBSTU) |
| **Department** | Information and Communication Technology (ICT) |
| **Course Title** | Software Engineering Project Management |
| **Course Code** | ICT_3107 |
| **Topic** | Software Design |

---

## Q1. Define software design. State its two basic design activities. (2)

Software design is the process of planning the structure and components of a software system before implementation.

The two basic design activities are:
1. Architectural design.
2. Detailed design.

## Q2. Differentiate between architectural design and detailed design. (4)

| Architectural Design | Detailed Design |
|---|---|
| Defines the overall structure of the system. | Defines the internal details of each component. |
| Identifies major modules and their relationships. | Describes algorithms, methods, and data structures. |
| Focuses on the big picture. | Focuses on implementation details. |

**Example:** In an online shopping system, architectural design identifies the user, payment, and product modules. Detailed design describes how the payment module processes a transaction.

## Q3. Define coupling and cohesion. (2)

- **Coupling:** The degree of dependency between different software modules.
- **Cohesion:** The degree to which the elements within a module are related to each other.

## Q4. Explain why low coupling and high cohesion are important. (3)

Low coupling means modules have fewer dependencies on each other. Therefore, changing one module has less effect on other modules.

High cohesion means a module focuses on a single related responsibility. This makes the module easier to understand, test, and maintain.

Together, they improve software quality and maintainability.

## Q5. Suggest how to divide a module responsible for login, payment, and report generation. (2)

I would divide it into three separate modules:

1. **Login Module:** Handles user authentication.
2. **Payment Module:** Processes payments and transactions.
3. **Report Module:** Generates and displays reports.

This improves modularity because each module has a separate responsibility.

## Q6. Define CBO and WMC metrics. (2)

- **CBO (Coupling Between Objects):** Measures how many other classes a class is connected to or depends on.
- **WMC (Weighted Methods per Class):** Measures the total complexity of a class by adding the complexity of its methods.

## Q7. Explain how software design metrics evaluate quality and complexity. (4)

Software design metrics help measure the quality and complexity of a software system.

- **CBO:** Measures dependencies between classes. Lower CBO generally indicates better modularity.
- **WMC:** Measures the complexity of a class. Higher WMC may indicate that a class is difficult to maintain.
- **LCOM:** Measures how closely the methods of a class use common attributes. Lower LCOM generally indicates better cohesion.

These metrics help developers identify design problems and improve maintainability.

## Q8. Define software architecture and name four common architectural styles. (3)

Software architecture describes the overall structure of a software system, including its components and their relationships.

Four common architectural styles are:

1. Layered Architecture.
2. Client-Server Architecture.
3. Model-View-Controller (MVC).
4. Pipes and Filters Architecture.

## Q9. Explain the roles of Model, View, and Controller in MVC. (4)

MVC stands for Model-View-Controller.

- **Model:** Manages application data and business logic.
- **View:** Displays information to the user.
- **Controller:** Receives user input and coordinates actions between the Model and View.

For example, in a student management application, the Model stores student information, the View displays it, and the Controller handles user requests.

## Q10. Name the three main categories of GoF design patterns and give one example of each. (3)

The three categories are:

1. **Creational Patterns:** Singleton.
2. **Structural Patterns:** Decorator.
3. **Behavioral Patterns:** Observer.

## Q11. Explain the intent and benefits of Singleton and Observer patterns. (5)

**Singleton Pattern**

**Intent:** Ensures that a class has only one instance and provides a global access point to it.

**Benefits:**
- Controls the creation of objects.
- Provides one shared instance throughout the application.

**Observer Pattern**

**Intent:** Creates a one-to-many relationship between objects. When one object changes, its dependent objects are automatically notified.

**Benefits:**
- Keeps related objects updated.
- Reduces direct dependency between objects.

## Q12. Identify a suitable design pattern for an online notification system and explain how it works. (6)

The **Observer Design Pattern** is suitable for an online notification system.

**How it works:**

1. The announcement system acts as the Subject.
2. Users act as Observers.
3. Users subscribe to receive notifications.
4. When a new announcement is published, the Subject notifies all subscribed users.
5. Each user receives the updated announcement.

**Example:** When a university publishes a new notice, all subscribed students automatically receive a notification.

**Benefits:**
- Automatically notifies multiple users.
- Reduces dependency between the announcement system and users.
- New users can subscribe without changing the main system.

Question souce : [IT-24002](https://github.com/khanmila95-alt/Assignment-for-software-Engineering-Make-question-on-155-slide-)
