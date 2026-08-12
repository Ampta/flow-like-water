# Master Guide: Java Servlet, JSP & JDBC (100 Q&A Handbook)

---

## Table of Contents
- [Module 1: Servlet Fundamentals & Advanced Mechanics (Q1–Q35)](#module-1-servlet-fundamentals--advanced-mechanics-q1q35)
- [Module 2: JSP (JavaServer Pages) & JSTL (Q36–Q62)](#module-2-jsp-javaserver-pages--jstl-q36q62)
- [Module 3: JDBC Architecture & Database Persistence (Q63–Q82)](#module-3-jdbc-architecture--database-persistence-q63q82)
- [Module 4: Practical Project Integration & MVC Architecture (Q83–Q92)](#module-4-practical-project-integration--mvc-architecture-q83q92)
- [Module 5: Performance Optimization, Security & Best Practices (Q93–Q100)](#module-5-performance-optimization-security--best-practices-q93q100)

---

# Module 1: Servlet Fundamentals & Advanced Mechanics (Q1–Q35)

### 1. What is a Servlet?
**Intuitive Analogy:** Think of a Servlet as a waiter at a restaurant. A customer (the web browser) places an order (HTTP Request). The waiter takes the request to the kitchen (Java code / Database), retrieves the freshly prepared meal (Data / HTML), and serves it back to the customer's table (HTTP Response).

**Technical Explanation:** 
A **Servlet** is a Java class that extends the capabilities of a server to host applications accessed via a request-response programming model. It acts as an intermediate layer between a client request (from a browser or mobile client) and backend databases/applications. Servlets run inside a **Web Container** (Servlet Engine like Apache Tomcat).

```
Client (Browser) <---> HTTP Request / Response <---> Web Container (Tomcat) <---> Servlet Class
```

**Key Points:**
- Part of the `jakarta.servlet` (formerly `javax.servlet`) package.
- Platform-independent because it runs inside the JVM.
- Efficient because it uses multithreading rather than creating heavy OS processes.

---

### 2. What is the difference between Servlet and CGI?
**CGI (Common Gateway Interface)** is an older technology used to create dynamic web pages, whereas **Servlets** are Java's solution to dynamic web applications.

| Feature | CGI (Common Gateway Interface) | Java Servlet |
| :--- | :--- | :--- |
| **Execution Model** | Process-per-request model (creates a new OS process for *every* HTTP request). | Thread-per-request model (creates a lightweight Java Thread within a single JVM process). |
| **Performance** | Slow under heavy traffic due to process creation overhead. | High performance and scalable. |
| **Resource Usage** | High CPU and RAM consumption. | Very low memory footprint (threads share memory). |
| **Platform** | Platform-dependent (written in Perl, C++, C). | Platform-independent (Runs anywhere JVM runs). |
| **Session Tracking** | Difficult to manage natively. | Built-in native Session Management API (`HttpSession`). |

---

### 3. Explain the lifecycle of a Servlet.
The Servlet lifecycle is managed entirely by the **Web Container** (e.g., Tomcat) through 3 primary lifecycle methods and 2 optional helper events:

```
[Servlet Class Loaded] 
        ↓
[Instance Created]
        ↓
   init(config)        <-- Executed ONCE when Servlet is loaded
        ↓
 service(req, res)     <-- Executed EVERY TIME a request arrives (Multi-threaded)
        ↓
     destroy()         <-- Executed ONCE when container stops or unloads Servlet
```

1. **Loading & Instantiation:** Container loads the `.class` file into memory and creates an instance using default constructor.
2. **Initialization (`init()`):** 
   - `public void init(ServletConfig config) throws ServletException`
   - Called **only once** by the container after instantiation. Used for setting up resources (e.g., DB connections, config variables).
3. **Request Handling (`service()`):** 
   - `public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException`
   - Called **on every HTTP request**. For `HttpServlet`, the `service()` method inspects the HTTP request method (GET, POST, etc.) and routes it to `doGet()`, `doPost()`, etc.
4. **Destruction (`destroy()`):** 
   - `public void destroy()`
   - Called **only once** before unloading the Servlet. Used for cleanup (closing connections, freeing memory).

---

### 4. What are the different methods of the HttpServlet class?
`HttpServlet` is an abstract class that extends `GenericServlet` and provides HTTP-specific methods. You override these methods depending on the HTTP verb used by the client:

- `doGet()`: Handles HTTP `GET` requests (fetching data).
- `doPost()`: Handles HTTP `POST` requests (submitting form data / creating resources).
- `doPut()`: Handles HTTP `PUT` requests (updating existing resources).
- `doDelete()`: Handles HTTP `DELETE` requests (removing resources).
- `doHead()`: Returns header metadata without response body.
- `doOptions()`: Returns supported HTTP methods for the target URL.
- `doTrace()`: Used for debugging HTTP request paths.

---

### 5. What are the main steps to create a Servlet?
To create and run a working Servlet:

1. **Create Java Class:** Extend `HttpServlet`.
2. **Override Request Methods:** Override `doGet()` or `doPost()`.
3. **Configure Mapping:** Map the Servlet to a URL pattern using `@WebServlet("/myurl")` annotation or `web.xml`.
4. **Compile & Package:** Package as a `.war` file or compile inside the application context.
5. **Deploy:** Run inside a Servlet container like Apache Tomcat.

```java
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/welcome")
public class WelcomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<h1>Welcome to Java Servlets!</h1>");
    }
}
```

---

### 6. What are the advantages of using Servlets?
- **Performance:** Thread-based execution consumes minimal resources.
- **Portability:** Write once, run on any operating system and Servlet container.
- **Robustness:** Benefits from Java's strong typing, garbage collection, and exception handling.
- **Security:** Integrates natively with Java EE / Jakarta security mechanisms.
- **Rich API Ecosystem:** Easy integration with databases (JDBC), messaging (JMS), and Enterprise Frameworks (Spring, Hibernate).

---

### 7. What is the difference between GenericServlet and HttpServlet?

| Feature | GenericServlet | HttpServlet |
| :--- | :--- | :--- |
| **Protocol Support** | Protocol-independent (can support HTTP, FTP, SMTP). | Protocol-dependent (Specific to HTTP / HTTPS protocol). |
| **Package** | `jakarta.servlet.GenericServlet` | `jakarta.servlet.http.HttpServlet` |
| **Key Method** | Must override `service(ServletRequest, ServletResponse)`. | Overrides protocol methods like `doGet()`, `doPost()`. |
| **Usage** | Rarely used in web applications. | Universally used for standard web development. |

---

### 8. How do you configure a Servlet in web.xml?
Before annotations (`@WebServlet`), Servlets were configured in `WEB-INF/web.xml`:

```xml
<web-app xmlns="https://jakarta.ee/xml/ns/jakartaee" version="6.0">
    <!-- 1. Define the Servlet -->
    <servlet>
        <servlet-name>MyUserServlet</servlet-name>
        <servlet-class>com.example.UserServlet</servlet-class>
    </servlet>

    <!-- 2. Map Servlet to URL Pattern -->
    <servlet-mapping>
        <servlet-name>MyUserServlet</servlet-name>
        <url-pattern>/users</url-pattern>
    </servlet-mapping>
</web-app>
```

---

### 9. What is the web.xml deployment descriptor used for?
The `web.xml` file is an XML configuration file located in `WEB-INF/` directory. It defines:
- Servlet declarations and URL mappings.
- Initialization parameters (`<init-param>`) and context parameters (`<context-param>`).
- Session timeout configurations.
- Filter declarations and Filter mappings.
- Event listener bindings (`<listener>`).
- Custom error pages (`<error-page>`).
- Security constraints and authentication roles.

---

### 10. How do you map a Servlet to a URL pattern?
There are 2 standard ways:

1. **Using Annotation (Modern & Preferred):**
   ```java
   @WebServlet(urlPatterns = {"/user", "/profile", "/account/*"})
   public class UserServlet extends HttpServlet { ... }
   ```
2. **Using web.xml (Legacy / XML-based):**
   ```xml
   <servlet-mapping>
       <servlet-name>UserServlet</servlet-name>
       <url-pattern>/user/*</url-pattern>
   </servlet-mapping>
   ```

---

### 11. What are doGet() and doPost() methods?
- **`doGet()`:** Invoked when client sends an HTTP `GET` request. 
  - Parameters are visible in the browser address bar (`?id=101&name=John`).
  - Used for retrieving data.
  - Idempotent (sending same request multiple times leaves system in same state).
  - Data payload limit is restricted by URL length limits (~2048 chars).
- **`doPost()`:** Invoked when client sends an HTTP `POST` request.
  - Parameters are sent inside HTTP Request Body (invisible in URL).
  - Used for submitting sensitive data (passwords, payment forms, creation of entities).
  - Not restricted in data size (supports image/file uploads).

---

### 12. What happens if both doGet() and doPost() are implemented in a Servlet?
If both are implemented, the Servlet container routes HTTP GET requests to `doGet()` and HTTP POST requests to `doPost()`. 

If common processing logic is required regardless of request type, one method can simply call the other:
```java
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    processRequest(req, resp);
}

@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    processRequest(req, resp);
}

private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.getWriter().println("Processing both GET and POST!");
}
```

---

### 13. When should we use doGet() and when doPost()?
- **Use `doGet()` when:**
  - Searching, filtering, or fetching content.
  - You want users to be able to bookmark the URL.
  - Content is non-sensitive.
- **Use `doPost()` when:**
  - Processing sensitive data (login forms, payment forms).
  - Creating or modifying backend database records.
  - Uploading binary files.
  - Sending large volumes of parameter data.

---

### 14. What are Servlet attributes and how do you share data between Servlets?
Servlet attributes are object key-value pairs stored in container-managed scopes. You use methods `setAttribute(name, object)`, `getAttribute(name)`, and `removeAttribute(name)`.

There are **3 primary scopes** for sharing attributes:
1. **Request Scope (`HttpServletRequest`):** Data lives only during single request-response lifecycle (e.g., passing data via `RequestDispatcher`).
2. **Session Scope (`HttpSession`):** Data persists across multiple requests made by a specific user during their session.
3. **Application Scope (`ServletContext`):** Data is shared globally across all users and all Servlets in the application.

---

### 15. What is RequestDispatcher? Explain forward() and include() methods.
`RequestDispatcher` is an interface used to dispatch a request to another resource (Servlet, JSP, or static HTML file) on the server.

- **`forward(request, response)`:**
  - Transfers request processing completely to the target resource.
  - The initial Servlet stops writing to response. Output comes entirely from target resource.
  - Browser URL **does not change** (happens server-side).
- **`include(request, response)`:**
  - Includes the response content of target resource inside current response.
  - Control returns to calling Servlet after included target finishes. Useful for common headers/footers.

```java
RequestDispatcher rd = request.getRequestDispatcher("/dashboard.jsp");
rd.forward(request, response); // or rd.include(request, response);
```

---

### 16. How to redirect from one Servlet to another Servlet?
Use `response.sendRedirect("targetURL")`.

```java
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Process login...
    response.sendRedirect("dashboard"); // Sends HTTP 302 to client browser
}
```

---

### 17. What is the difference between sendRedirect() and forward()?

```
FORWARD (Server-side):
Client ---> [Servlet A (forward)] ---> [Servlet B] ---> Output to Client

REDIRECT (Client-side):
Client ---> [Servlet A (sendRedirect)] ---> 302 Redirect Response 
Client ---> [Servlet B] ---------------> Output to Client
```

| Feature | `RequestDispatcher.forward()` | `HttpServletResponse.sendRedirect()` |
| :--- | :--- | :--- |
| **Execution Location** | Entirely on Server-side. | Client-side (Browser receives HTTP 302 status code and makes new request). |
| **Network Trips** | 1 Request / 1 Response roundtrip. | 2 Requests / 2 Responses roundtrips. |
| **Browser URL** | URL in address bar **does NOT change**. | URL in address bar **changes** to new target URL. |
| **Request Attributes** | Preserved (Request objects remain identical). | Lost (New request object is generated for second trip). |
| **Target Destination** | Can only redirect within *same web application context*. | Can redirect to *any external domain* (e.g., `https://google.com`). |

---

### 18. How can you get initialization parameters from a Servlet?
Initial parameters are configuration settings defined for a specific Servlet.

**Using Annotation:**
```java
@WebServlet(urlPatterns = "/configDemo", initParams = {
    @WebInitParam(name = "adminEmail", value = "admin@example.com")
})
public class ConfigServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = getServletConfig().getInitParameter("adminEmail");
        resp.getWriter().println("Admin Email: " + email);
    }
}
```

---

### 19. What is the role of ServletContext and ServletConfig?
- **`ServletConfig`:** An object created per Servlet instance by container during initialization (`init()`). Used to pass Servlet-specific configuration parameters.
- **`ServletContext`:** An object created **once per web application** when container starts. Represents the entire application context and allows Servlets to interact with container resources, log errors, and store application-wide global attributes.

---

### 20. What is the difference between ServletConfig and ServletContext?

| Feature | ServletConfig | ServletContext |
| :--- | :--- | :--- |
| **Scope** | One instance per Servlet class configuration. | One instance shared across entire Web Application. |
| **Creation** | Created when individual Servlet is instantiated. | Created when application is deployed / container boots up. |
| **Parameter Source** | Defined under `<servlet>` tag in `web.xml` or `@WebInitParam`. | Defined under `<context-param>` root tag in `web.xml`. |
| **Data Sharing** | Exclusive to that particular Servlet. | Shared across all Servlets, JSPs, and Filters. |

---

### 21. How do you manage session in Servlets?
HTTP is a stateless protocol (every request is treated independently). To track user identity across multiple pages, we use **Session Management**.

**Primary Methods:**
1. `HttpSession` API (Most common and powerful).
2. Cookies (`jakarta.servlet.http.Cookie`).
3. URL Rewriting (`response.encodeURL()`).
4. Hidden Form Fields (`<input type="hidden" name="sessionID" value="XYZ">`).

**HttpSession Code Example:**
```java
// Retrieve existing session or create a new one if false is not specified
HttpSession session = request.getSession(); 
session.setAttribute("user", username); // Store data

String user = (String) session.getAttribute("user"); // Fetch data
```

---

### 22. What is the difference between URL Rewriting, Cookies, and HttpSession?

- **Cookies:** Small text data sent by server and saved on user's browser disk/memory. Sent back automatically with every HTTP header request. (Can be disabled by user).
- **URL Rewriting:** Appends session ID as a request parameter to every URL string (`site.com/profile;jsessionid=1A2B3C`). Used as fallback if browser disables cookies.
- **HttpSession:** High-level Java abstraction built on top of Cookies or URL Rewriting. Keeps data on the server memory while tracking browser via a session token (`JSESSIONID`).

---

### 23. How does session tracking work internally?
1. Client makes first HTTP request to Servlet.
2. Server creates unique `HttpSession` instance and assigns a 32-character random string ID (e.g., `JSESSIONID=A1B2C3D4...`).
3. Server creates a Cookie containing `JSESSIONID` and includes it in HTTP `Set-Cookie` response header.
4. Browser stores cookie in memory and automatically attaches `Cookie: JSESSIONID=A1B2C3D4...` to subsequent HTTP request headers.
5. Server reads `JSESSIONID` header, looks up session map in JVM memory, and retrieves stored attributes.

---

### 24. How can you invalidate a session?
To log out a user or destroy session data:

```java
HttpSession session = request.getSession(false); // Do not create if doesn't exist
if (session != null) {
    session.invalidate(); // Destroys session object & unbinds stored objects
}
```

Also, set session timeouts in `web.xml`:
```xml
<session-config>
    <session-timeout>30</session-timeout> <!-- In minutes -->
</session-config>
```

---

### 25. What are cookies? How do you create and read cookies in a Servlet?
A Cookie is a small snippet of name-value data sent from server to browser.

**Creating a Cookie (Server to Browser):**
```java
Cookie userCookie = new Cookie("userTheme", "darkMode");
userCookie.setMaxAge(60 * 60 * 24); // Expires in 24 hours
response.addCookie(userCookie);
```

**Reading Cookies (Browser to Server):**
```java
Cookie[] cookies = request.getCookies();
if (cookies != null) {
    for (Cookie c : cookies) {
        if (c.getName().equals("userTheme")) {
            String theme = c.getValue();
        }
    }
}
```

---

### 26. How can you handle exceptions in Servlets?
1. **Programmatic (Try-Catch):** Catch exceptions inside `doGet()` / `doPost()` and forward to an error page.
2. **Declarative Configuration in `web.xml` (Best Practice):** Map specific Java exceptions or HTTP error status codes to custom error handler JSPs/Servlets.

```xml
<error-page>
    <error-code>404</error-code>
    <location>/error404.jsp</location>
</error-page>

<error-page>
    <exception-type>java.lang.NullPointerException</exception-type>
    <location>/errorNull.jsp</location>
</error-page>
```

---

### 27. What is the purpose of the Filter in Servlet?
A **Filter** is an object that intercepts incoming requests and outgoing responses *before* they reach a Servlet or *after* a Servlet generates a response.

**Common Use Cases:**
- Authentication & Authorization guards.
- Request payload logging & auditing.
- Data compression (GZIP).
- Input encryption / sanitization (XSS filtering).

```java
@WebFilter("/*")
public class AuthFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) 
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("user") != null) {
            chain.doFilter(req, res); // Continue to destination Servlet
        } else {
            ((HttpServletResponse) res).sendRedirect("login.html");
        }
    }
}
```

---

### 28. What are Listeners in Servlet?
Listeners are classes that react to state changes in the Servlet Container context (lifecycle events, attribute mutations, session creations).

**Key Listener Interfaces:**
- `ServletContextListener`: Triggers when application starts or stops (ideal for initializing connection pools).
- `HttpSessionListener`: Triggers when session is created or destroyed (ideal for tracking active online users count).
- `ServletRequestListener`: Triggers whenever request enters or leaves application.

```java
@WebListener
public class AppStartupListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Application Deployed / Started Successfully!");
    }
}
```

---

### 29. How can you restrict access to a Servlet?
- **Filter Interceptor:** Write a `Filter` to inspect session tokens before routing to restricted Servlet URLs.
- **Declarative Container Security (`web.xml`):** Use `<security-constraint>` with HTTP Basic/Form authentication bindings.

---

### 30. What is the difference between Filter and Servlet?

| Feature | Filter | Servlet |
| :--- | :--- | :--- |
| **Primary Role** | Pre-processing & Post-processing (Intercepting). | Core request handling & output response generation. |
| **Execution Order** | Executes **before** request reaches Servlet and **after** response leaves Servlet. | Executes **after** Filters have allowed request to pass. |
| **Chainability** | Multiple filters can be chained together (`chain.doFilter()`). | Servlet is target endpoint; only 1 Servlet generates main payload. |

---

### 31. What is the difference between SingleThreadModel and normal Servlets?
- **Normal Servlets (Default):** Container instantiates **1 Servlet object instance**, and every HTTP request creates a **new thread** accessing that single instance concurrently. High performance, but requires thread-safe code.
- **SingleThreadModel (Deprecated):** Container guarantees that only one thread at a time executes Servlet's `service()` method. Container either queues requests or maintains a pool of Servlet instances. Deprecated because it degrades performance and degrades scalability.

---

### 32. How to upload and download files using Servlets?

**File Upload (`@MultipartConfig`):**
```java
@WebServlet("/upload")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2) // 2MB limit
public class UploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        Part filePart = request.getPart("file"); // Form field <input type="file" name="file">
        String fileName = filePart.getSubmittedFileName();
        filePart.write("/uploads/" + fileName);
        response.getWriter().println("File Uploaded Successfully!");
    }
}
```

**File Download:**
```java
@WebServlet("/download")
public class DownloadServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File("/uploads/sample.pdf");
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"sample.pdf\"");
        
        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }
    }
}
```

---

### 33. How do you handle multi-threading issues in Servlets?
Because container handles requests via multiple concurrent threads accessing a shared single Servlet instance:

1. **Avoid Instance & Static Variables:** Do not store request-specific state in instance fields of Servlet class.
2. **Use Local Variables:** Variables declared inside `doGet()` or `doPost()` reside on thread stack memory and are strictly thread-safe.
3. **Synchronize Shared External Objects:** If mutating external shared resources (e.g. static maps, shared files), use thread-safe data structures (`ConcurrentHashMap`, `AtomicInteger`) or explicit synchronization blocks.

---

### 34. What are the best practices for writing Servlets?
- Never write presentation HTML code directly inside Servlets using `PrintWriter` (Delegate rendering to JSP).
- Keep Servlets thread-safe; do not use instance variables to hold request-scoped state.
- Handle exceptions gracefully using declarative error mapping in `web.xml`.
- Always close database and stream resources using `try-with-resources`.
- Use asynchronous processing for long-running execution tasks (`@WebServlet(asyncSupported = true)`).

---

### 35. How to connect a Servlet to a database using JDBC?
```java
@WebServlet("/dbDemo")
public class DbServlet extends HttpServlet {
    private static final String URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String USER = "root";
    private static final String PASS = "password";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        response.setContentType("text/html");
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                 PreparedStatement ps = conn.prepareStatement("SELECT name FROM users");
                 ResultSet rs = ps.executeQuery()) {

                PrintWriter out = response.getWriter();
                while (rs.next()) {
                    out.println("<p>User: " + rs.getString("name") + "</p>");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

# Module 2: JSP (JavaServer Pages) & JSTL (Q36–Q62)

### 36. What is JSP and why is it used?
**Intuitive Analogy:** Servlets are Java code with HTML embedded inside strings (messy). JSP is HTML code with Java embedded inside special tags (clean).

**Technical Explanation:** 
**JSP (JavaServer Pages)** is a server-side technology used to create dynamic, data-driven web pages. It lets web designers write standard HTML/CSS/JS and inject dynamic backend Java logic via special markup tags. Under the hood, the Servlet Container translates every JSP page into a Java Servlet `.java` class before compiling and executing it.

---

### 37. What are the lifecycle methods of JSP?
When a request hits a JSP for the first time:

1. **Translation:** JSP translated into Servlet source file (`filename_jsp.java`).
2. **Compilation:** Servlet compiled into `.class` bytecode.
3. **Loading & Instantiation:** Servlet loaded into JVM memory.
4. **Initialization (`jspInit()`):** Executed once when JSP loaded into memory.
5. **Request Handling (`_jspService()`):** Executed for every client request. (Notice leading underscore; never override this manually).
6. **Destruction (`jspDestroy()`):** Executed once before JSP is destroyed/unloaded.

---

### 38. Difference between JSP and Servlet?

| Feature | Servlet | JSP |
| :--- | :--- | :--- |
| **Primary Focus** | Controller logic, request processing, business rules. | Presentation layer (UI rendering). |
| **Coding Style** | Java code containing HTML string streams. | HTML code containing special JSP/Java tags. |
| **Modification Speed** | Code changes require recompilation and redeployment. | Code changes auto-compile instantly on browser refresh. |
| **MVC Role** | Acts as **Controller**. | Acts as **View**. |

---

### 39. What are JSP directives? Name and explain types.
Directives give high-level instructions to the JSP engine regarding translation and compilation. Syntax: `<%@ directive attribute="value" %>`.

Three Main Types:
1. **`page` Directive:** Configures page-wide settings (imports, session usage, error pages, language).
2. **`include` Directive:** Performs static header/footer inclusion during translation phase (`<%@ include file="header.jsp" %>`).
3. **`taglib` Directive:** Declares custom tag libraries like JSTL (`<%@ taglib uri="..." prefix="c" %>`).

---

### 40. What is the use of the `<%@ page %>` directive?
Used to import Java packages, set response content type, configure error pages, and turn session tracking on/off:

```jsp
<%@ page language="java" 
         contentType="text/html; charset=UTF-8" 
         import="java.util.List, java.util.Date" 
         errorPage="myError.jsp" 
         isThreadSafe="true" %>
```

---

### 41. What is the use of `<%@ include %>` and `<jsp:include>`?
- **Static Include (`<%@ include file="header.jsp" %>`):** Merges content of target file *during translation phase* into one single master Servlet class. Best for static fragments.
- **Dynamic Include (`<jsp:include page="header.jsp" />`):** Executes target page independently *during runtime request phase* and includes output content stream into current response.

---

### 42. What are JSP scripting elements?
Scripting elements allow raw Java code inside HTML. (Note: Modern applications avoid scriptlets in favor of EL & JSTL).

1. **Scriptlet Tag (`<% ... %>`):** Executes arbitrary Java statement inside `_jspService()`.
2. **Expression Tag (`<%= ... %>`):** Evaluates Java expression and writes result directly into response output stream. (No semicolon!).
3. **Declaration Tag (`<%! ... %>`):** Declares variables and helper methods at class level outside `_jspService()`.

---

### 43. Difference between `<%= %>`, `<%! %>`, and `<% %>` tags.

```jsp
<%-- Declaration Tag (Class Field / Method) --%>
<%! int globalCounter = 0; %> 

<%-- Scriptlet Tag (Inside _jspService method) --%>
<% 
   globalCounter++; 
   String name = "Java Developer";
%>

<%-- Expression Tag (Prints value directly to UI) --%>
<h2>Welcome <%= name %>, Visited <%= globalCounter %> times!</h2>
```

---

### 44. What are JSP implicit objects? List some of them.
Implicit objects are predefined Java objects created automatically by container inside `_jspService()` method. You can use them directly in JSP without instantiating them:

1. `request` (`HttpServletRequest`)
2. `response` (`HttpServletResponse`)
3. `session` (`HttpSession`)
4. `application` (`ServletContext`)
5. `out` (`JspWriter`)
6. `config` (`ServletConfig`)
7. `pageContext` (`PageContext`)
8. `page` (`Object` instance of current Servlet)
9. `exception` (`Throwable` - only available if `isErrorPage="true"`)

---

### 45. What is the difference between request and session implicit objects?
- **`request` Object:** Data exists only for single HTTP request roundtrip. Cleaned up immediately after response is sent back to browser.
- **`session` Object:** Data persists across multiple HTTP requests made by same browser user over time until timeout or invalidation.

---

### 46. What are JSP actions?
JSP action tags use XML syntax to control behavior of engine, include dynamic files, instantiate JavaBeans, and forward requests. Syntax: `<jsp:actionName attribute="value" />`.

---

### 47. What is the use of `<jsp:useBean>`, `<jsp:setProperty>`, and `<jsp:getProperty>`?
Used to instantiate and manipulate JavaBean objects in JSP without scriptlets:

```jsp
<!-- Instantiate Bean in session scope -->
<jsp:useBean id="user" class="com.example.UserBean" scope="session" />

<!-- Call setUser(name) -->
<jsp:setProperty name="user" property="name" value="Alice" />

<!-- Call getUser() and display -->
Welcome, <jsp:getProperty name="user" property="name" />
```

---

### 48. What is Expression Language (EL) in JSP?
Expression Language (EL) simplifies accessing Java data objects stored in request/session/application attributes without writing raw Java code. Syntax: `${expression}`.

```jsp
<!-- Scriptlet style (Old & Messy) -->
Hello, <%= ((User) request.getAttribute("user")).getName() %>

<!-- Expression Language style (Clean & Null-safe) -->
Hello, ${user.name}
```

---

### 49. How do you access request parameters and attributes in JSP?
- Access request parameter (`?id=10`): `${param.id}`
- Access request attribute (`request.setAttribute("user", obj)`): `${requestScope.user}` or simply `${user}` (EL automatically searches page -> request -> session -> application scopes in order).

---

### 50. What is JSTL? Why do we use it?
**JSTL (JSP Standard Tag Library)** provides standard structural XML tags to manage iteration, conditional logic, formatting, and database queries in JSP without using Java scriptlets.

**Advantages:**
- Zero Java code inside UI files.
- Better readability and maintainability.
- XSS prevention (Auto-escapes output).

To import JSTL Core Library:
```jsp
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
```

---

### 51. What are JSTL core tags?
Common JSTL core tags:
- `<c:out value="${data}" />`: Outputs value safely.
- `<c:set var="key" value="val" scope="session" />`: Sets variable value in scope.
- `<c:remove var="key" />`: Removes variable.
- `<c:if test="${condition}">`: Conditional execution.
- `<c:choose>`, `<c:when>`, `<c:otherwise>`: Switch-case equivalent.
- `<c:forEach var="item" items="${list}">`: Loop over collections.

---

### 52. Difference between `<c:if>` and `<c:choose>`?
- `<c:if>` handles single conditional check (No `<c:else>` tag exists!).
- `<c:choose>` handles multiple evaluation conditions (similar to `if-else if-else` or `switch` statements):

```jsp
<c:choose>
    <c:when test="${user.role == 'Admin'}">
        <p>Admin Dashboard</p>
    </c:when>
    <c:when test="${user.role == 'Manager'}">
        <p>Manager Dashboard</p>
    </c:when>
    <c:otherwise>
        <p>User Dashboard</p>
    </c:otherwise>
</c:choose>
```

---

### 53. How do you handle exceptions in JSP?
1. Declare error handling page in standard page directive (`<%@ page errorPage="error.jsp" %>`).
2. Mark handler page as exception handler (`<%@ page isErrorPage="true" %>`).

```jsp
<%-- On display.jsp --%>
<%@ page errorPage="error.jsp" %>
<% int x = 10 / 0; %>

<%-- On error.jsp --%>
<%@ page isErrorPage="true" %>
<h2>Oops! Something went wrong.</h2>
<p>Exception Details: ${pageContext.exception.message}</p>
```

---

### 54. What is the purpose of errorPage and isErrorPage attributes?
- **`errorPage="URL"`:** Specifies target JSP URL to handle exceptions if runtime failure occurs on current page.
- **`isErrorPage="true|false"`:** Enables implicit `exception` object on target page so stack trace and error messages can be read and rendered.

---

### 55. How to perform pagination in JSP using JSTL?
Calculate page offsets in Servlet controller and loop with JSTL `<c:forEach>`:

```jsp
<!-- Render records -->
<c:forEach var="prod" items="${products}">
    <tr><td>${prod.id}</td><td>${prod.name}</td></tr>
</c:forEach>

<!-- Render Pagination Links -->
<c:forEach begin="1" end="${totalPages}" var="i">
    <a href="products?page=${i}">${i}</a>
</c:forEach>
```

---

### 56. How do you include dynamic content in JSP?
Use `<jsp:include page="dynamicContent.jsp" />` action tag. It executes target page at runtime and streams output back to caller.

---

### 57. What are custom tags in JSP?
Custom tags allow developers to create user-defined domain-specific XML tags (e.g. `<myApp:formatCurrency amount="100" />`) by extending Java `SimpleTagSupport` classes and declaring tag handler definitions in `.tld` (Tag Library Descriptor) files.

---

### 58. What is the difference between static and dynamic include?

| Feature | Static Include (`<%@ include %>`) | Dynamic Include (`<jsp:include>`) |
| :--- | :--- | :--- |
| **Execution Time** | Translation Phase (Pre-compile time). | Request Processing Phase (Runtime). |
| **Output File** | Both files compiled into single shared Servlet `.class`. | Target file compiled into separate independent Servlet `.class`. |
| **Parameter Passing** | Cannot pass request parameters. | Can pass parameters using `<jsp:param>`. |
| **Performance** | Faster initial load. | Flexible and modular. |

---

### 59. What are the different scopes of JSP objects? (page, request, session, application)
1. **Page Scope:** Accessible only within specific JSP page currently being processed.
2. **Request Scope:** Accessible to any Servlet/JSP that processes current request roundtrip (via `forward`).
3. **Session Scope:** Accessible to all requests made by single user across entire browser session duration.
4. **Application Scope:** Globally accessible to all users and all pages across web application lifetime.

---

### 60. How can you access Servlet variables in JSP and vice versa?
To pass variables from Servlet to JSP, store variable inside a Scope attribute (`request`, `session`, or `application`), then dispatch request:

```java
// Inside Servlet:
request.setAttribute("message", "Data from Servlet");
request.getRequestDispatcher("page.jsp").forward(request, response);
```

```jsp
<!-- Inside JSP: -->
<p>Message: ${message}</p>
```

---

### 61. How do you prevent scriptlet code in JSP pages?
Add XML configuration inside `web.xml` to disallow scriptlets site-wide. Any build containing `<% %>` tags will throw a compilation error:

```xml
<jsp-config>
    <jsp-property-group>
        <url-pattern>*.jsp</url-pattern>
        <scripting-invalid>true</scripting-invalid>
    </jsp-property-group>
</jsp-config>
```

---

### 62. What are best practices for writing JSP pages?
- Never put database access or business domain logic inside JSP pages.
- Use JSP strictly for presentation rendering (View in MVC).
- Completely avoid scriptlets (`<% %>`). Use JSTL and Expression Language (`${}`) exclusively.
- Use dynamic includes (`<jsp:include>`) for reusable template layouts (headers, navigation, footers).
- Disable scriptlets via `web.xml`.

---

# Module 3: JDBC Architecture & Database Persistence (Q63–Q82)

### 63. What is JDBC?
**JDBC (Java Database Connectivity)** is an API specification provided in Java standard library (`java.sql` package) that defines standard interfaces to connect Java applications with relational databases (MySQL, PostgreSQL, Oracle, SQL Server), execute SQL statements, and process query results.

---

### 64. What are the steps to connect Java with a database using JDBC?
5 Standard Steps:

1. **Load JDBC Driver Class:** `Class.forName("com.mysql.cj.jdbc.Driver");`
2. **Establish Connection:** `Connection conn = DriverManager.getConnection(url, user, password);`
3. **Create Statement:** `PreparedStatement ps = conn.prepareStatement("SELECT * FROM users");`
4. **Execute Query / Update:** `ResultSet rs = ps.executeQuery();`
5. **Close Resources:** Close `ResultSet`, `PreparedStatement`, and `Connection`.

---

### 65. What are the types of JDBC drivers?
1. **Type 1 (JDBC-ODBC Bridge):** Translates JDBC calls to ODBC calls. Native to Windows. (Obsolete & Removed in Java 8).
2. **Type 2 (Native-API Driver):** Converts JDBC calls into native C/C++ database client library calls. Requires client-side installation.
3. **Type 3 (Network Protocol Driver):** Middleware driver that translates JDBC calls into socket protocol converted by application server.
4. **Type 4 (Pure Java / Thin Driver - Recommended):** Directly converts JDBC calls into database-native network wire protocol. Fast, portable, requires no client-side setup.

---

### 66. Which JDBC driver is the fastest?
**Type 4 (Pure Java Thin Driver)** is the fastest because it communicates directly with database using native socket calls without needing C/C++ translation layers, middleware servers, or ODBC bridges.

---

### 67. What is DriverManager?
`DriverManager` is a factory helper class in `java.sql` package that manages loaded JDBC drivers and creates database `Connection` objects based on registered database connection URLs (`jdbc:mysql://...`).

---

### 68. What are Connection, Statement, and ResultSet objects?
- **`Connection`:** Interface representing an active session bridge with specific database.
- **`Statement`:** Interface used to execute basic static SQL queries against database.
- **`ResultSet`:** Interface representing tabular data stream returned by SQL query (acts like a database cursor pointing before first row).

---

### 69. Difference between Statement and PreparedStatement.

| Feature | Statement | PreparedStatement |
| :--- | :--- | :--- |
| **Pre-compilation** | SQL compiled by database on *every* execution. | SQL compiled once by database engine and cached for reuse. |
| **Performance** | Slow for repeated queries. | Very fast due to query caching. |
| **SQL Injection** | **Vulnerable** to SQL Injection attacks. | **Secure** against SQL Injection via parameterized placeholders (`?`). |
| **Binary Data** | Cannot easily process images/files. | Supports streams for BLOB / CLOB handling. |

---

### 70. What are the advantages of PreparedStatement?
- **Security:** Parameter placeholders eliminate SQL Injection vulnerabilities.
- **Performance:** Database compiles SQL query structure once; subsequent calls pass only input parameters.
- **Readability:** Clean, clean parameter bindings (`ps.setInt(1, 101);`).

---

### 71. What is CallableStatement?
`CallableStatement` is an interface extending `PreparedStatement` used to execute **Database Stored Procedures** and functions containing `IN`, `OUT`, and `INOUT` parameters.

---

### 72. How to execute stored procedures using JDBC?
```java
String sql = "{call get_user_balance(?, ?)}";
try (CallableStatement cs = conn.prepareCall(sql)) {
    cs.setInt(1, 101); // IN parameter
    cs.registerOutParameter(2, java.sql.Types.DOUBLE); // OUT parameter
    
    cs.execute();
    double balance = cs.getDouble(2);
}
```

---

### 73. How to prevent SQL Injection in JDBC?
**SQL Injection** occurs when malicious user input modifies SQL query logic (e.g. `' OR '1'='1`).

**Prevention:** Always use `PreparedStatement` with parameterized placeholders (`?`). **Never** concatenate strings into SQL queries!

```java
// BAD (Vulnerable to injection):
String sql = "SELECT * FROM users WHERE user = '" + input + "'";

// GOOD (Secure):
String sql = "SELECT * FROM users WHERE user = ?";
PreparedStatement ps = conn.prepareStatement(sql);
ps.setString(1, input);
```

---

### 74. How to perform batch updates in JDBC?
Batch processing groups multiple SQL statements into a single network execution payload to reduce database roundtrip latency:

```java
conn.setAutoCommit(false);
PreparedStatement ps = conn.prepareStatement("INSERT INTO logs (message) VALUES (?)");

for (String msg : logList) {
    ps.setString(1, msg);
    ps.addBatch(); // Add query to batch list
}

int[] results = ps.executeBatch(); // Send all queries in one network trip
conn.commit();
```

---

### 75. How do you handle database transactions in JDBC?
A transaction is a unit of work that must satisfy ACID properties (All-or-Nothing execution).

```java
try {
    conn.setAutoCommit(false); // 1. Start transaction boundary
    
    // Execute SQL Operations
    ps1.executeUpdate();
    ps2.executeUpdate();
    
    conn.commit(); // 2. Commit transaction changes
} catch (Exception e) {
    conn.rollback(); // 3. Rollback all changes if any error occurs
}
```

---

### 76. What is auto-commit mode in JDBC?
By default, newly created database connections operate in `autoCommit = true` mode, meaning every single SQL statement is automatically committed to database immediately upon execution. Set `conn.setAutoCommit(false)` to manage multi-step transactions manually.

---

### 77. How do you roll back a transaction in JDBC?
Invoke `conn.rollback()` inside a `catch` block to undo all database modifications performed since `conn.setAutoCommit(false)` was called.

---

### 78. How to handle database connection pooling in web applications?
Opening and closing raw database connections for every HTTP request is slow and resource-heavy. 

**Connection Pooling** maintains a pool of pre-created, reusable database connections in memory. When a Servlet needs a connection, it borrows one from pool, executes queries, and returns connection back to pool upon completion. (Popular connection pools: HikariCP, Apache DBCP, Tomcat JDBC Pool).

---

### 79. What is DataSource in JDBC?
`DataSource` is a standard factory interface in `javax.sql` package for obtaining database connections. Unlike `DriverManager`, `DataSource` supports connection pooling, distributed transactions, and JNDI lookup natively in Enterprise web servers.

---

### 80. How can you display database data on a JSP page using a Servlet controller?

```java
// Servlet (Controller):
List<User> userList = userDAO.getAllUsers();
request.setAttribute("users", userList);
request.getRequestDispatcher("userList.jsp").forward(request, response);
```

```jsp
<!-- JSP (View): -->
<c:forEach var="u" items="${users}">
    <p>ID: ${u.id} | Name: ${u.name}</p>
</c:forEach>
```

---

### 81. How to pass form data from JSP to Servlet and insert into a database?

```html
<!-- HTML Form on JSP -->
<form action="addUser" method="POST">
    <input type="text" name="username" required />
    <button type="submit">Save</button>
</form>
```

```java
// Servlet receiving form data
@WebServlet("/addUser")
public class AddUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("username");
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO users(name) VALUES(?)")) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch(Exception e) { e.printStackTrace(); }
        
        resp.sendRedirect("userList.jsp");
    }
}
```

---

### 82. How to handle exceptions and close JDBC resources properly?
Always use **`try-with-resources`** block (introduced in Java 7). It automatically closes `Connection`, `PreparedStatement`, and `ResultSet` objects in reverse order of creation even if an exception occurs:

```java
try (Connection conn = dataSource.getConnection();
     PreparedStatement ps = conn.prepareStatement("SELECT * FROM items");
     ResultSet rs = ps.executeQuery()) {
     
    while(rs.next()) {
        // process data
    }
} catch (SQLException e) {
    // Log exception
}
```

---

# Module 4: Practical Project Integration & MVC Architecture (Q83–Q92)

### 83. Explain the flow: JSP → Servlet → JDBC → Database → JSP.

```
[1. User submits form on JSP Page]
             ↓
[2. HTTP Request sent to Servlet Controller]
             ↓
[3. Servlet invokes DAO layer]
             ↓
[4. DAO executes SQL query via JDBC]
             ↓
[5. Database returns record dataset]
             ↓
[6. Servlet attaches record list to request attribute]
             ↓
[7. Request forwarded to JSP View page]
             ↓
[8. JSP renders HTML via JSTL & returns to Browser]
```

---

### 84. How do you perform CRUD operations using JSP, Servlet, and JDBC?
CRUD stands for **Create, Read, Update, Delete**:
- **Create:** Form POST -> Servlet -> `INSERT INTO table` via JDBC -> Redirect to list.
- **Read:** Request GET -> Servlet -> `SELECT * FROM table` -> Pass List to JSP -> Render via `<c:forEach>`.
- **Update:** Form POST -> Servlet -> `UPDATE table SET ... WHERE id=?` -> Redirect.
- **Delete:** Request GET/POST -> Servlet -> `DELETE FROM table WHERE id=?` -> Redirect.

---

### 85. How can you validate a login using JSP & Servlet with JDBC?
1. JSP login form submits `username` & `password` via POST to `LoginServlet`.
2. `LoginServlet` queries database: `SELECT * FROM users WHERE username=? AND password=?`.
3. If user record exists:
   - Create session: `HttpSession session = request.getSession();`
   - Store user: `session.setAttribute("currentUser", user);`
   - Redirect to dashboard.
4. If authentication fails:
   - Set error message: `request.setAttribute("error", "Invalid Credentials!");`
   - Forward back to `login.jsp`.

---

### 86. How do you display data from a database table in a JSP using JSTL `<c:forEach>`?
```jsp
<table border="1">
    <tr><th>ID</th><th>Username</th><th>Email</th></tr>
    <c:forEach var="user" items="${userList}">
        <tr>
            <td>${user.id}</td>
            <td>${user.name}</td>
            <td>${user.email}</td>
        </tr>
    </c:forEach>
</table>
```

---

### 87. What is MVC architecture in JSP/Servlet application?
**MVC (Model-View-Controller)** is a software design pattern that separates application responsibilities into 3 layers:

- **Model (JavaBeans / Data Access Objects):** Holds data structures and business/database logic.
- **View (JSP Pages / HTML):** Displays UI data to user. Contains zero business logic.
- **Controller (Servlets):** Acts as coordinator. Receives request, calls Model for database operations, and selects appropriate View page for display.

---

### 88. How to upload a file and store file name in DB using JSP/Servlet?
1. JSP Form must include `enctype="multipart/form-data"`.
2. Servlet handles upload using `@MultipartConfig` and extracts file:
   ```java
   Part filePart = request.getPart("image");
   String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
   filePart.write("/uploads/" + fileName);
   ```
3. Store path string in database: `INSERT INTO files(file_name) VALUES(?)`.

---

### 89. How do you handle form validations on JSP & Servlet side?
- **Client-side (JSP / JavaScript):** HTML5 attributes (`required`, `type="email"`, `pattern="..."`) provide instant browser feedback. (Can be bypassed by malicious users).
- **Server-side (Servlet - MANDATORY for Security):** Inspect parameter strings inside Servlet (`if (name == null || name.trim().isEmpty())`). If invalid, attach error string to request and forward back to form.

---

### 90. How do you maintain user session after login using Servlet and JSP?
- Upon login success, store user object in session: `request.getSession().setAttribute("user", userObj);`
- On protected JSP/Servlets, check session presence:
  ```jsp
  <c:if test="${empty sessionScope.user}">
      <c:redirect url="login.jsp"/>
  </c:if>
  ```

---

### 91. How do you secure JSP pages from unauthorized access?
- Place internal JSP pages inside the **`WEB-INF/`** folder!
- Files stored inside `WEB-INF/` cannot be accessed directly by typing URL in client browser. They can only be accessed internally when forwarded by a Servlet controller (`request.getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response)`).

---

### 92. How do you show error messages dynamically on JSP?
```jsp
<%-- Servlet sets: request.setAttribute("errorMessage", "Password too short!"); --%>
<c:if test="${not empty errorMessage}">
    <div style="color: red; padding: 10px; border: 1px solid red;">
        ${errorMessage}
    </div>
</c:if>
```

---

# Module 5: Performance Optimization, Security & Best Practices (Q93–Q100)

### 93. Why should business logic not be written in JSP?
- Hard to debug and write unit tests for.
- Violates MVC Separation of Concerns.
- Makes code unreadable and unmaintainable.
- Causes security vulnerabilities and performance bottlenecks.

---

### 94. How to reduce database load in a web application?
- Use **Connection Pooling** (e.g. HikariCP).
- Implement **Caching** (Redis or Ehcache) for frequently read, static data.
- Optimize SQL queries using indexes.
- Fetch only required columns (`SELECT id, name` instead of `SELECT *`).
- Use JDBC **Batching** for mass update operations.

---

### 95. Why should database connections be closed in finally block?
Database connections are scarce OS sockets and server memory resources. If connections are not explicitly closed after use, they remain open (Resource Leak). Eventually, database runs out of available connection handles and crashes (`Too many connections` error). 

Using `try-with-resources` automatically ensures resource cleanup.

---

### 96. How to implement connection pooling for performance improvement?
In Apache Tomcat, configure connection pool resource in `META-INF/context.xml`:

```xml
<Context>
    <Resource name="jdbc/TestDB" auth="Container" type="javax.sql.DataSource"
              maxTotal="100" maxIdle="30" maxWaitMillis="10000"
              username="root" password="password" 
              driverClassName="com.mysql.cj.jdbc.Driver"
              url="jdbc:mysql://localhost:3306/mydb"/>
</Context>
```

In Servlet, lookup via JNDI:
```java
InitialContext ctx = new InitialContext();
DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/TestDB");
Connection conn = ds.getConnection();
```

---

### 97. How to separate UI, Business, and DB logic in JSP/Servlet architecture?
Divide application code into 3 strict architectural tiers:

1. **Presentation Layer (UI):** JSP pages + CSS/JS (Renders data via JSTL/EL).
2. **Control & Service Layer:** Servlets (Parses HTTP requests) + Service Classes (Business validation algorithms).
3. **Data Access Layer (DAO):** DAO Classes handling raw SQL / JDBC executions.

---

### 98. How to use MVC pattern in JSP-Servlet applications?
- **Request Flow:** Browser -> Servlet Controller -> Service / DAO -> Servlet updates Request Attribute -> Forwards to JSP View -> Browser.
- Never let JSP communicate directly with Database or DAO classes!

---

### 99. What are some common exceptions in Servlets/JDBC and how to handle them?
- **`ClassNotFoundException`:** JDBC driver jar missing from `WEB-INF/lib` folder.
- **`SQLException`:** Incorrect SQL syntax, invalid database credentials, or deadlocks.
- **`NullPointerException`:** Trying to invoke methods on null request attributes or uninitialized sessions.
- **`IllegalStateException`:** Occurs when trying to call `response.sendRedirect()` or `forward()` *after* response buffer has already been committed to client.

---

### 100. How do you deploy your JSP/Servlet + JDBC application in Tomcat?
1. Build application into a Web Application Archive file (**`.war`**).
2. Ensure database driver `.jar` (e.g. `mysql-connector-j.jar`) is inside `WEB-INF/lib/` folder.
3. Copy `.war` file to Tomcat's **`webapps/`** directory.
4. Start Tomcat server via `bin/startup.sh` (Mac/Linux) or `bin/startup.bat` (Windows).
5. Open browser and access app at `http://localhost:8080/YourAppName/`.
