# 🚀 Master Servlet, JSP, JSTL & MySQL CRUD in IntelliJ Community Edition: Complete Step-by-Step Guide

> [!TIP]
> **No IntelliJ Ultimate Edition needed!** 
> This guide is customized specifically for **IntelliJ IDEA Community Edition (Free)** using the free **Smart Tomcat** plugin.

---

## 📑 Table of Contents
1. [IntelliJ Community Edition Setup & Smart Tomcat Plugin](#1-intellij-community-edition-setup--smart-tomcat-plugin)
2. [Where to Download Required JAR Files (4 JARs)](#2-where-to-download-required-jar-files-4-jars)
3. [Creating the Project in IntelliJ Community Edition](#3-creating-the-project-in-intellij-community-edition)
4. [Step 1: First "Hello Servlet" Test (UI Verification)](#step-1-first-hello-servlet-test-ui-verification)
5. [Step 2: MySQL Database & Relational Schema Setup](#step-2-mysql-database--relational-schema-setup)
6. [Step 3: Database Connection & Model Classes](#step-3-database-connection--model-classes)
7. [Step 4: Manager CRUD (DAO, Servlet & JSTL Views)](#step-4-manager-crud-dao-servlet--jstl-views)
8. [Step 5: Employee CRUD with Dynamic Manager Dropdown](#step-5-employee-crud-with-dynamic-manager-dropdown)
9. [Step 6: UI Styling & Dashboard Navigation](#step-6-ui-styling--dashboard-navigation)
10. [Troubleshooting & Common Pitfalls](#troubleshooting--common-pitfalls)

---

## 1. IntelliJ Community Edition Setup & Smart Tomcat Plugin

IntelliJ Community Edition does not have a built-in Application Server panel, but you can add Tomcat integration with one free click:

### Install "Smart Tomcat" Plugin:
1. Open IntelliJ IDEA Community.
2. Go to **Settings** (or **Preferences** on Mac) -> **Plugins**.
3. Select the **Marketplace** tab at the top.
4. Search for: `Smart Tomcat` (by *pwielgolaski*).
5. Click **Install**, then click **Restart IDE**.

---

## 2. Where to Download Required JAR Files (4 JARs)

Because Community Edition doesn't automatically attach Tomcat libraries to your code editor, download these **4 JARs** and place them in a folder.

### 1. Jakarta Servlet API (For code completion & compilation)
- **What it does**: Provides `HttpServlet`, `HttpServletRequest`, `HttpServletResponse`, `@WebServlet`.
- **File**: `jakarta.servlet-api-6.0.0.jar`
- **Download Link**: [Maven Central - Jakarta Servlet API 6.0.0](https://repo1.maven.org/maven2/jakarta/servlet/jakarta.servlet-api/6.0.0/jakarta.servlet-api-6.0.0.jar)
  *(Or grab `servlet-api.jar` directly from your Tomcat `libexec/lib/` or `tomcat/lib/` folder)*

### 2. MySQL JDBC Driver
- **What it does**: Connects Java code to MySQL database.
- **File**: `mysql-connector-j-8.3.0.jar`
- **Download Link**: [Maven Central - MySQL Connector J 8.3.0](https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar)

### 3. Jakarta Standard Tag Library (JSTL) API
- **What it does**: Provides `<c:forEach>`, `<c:if>`, `<c:out>` tag definitions.
- **File**: `jakarta.servlet.jsp.jstl-api-3.0.0.jar`
- **Download Link**: [Maven Central - JSTL API 3.0.0](https://repo1.maven.org/maven2/jakarta/servlet/jsp/jstl/jakarta.servlet.jsp.jstl-api/3.0.0/jakarta.servlet.jsp.jstl-api-3.0.0.jar)

### 4. Jakarta Standard Tag Library (JSTL) Implementation (Glassfish)
- **What it does**: Executes JSTL tags at runtime inside JSP pages.
- **File**: `jakarta.servlet.jsp.jstl-3.0.1.jar`
- **Download Link**: [Maven Central - JSTL Implementation 3.0.1](https://repo1.maven.org/maven2/org/glassfish/web/jakarta.servlet.jsp.jstl/3.0.1/jakarta.servlet.jsp.jstl-3.0.1.jar)

---

## 3. Creating the Project in IntelliJ Community Edition

### 3.1 Create Standard Java Project
1. **File** -> **New** -> **Project...**
2. Choose **Java** (JDK 17 or JDK 21).
   - Name: `EmployeeManagementApp`
   - Build System: **IntelliJ**
3. Click **Create**.

### 3.2 Create the Folder Structure
Inside your new project, right-click and create directories so the project looks like this:

```
EmployeeManagementApp/
├── src/
│   └── com/
│       └── learn/
│           ├── model/
│           ├── dao/
│           ├── servlet/
│           └── util/
└── web/
    ├── css/
    │   └── styles.css
    ├── WEB-INF/
    │   ├── lib/                  <-- Paste all 4 downloaded JAR files here!
    │   └── web.xml
    ├── index.jsp
    ├── manager-list.jsp
    ├── manager-form.jsp
    ├── employee-list.jsp
    └── employee-form.jsp
```

### 3.3 Add JARs to Project Dependencies (Classpath)
1. Copy all 4 `.jar` files into the `web/WEB-INF/lib/` folder.
2. In IntelliJ:
   - Go to **File** -> **Project Structure...** (`Cmd + ;` on Mac or `Ctrl + Alt + Shift + S` on Windows).
   - Click **Modules** -> Select `EmployeeManagementApp` -> Click the **Dependencies** tab.
   - Click the **`+`** (Plus icon) -> **JARs or Directories...**
   - Navigate to `web/WEB-INF/lib/` and select all 4 `.jar` files.
   - Click **Apply** -> **OK**.
   *(Now IntelliJ will recognize `jakarta.servlet.*`, `java.sql.*`, and JSTL with zero red squiggly lines!)*

### 3.4 Configure Smart Tomcat Run Configuration
1. In the top toolbar, click the dropdown next to the green Play button -> **Edit Configurations...**
2. Click **`+`** (Add New Configuration) -> Select **Smart Tomcat**.
3. Fill in the fields:
   - **Name**: `Tomcat 11`
   - **Tomcat Server**: Browse and select your Tomcat installation directory:
     - On Mac Homebrew: `/usr/local/Cellar/tomcat/11.0.x/libexec` (or `/opt/homebrew/opt/tomcat/libexec`)
     - On Windows: `C:\apache-tomcat-11.x`
   - **Deployment Directory**: Click Browse and select your `web` folder (e.g. `/path/to/EmployeeManagementApp/web`).
   - **Context Path**: `/app`
   - **Server Port**: `8080`
4. Click **Apply** -> **OK**.

---

## Step 1: First "Hello Servlet" Test (UI Verification)

### 🎯 Purpose:
Verify that your Smart Tomcat server compiles your Java code, deploys the `web` folder, and returns dynamic HTML in your browser.

### 📝 Code: `src/com/learn/servlet/TestServlet.java`

```java
package com.learn.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@WebServlet("/test")
public class TestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet Test Status</title>");
        out.println("<style>");
        out.println("body { font-family: 'Segoe UI', Tahoma, sans-serif; padding: 40px; background-color: #f4f6f9; }");
        out.println(".card { background: white; border-radius: 8px; padding: 30px; max-width: 600px; margin: 0 auto; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }");
        out.println(".success-badge { background-color: #28a745; color: white; padding: 6px 12px; border-radius: 20px; font-size: 14px; font-weight: bold; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='card'>");
        out.println("<h2>Servlet Engine Test</h2>");
        out.println("<p><span class='success-badge'>✓ Servlet is Running on IntelliJ Community Edition!</span></p>");
        out.println("<hr>");
        out.println("<p><strong>Current Server Time:</strong> " + new Date() + "</p>");
        out.println("<p><strong>Context Path:</strong> " + request.getContextPath() + "</p>");
        out.println("<p><strong>Servlet Path:</strong> " + request.getServletPath() + "</p>");
        out.println("<p><a href='" + request.getContextPath() + "/index.jsp'>Go to Home Index</a></p>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}
```

### 📝 Code: `web/WEB-INF/web.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="https://jakarta.ee/xml/ns/jakartaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee https://jakarta.ee/xml/ns/jakartaee/web-app_6_0.xsd"
         version="6.0">
    <welcome-file-list>
        <welcome-file>index.jsp</welcome-file>
    </welcome-file-list>
</web-app>
```

### 📝 Code: `web/index.jsp`

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Employee Management System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="navbar">
    <a href="${pageContext.request.contextPath}/index.jsp" class="active">🏠 Dashboard</a>
    <a href="${pageContext.request.contextPath}/managers">👥 Managers</a>
    <a href="${pageContext.request.contextPath}/employees">👔 Employees</a>
</div>

<div class="content-container" style="text-align: center;">
    <h1>🚀 Employee & Manager Management</h1>
    <p style="color: #64748b; margin-top: 8px;">Servlet + JSP + JSTL + MySQL Relational CRUD Application</p>
    <hr style="margin: 25px 0; border: 0; border-top: 1px solid #e2e8f0;">
    
    <div style="display: flex; justify-content: center; gap: 15px; margin-top: 20px;">
        <a href="${pageContext.request.contextPath}/test" class="btn btn-secondary">1. Test Servlet Status</a>
        <a href="${pageContext.request.contextPath}/managers" class="btn btn-primary">2. Manage Managers</a>
        <a href="${pageContext.request.contextPath}/employees" class="btn btn-primary" style="background-color: #059669;">3. Manage Employees</a>
    </div>
</div>
</body>
</html>
```

### 🧪 Test & Verify:
1. Click the green **Run** button for **Tomcat 11**.
2. Open your browser to: `http://localhost:8080/app/test`
3. **Result**: You should see the green **"✓ Servlet is Running on IntelliJ Community Edition!"** message!

---

## Step 2: MySQL Database & Relational Schema Setup

### 📝 Run this SQL script in MySQL Workbench or Terminal:

```sql
CREATE DATABASE IF NOT EXISTS emp_mgmt_db;
USE emp_mgmt_db;

-- 1. Create Managers Table
CREATE TABLE IF NOT EXISTS managers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20)
);

-- 2. Create Employees Table (with Foreign Key linking to managers)
CREATE TABLE IF NOT EXISTS employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    designation VARCHAR(100) NOT NULL,
    salary DOUBLE NOT NULL,
    manager_id INT,
    CONSTRAINT fk_employee_manager 
        FOREIGN KEY (manager_id) 
        REFERENCES managers(id) 
        ON DELETE SET NULL 
        ON UPDATE CASCADE
);

-- 3. Seed Initial Managers
INSERT INTO managers (name, department, email, phone) VALUES
('Sarah Connor', 'Engineering', 'sarah.connor@example.com', '555-0101'),
('Michael Scott', 'Sales & Marketing', 'michael.scott@example.com', '555-0102'),
('Jessica Pearson', 'Legal & HR', 'jessica.pearson@example.com', '555-0103');

-- 4. Seed Initial Employees
INSERT INTO employees (name, email, designation, salary, manager_id) VALUES
('John Doe', 'john.doe@example.com', 'Senior Software Engineer', 95000, 1),
('Jim Halpert', 'jim.halpert@example.com', 'Sales Executive', 65000, 2),
('Rachel Zane', 'rachel.zane@example.com', 'HR Specialist', 70000, 3);
```

---

## Step 3: Database Connection & Model Classes

### 3.1 `src/com/learn/util/DBConnection.java`

```java
package com.learn.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/emp_mgmt_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "your_mysql_password"; // <-- Update with your password

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver not found in classpath!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
```

### 3.2 `src/com/learn/model/Manager.java`

```java
package com.learn.model;

import java.io.Serializable;

public class Manager implements Serializable {
    private int id;
    private String name;
    private String department;
    private String email;
    private String phone;

    public Manager() {}

    public Manager(String name, String department, String email, String phone) {
        this.name = name;
        this.department = department;
        this.email = email;
        this.phone = phone;
    }

    public Manager(int id, String name, String department, String email, String phone) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.email = email;
        this.phone = phone;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
```

### 3.3 `src/com/learn/model/Employee.java`

```java
package com.learn.model;

import java.io.Serializable;

public class Employee implements Serializable {
    private int id;
    private String name;
    private String email;
    private String designation;
    private double salary;
    private Integer managerId;
    private String managerName; // Display property for join query

    public Employee() {}

    public Employee(String name, String email, String designation, double salary, Integer managerId) {
        this.name = name;
        this.email = email;
        this.designation = designation;
        this.salary = salary;
        this.managerId = managerId;
    }

    public Employee(int id, String name, String email, String designation, double salary, Integer managerId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.designation = designation;
        this.salary = salary;
        this.managerId = managerId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public Integer getManagerId() { return managerId; }
    public void setManagerId(Integer managerId) { this.managerId = managerId; }

    public String getManagerName() { return managerName; }
    public void setManagerName(String managerName) { this.managerName = managerName; }
}
```

---

## Step 4: Manager CRUD (DAO, Servlet & JSTL Views)

### 4.1 `src/com/learn/dao/ManagerDAO.java`

```java
package com.learn.dao;

import com.learn.model.Manager;
import com.learn.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManagerDAO {

    public List<Manager> getAllManagers() {
        List<Manager> list = new ArrayList<>();
        String sql = "SELECT * FROM managers ORDER BY name ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Manager(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getString("email"),
                        rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Manager getManagerById(int id) {
        String sql = "SELECT * FROM managers WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Manager(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("department"),
                            rs.getString("email"),
                            rs.getString("phone")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertManager(Manager m) {
        String sql = "INSERT INTO managers (name, department, email, phone) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, m.getName());
            ps.setString(2, m.getDepartment());
            ps.setString(3, m.getEmail());
            ps.setString(4, m.getPhone());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateManager(Manager m) {
        String sql = "UPDATE managers SET name = ?, department = ?, email = ?, phone = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, m.getName());
            ps.setString(2, m.getDepartment());
            ps.setString(3, m.getEmail());
            ps.setString(4, m.getPhone());
            ps.setInt(5, m.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteManager(int id) {
        String sql = "DELETE FROM managers WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
```

### 4.2 `src/com/learn/servlet/ManagerServlet.java`

```java
package com.learn.servlet;

import com.learn.dao.ManagerDAO;
import com.learn.model.Manager;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/managers")
public class ManagerServlet extends HttpServlet {

    private ManagerDAO managerDAO;

    @Override
    public void init() {
        managerDAO = new ManagerDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteManager(request, response);
                break;
            default:
                listManagers(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        String name = request.getParameter("name");
        String department = request.getParameter("department");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        if (idStr == null || idStr.trim().isEmpty()) {
            Manager newManager = new Manager(name, department, email, phone);
            managerDAO.insertManager(newManager);
        } else {
            int id = Integer.parseInt(idStr);
            Manager manager = new Manager(id, name, department, email, phone);
            managerDAO.updateManager(manager);
        }

        response.sendRedirect(request.getContextPath() + "/managers");
    }

    private void listManagers(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Manager> managers = managerDAO.getAllManagers();
        request.setAttribute("managersList", managers);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/manager-list.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/manager-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Manager existingManager = managerDAO.getManagerById(id);
        request.setAttribute("manager", existingManager);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/manager-form.jsp");
        dispatcher.forward(request, response);
    }

    private void deleteManager(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        managerDAO.deleteManager(id);
        response.sendRedirect(request.getContextPath() + "/managers");
    }
}
```

### 4.3 `web/manager-list.jsp`

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Managers List</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="navbar">
    <a href="${pageContext.request.contextPath}/index.jsp">🏠 Dashboard</a>
    <a href="${pageContext.request.contextPath}/managers" class="active">👥 Managers</a>
    <a href="${pageContext.request.contextPath}/employees">👔 Employees</a>
</div>

<div class="content-container">
    <div class="header-row">
        <h2>Manager Directory</h2>
        <a href="${pageContext.request.contextPath}/managers?action=new" class="btn btn-primary">+ Add New Manager</a>
    </div>

    <table class="data-table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Department</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="mgr" items="${managersList}">
            <tr>
                <td>${mgr.id}</td>
                <td><strong>${mgr.name}</strong></td>
                <td><span class="badge department-badge">${mgr.department}</span></td>
                <td>${mgr.email}</td>
                <td>${mgr.phone}</td>
                <td class="action-links">
                    <a href="${pageContext.request.contextPath}/managers?action=edit&id=${mgr.id}" class="btn-sm btn-edit">Edit</a>
                    <a href="${pageContext.request.contextPath}/managers?action=delete&id=${mgr.id}" 
                       onclick="return confirm('Are you sure you want to delete this manager?');" 
                       class="btn-sm btn-delete">Delete</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty managersList}">
            <tr>
                <td colspan="6" style="text-align: center; color: #888;">No managers found.</td>
            </tr>
        </c:if>
        </tbody>
    </table>
</div>
</body>
</html>
```

### 4.4 `web/manager-form.jsp`

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title><c:out value="${manager != null ? 'Edit Manager' : 'Add Manager'}"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="navbar">
    <a href="${pageContext.request.contextPath}/index.jsp">🏠 Dashboard</a>
    <a href="${pageContext.request.contextPath}/managers" class="active">👥 Managers</a>
    <a href="${pageContext.request.contextPath}/employees">👔 Employees</a>
</div>

<div class="form-container">
    <h2><c:out value="${manager != null ? 'Edit Manager Details' : 'Register New Manager'}"/></h2>
    
    <form action="${pageContext.request.contextPath}/managers" method="post">
        <input type="hidden" name="id" value="${manager.id}">

        <div class="form-group">
            <label for="name">Manager Full Name *</label>
            <input type="text" id="name" name="name" value="${manager.name}" required placeholder="e.g. Sarah Connor">
        </div>

        <div class="form-group">
            <label for="department">Department *</label>
            <input type="text" id="department" name="department" value="${manager.department}" required placeholder="e.g. Engineering">
        </div>

        <div class="form-group">
            <label for="email">Email Address *</label>
            <input type="email" id="email" name="email" value="${manager.email}" required placeholder="e.g. sarah@example.com">
        </div>

        <div class="form-group">
            <label for="phone">Phone Number</label>
            <input type="text" id="phone" name="phone" value="${manager.phone}" placeholder="e.g. +1 555-0199">
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Save Manager</button>
            <a href="${pageContext.request.contextPath}/managers" class="btn btn-secondary">Cancel</a>
        </div>
    </form>
</div>
</body>
</html>
```

### 🧪 Test Manager CRUD:
- Open `http://localhost:8080/app/managers`
- Add a new Manager -> Click Edit -> Update Phone -> Test Delete.

---

## Step 5: Employee CRUD with Dynamic Manager Dropdown

### 5.1 `src/com/learn/dao/EmployeeDAO.java`

```java
package com.learn.dao;

import com.learn.model.Employee;
import com.learn.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT e.id, e.name, e.email, e.designation, e.salary, e.manager_id, m.name AS manager_name " +
                     "FROM employees e " +
                     "LEFT JOIN managers m ON e.manager_id = m.id " +
                     "ORDER BY e.id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Employee emp = new Employee();
                emp.setId(rs.getInt("id"));
                emp.setName(rs.getString("name"));
                emp.setEmail(rs.getString("email"));
                emp.setDesignation(rs.getString("designation"));
                emp.setSalary(rs.getDouble("salary"));
                
                int mId = rs.getInt("manager_id");
                emp.setManagerId(rs.wasNull() ? null : mId);
                emp.setManagerName(rs.getString("manager_name"));

                list.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Employee getEmployeeById(int id) {
        String sql = "SELECT * FROM employees WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Employee emp = new Employee();
                    emp.setId(rs.getInt("id"));
                    emp.setName(rs.getString("name"));
                    emp.setEmail(rs.getString("email"));
                    emp.setDesignation(rs.getString("designation"));
                    emp.setSalary(rs.getDouble("salary"));
                    
                    int mId = rs.getInt("manager_id");
                    emp.setManagerId(rs.wasNull() ? null : mId);
                    return emp;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertEmployee(Employee emp) {
        String sql = "INSERT INTO employees (name, email, designation, salary, manager_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, emp.getName());
            ps.setString(2, emp.getEmail());
            ps.setString(3, emp.getDesignation());
            ps.setDouble(4, emp.getSalary());

            if (emp.getManagerId() != null && emp.getManagerId() > 0) {
                ps.setInt(5, emp.getManagerId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateEmployee(Employee emp) {
        String sql = "UPDATE employees SET name = ?, email = ?, designation = ?, salary = ?, manager_id = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, emp.getName());
            ps.setString(2, emp.getEmail());
            ps.setString(3, emp.getDesignation());
            ps.setDouble(4, emp.getSalary());

            if (emp.getManagerId() != null && emp.getManagerId() > 0) {
                ps.setInt(5, emp.getManagerId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            ps.setInt(6, emp.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteEmployee(int id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
```

### 5.2 `src/com/learn/servlet/EmployeeServlet.java`

```java
package com.learn.servlet;

import com.learn.dao.EmployeeDAO;
import com.learn.dao.ManagerDAO;
import com.learn.model.Employee;
import com.learn.model.Manager;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/employees")
public class EmployeeServlet extends HttpServlet {

    private EmployeeDAO employeeDAO;
    private ManagerDAO managerDAO;

    @Override
    public void init() {
        employeeDAO = new EmployeeDAO();
        managerDAO = new ManagerDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteEmployee(request, response);
                break;
            default:
                listEmployees(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String designation = request.getParameter("designation");
        double salary = Double.parseDouble(request.getParameter("salary"));
        
        String managerIdStr = request.getParameter("managerId");
        Integer managerId = (managerIdStr != null && !managerIdStr.trim().isEmpty()) 
                ? Integer.parseInt(managerIdStr) : null;

        if (idStr == null || idStr.trim().isEmpty()) {
            Employee newEmp = new Employee(name, email, designation, salary, managerId);
            employeeDAO.insertEmployee(newEmp);
        } else {
            int id = Integer.parseInt(idStr);
            Employee emp = new Employee(id, name, email, designation, salary, managerId);
            employeeDAO.updateEmployee(emp);
        }

        response.sendRedirect(request.getContextPath() + "/employees");
    }

    private void listEmployees(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Employee> employees = employeeDAO.getAllEmployees();
        request.setAttribute("employeesList", employees);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/employee-list.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 🔥 Pre-fetch managers for the dropdown
        List<Manager> managers = managerDAO.getAllManagers();
        request.setAttribute("managers", managers);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/employee-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Employee existingEmployee = employeeDAO.getEmployeeById(id);
        List<Manager> managers = managerDAO.getAllManagers();

        request.setAttribute("employee", existingEmployee);
        request.setAttribute("managers", managers);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/employee-form.jsp");
        dispatcher.forward(request, response);
    }

    private void deleteEmployee(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        employeeDAO.deleteEmployee(id);
        response.sendRedirect(request.getContextPath() + "/employees");
    }
}
```

### 5.3 `web/employee-list.jsp`

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Employees Directory</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="navbar">
    <a href="${pageContext.request.contextPath}/index.jsp">🏠 Dashboard</a>
    <a href="${pageContext.request.contextPath}/managers">👥 Managers</a>
    <a href="${pageContext.request.contextPath}/employees" class="active">👔 Employees</a>
</div>

<div class="content-container">
    <div class="header-row">
        <h2>Employee Directory</h2>
        <a href="${pageContext.request.contextPath}/employees?action=new" class="btn btn-primary">+ Register New Employee</a>
    </div>

    <table class="data-table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Designation</th>
            <th>Salary</th>
            <th>Assigned Manager</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="emp" items="${employeesList}">
            <tr>
                <td>${emp.id}</td>
                <td><strong>${emp.name}</strong></td>
                <td>${emp.email}</td>
                <td>${emp.designation}</td>
                <td><span class="salary-tag">$<fmt:formatNumber value="${emp.salary}" pattern="#,##0.00"/></span></td>
                <td>
                    <c:choose>
                        <c:when test="${not empty emp.managerName}">
                            <span class="badge manager-badge">👤 ${emp.managerName}</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge unassigned-badge">Unassigned</span>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td class="action-links">
                    <a href="${pageContext.request.contextPath}/employees?action=edit&id=${emp.id}" class="btn-sm btn-edit">Edit</a>
                    <a href="${pageContext.request.contextPath}/employees?action=delete&id=${emp.id}" 
                       onclick="return confirm('Are you sure you want to delete this employee?');" 
                       class="btn-sm btn-delete">Delete</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty employeesList}">
            <tr>
                <td colspan="7" style="text-align: center; color: #888;">No employees registered yet.</td>
            </tr>
        </c:if>
        </tbody>
    </table>
</div>
</body>
</html>
```

### 5.4 `web/employee-form.jsp` (With Dynamic Manager Dropdown)

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title><c:out value="${employee != null ? 'Edit Employee' : 'Register Employee'}"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="navbar">
    <a href="${pageContext.request.contextPath}/index.jsp">🏠 Dashboard</a>
    <a href="${pageContext.request.contextPath}/managers">👥 Managers</a>
    <a href="${pageContext.request.contextPath}/employees" class="active">👔 Employees</a>
</div>

<div class="form-container">
    <h2><c:out value="${employee != null ? 'Edit Employee Details' : 'Register New Employee'}"/></h2>
    
    <form action="${pageContext.request.contextPath}/employees" method="post">
        <input type="hidden" name="id" value="${employee.id}">

        <div class="form-group">
            <label for="name">Employee Full Name *</label>
            <input type="text" id="name" name="name" value="${employee.name}" required placeholder="e.g. John Doe">
        </div>

        <div class="form-group">
            <label for="email">Email Address *</label>
            <input type="email" id="email" name="email" value="${employee.email}" required placeholder="e.g. john.doe@example.com">
        </div>

        <div class="form-group">
            <label for="designation">Designation / Role *</label>
            <input type="text" id="designation" name="designation" value="${employee.designation}" required placeholder="e.g. Senior Software Engineer">
        </div>

        <div class="form-group">
            <label for="salary">Annual Salary (USD) *</label>
            <input type="number" step="0.01" id="salary" name="salary" value="${employee.salary}" required placeholder="e.g. 85000">
        </div>

        <!-- 🔥 DYNAMIC MANAGER DROPDOWN POPULATED FROM DATABASE -->
        <div class="form-group">
            <label for="managerId">Select Reporting Manager *</label>
            <select id="managerId" name="managerId" required>
                <option value="">-- Choose a Reporting Manager --</option>
                <c:forEach var="mgr" items="${managers}">
                    <option value="${mgr.id}" 
                        <c:if test="${employee != null && employee.managerId == mgr.id}">selected="selected"</c:if>>
                        ${mgr.name} (${mgr.department})
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Save Employee</button>
            <a href="${pageContext.request.contextPath}/employees" class="btn btn-secondary">Cancel</a>
        </div>
    </form>
</div>
</body>
</html>
```

---

## Step 6: UI Styling & Dashboard Navigation

Create `web/css/styles.css`:

```css
* {
    box-sizing: border-box;
    margin: 0;
    padding: 0;
}

body {
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
    background-color: #f0f2f5;
    color: #333;
    line-height: 1.6;
}

/* Navbar */
.navbar {
    background-color: #1e293b;
    padding: 14px 28px;
    display: flex;
    gap: 20px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.navbar a {
    color: #94a3b8;
    text-decoration: none;
    font-weight: 500;
    padding: 6px 12px;
    border-radius: 6px;
    transition: all 0.2s;
}

.navbar a:hover, .navbar a.active {
    color: #ffffff;
    background-color: #334155;
}

/* Layout Containers */
.content-container {
    max-width: 1000px;
    margin: 40px auto;
    background: #ffffff;
    padding: 30px;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.form-container {
    max-width: 550px;
    margin: 40px auto;
    background: #ffffff;
    padding: 30px;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.header-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;
}

/* Data Table */
.data-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 10px;
}

.data-table th, .data-table td {
    padding: 14px 16px;
    text-align: left;
    border-bottom: 1px solid #e2e8f0;
}

.data-table th {
    background-color: #f8fafc;
    color: #475569;
    font-weight: 600;
    font-size: 13px;
    text-transform: uppercase;
}

.data-table tr:hover {
    background-color: #f8fafc;
}

/* Badges */
.badge {
    display: inline-block;
    padding: 4px 10px;
    border-radius: 20px;
    font-size: 12px;
    font-weight: 600;
}

.department-badge {
    background-color: #e0f2fe;
    color: #0369a1;
}

.manager-badge {
    background-color: #fef3c7;
    color: #92400e;
}

.unassigned-badge {
    background-color: #f1f5f9;
    color: #64748b;
}

.salary-tag {
    font-weight: 600;
    color: #166534;
}

/* Buttons */
.btn {
    display: inline-block;
    padding: 10px 18px;
    border-radius: 6px;
    font-size: 14px;
    font-weight: 500;
    text-decoration: none;
    cursor: pointer;
    border: none;
    transition: background 0.2s;
}

.btn-primary {
    background-color: #2563eb;
    color: white;
}

.btn-primary:hover {
    background-color: #1d4ed8;
}

.btn-secondary {
    background-color: #e2e8f0;
    color: #334155;
}

.btn-secondary:hover {
    background-color: #cbd5e1;
}

.btn-sm {
    padding: 6px 12px;
    font-size: 12px;
    border-radius: 4px;
    text-decoration: none;
    font-weight: 500;
    margin-right: 4px;
}

.btn-edit {
    background-color: #f3f4f6;
    color: #374151;
    border: 1px solid #d1d5db;
}

.btn-edit:hover {
    background-color: #e5e7eb;
}

.btn-delete {
    background-color: #fee2e2;
    color: #991b1b;
}

.btn-delete:hover {
    background-color: #fecaca;
}

/* Forms */
.form-group {
    margin-bottom: 18px;
}

.form-group label {
    display: block;
    margin-bottom: 6px;
    font-weight: 500;
    font-size: 14px;
    color: #334155;
}

.form-group input, .form-group select {
    width: 100%;
    padding: 10px 12px;
    border: 1px solid #cbd5e1;
    border-radius: 6px;
    font-size: 14px;
    outline: none;
}

.form-group input:focus, .form-group select:focus {
    border-color: #2563eb;
    box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.15);
}

.form-actions {
    margin-top: 24px;
    display: flex;
    gap: 12px;
}
```

---

## Troubleshooting & Common Pitfalls

| Issue / Error | Root Cause | Solution |
|---|---|---|
| **`Cannot resolve symbol 'jakarta'`** | `jakarta.servlet-api-6.0.0.jar` not added to Project Dependencies. | Go to **File -> Project Structure -> Modules -> Dependencies**, click **`+`**, and add the JAR from `web/WEB-INF/lib/`. |
| **`HTTP Status 404`** | Context path mismatch in Smart Tomcat. | In Smart Tomcat run configuration, verify **Deployment Directory** points to your `web` folder and **Context path** is `/app`. |
| **`ClassNotFoundException: com.mysql.cj.jdbc.Driver`** | MySQL JAR missing from `web/WEB-INF/lib/`. | Ensure `mysql-connector-j-8.3.0.jar` is inside `web/WEB-INF/lib/`. |
| **`No tag library found for namespace jakarta.tags.core`** | Using old `http://java.sun.com/jsp/jstl/core` on Tomcat 10/11 or missing JSTL JARs. | Use `<%@ taglib uri="jakarta.tags.core" prefix="c" %>` and ensure both JSTL JARs (`jakarta.servlet.jsp.jstl-api` and `jakarta.servlet.jsp.jstl`) are in `web/WEB-INF/lib/`. |
