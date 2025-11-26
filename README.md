# **Student Management System - MVC Project**

## **Project Overview**

This is a simple **Student Management System** built using **Jakarta EE (JSP, Servlets, JSTL)** and **MySQL**.
The system follows the **MVC pattern** and demonstrates the following features:

* User authentication (login/logout)
* Role-based authorization (admin vs regular user)
* Student management (CRUD operations)
* Role-based UI (show/hide buttons based on user role)
* Filter-based access control for protected pages

---

## **Technologies Used**

* Java 17 / Jakarta EE
* JSP, Servlets, JSTL
* MySQL
* Maven (optional)
* BCrypt for password hashing

---

## **Project Structure**

```
src/
├─ controller/          # Servlet controllers (LoginController, StudentController, etc.)
├─ dao/                 # Data Access Objects (UserDAO, StudentDAO)
├─ filter/              # AuthFilter.java, AdminFilter.java
├─ model/               # User.java, Student.java
WebContent/
├─ views/
│  ├─ login.jsp
│  ├─ dashboard.jsp
│  ├─ student-list.jsp
│  └─ change-password.jsp (optional)
├─ css/, js/, images/   # Static resources
```

---

## **Exercises Implemented**

### **EXERCISE 5: Authentication Filter (AuthFilter)**

* Checks if a user is logged in for protected pages
* Allows public URLs: `/login`, `/logout`, static resources (`.css`, `.js`, images)
* Redirects to `/login` if not authenticated

**Testing Steps:**

1. Access `/student` without login → redirected to login page
2. Login successfully → access `/student` works
3. Static files (CSS, images) accessible without login

---

### **EXERCISE 6: Admin Authorization Filter (AdminFilter)**

* Restricts admin-only actions: `new`, `insert`, `edit`, `update`, `delete`
* Non-admin users attempting these actions are redirected to the student list page with an error message

**Testing Steps:**

1. Login as admin → perform edit/delete → allowed
2. Logout and login as regular user → edit/delete blocked
3. Direct URL access (`/student?action=delete&id=1`) blocked for non-admin

---

### **EXERCISE 7: Role-Based UI (Student List Page)**

* Navigation bar shows logged-in user info and role badge
* “Add Student” button visible only to admins
* Edit/Delete buttons visible only to admins
* Error messages from `AdminFilter` displayed on the page

**Testing Steps:**

1. Login as admin → see all buttons (Add/Edit/Delete)
2. Login as regular user → buttons hidden
3. Attempt unauthorized admin action → see error message

---

## **Setup Instructions**

1. Install **MySQL** and create a database (e.g., `student_management`)
2. Run SQL script to create `users` and `students` tables and insert test data
3. Configure `UserDAO` and `StudentDAO` with your database credentials
4. Deploy project to **Apache Tomcat**
5. Access the app in browser at `http://localhost:8080/your_project/`

---

## **Test Users**

| Username | Password    | Role  |
| -------- | ----------- | ----- |
| admin1   | password123 | admin |
| user1    | password123 | user  |

---

## **Notes**

* Passwords are stored using **BCrypt hashing**
* The **change password** feature is optional and can be implemented in `ChangePasswordController`
* All admin actions are protected by `AdminFilter`
* All protected pages are guarded by `AuthFilter`

---

## **Submission**

* ZIP file containing the project folder
* Include a **README** explaining setup, users, and testing instructions


Do you want me to make that concise version?
