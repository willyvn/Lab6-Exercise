<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student List - MVC</title>
    <style>
        /* --- Giữ nguyên CSS của bạn --- */
        * { margin:0; padding:0; box-sizing:border-box; }
        body { font-family:'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg,#667eea 0%,#764ba2 100%); min-height:100vh; padding:20px; }
        .container { max-width:1200px; margin:0 auto; background:white; border-radius:10px; padding:30px; box-shadow:0 10px 40px rgba(0,0,0,0.2); }
        h1 { color:#333; margin-bottom:10px; font-size:32px; }
        .subtitle { color:#666; margin-bottom:30px; font-style:italic; }

        /* Navbar */
        .navbar { 
            background:white; 
            padding:15px 20px; 
            border-radius:8px; 
            box-shadow:0 4px 10px rgba(0,0,0,0.1); 
            display:flex; 
            justify-content:space-between; 
            align-items:center; 
            margin-bottom:25px;
        }
        .navbar-right a { margin-left:15px; text-decoration:none; color:#333; font-weight:600; }
        .role-badge { 
            padding:4px 10px; 
            border-radius:5px; 
            font-size:12px; 
            margin-left:8px;
            text-transform:uppercase;
            font-weight:bold;
            color:white;
        }
        .role-admin { background:#dc3545; }
        .role-user { background:#6c757d; }

        .message { padding:15px; margin-bottom:20px; border-radius:5px; font-weight:500; }
        .success { background-color:#d4edda; color:#155724; border:1px solid #c3e6cb; }
        .error { background-color:#f8d7da; color:#721c24; border:1px solid #f5c6cb; }
        .btn { display:inline-block; padding:12px 24px; text-decoration:none; border-radius:5px; font-weight:500; transition:all 0.3s; border:none; cursor:pointer; font-size:14px; }
        .btn-primary { background: linear-gradient(135deg,#667eea 0%,#764ba2 100%); color:white; }
        .btn-secondary { background-color:#6c757d; color:white; }
        .btn-danger { background-color:#dc3545; color:white; padding:8px 16px; font-size:13px; }
        table { width:100%; border-collapse:collapse; margin-top:20px; }
        thead { background: linear-gradient(135deg,#667eea 0%,#764ba2 100%); color:white; }
        th, td { padding:15px; text-align:left; border-bottom:1px solid #ddd; }
        tbody tr:hover { background-color:#f8f9fa; }
        .actions { display:flex; gap:10px; }
        .empty-state { text-align:center; padding:60px 20px; color:#999; }
        .empty-state-icon { font-size:64px; margin-bottom:20px; }
        .search-container { margin-bottom:20px; display:flex; gap:10px; flex-wrap:wrap; }
        .search-container input[type=text], .search-container select { padding:10px; border-radius:5px; border:1px solid #ccc; }
        .search-container button { padding:10px 20px; border-radius:5px; border:none; background-color:#667eea; color:white; cursor:pointer; }
    </style>
</head>
<body>

<!-- TODO 1: Navigation bar -->
<div class="navbar">
    <h2>📚 Student Management System</h2>
    <div class="navbar-right">
        <span>Welcome, ${sessionScope.fullName}</span>
        <span class="role-badge role-${sessionScope.role}">
            ${sessionScope.role}
        </span>
        <a href="dashboard">Dashboard</a>
        <a href="logout">Logout</a>
    </div>
</div>

<div class="container">

    <h1>📚 Student List</h1>
    <p class="subtitle">MVC Pattern with Jakarta EE & JSTL</p>

    <!-- TODO 2: Show error from URL parameter (AdminFilter) -->
    <c:if test="${not empty param.error}">
        <div class="message error">❌ ${param.error}</div>
    </c:if>

    <c:if test="${not empty param.message}">
        <div class="message success">✅ ${param.message}</div>
    </c:if>

    <!-- TODO 3: Add Student button (admin only) -->
    <c:if test="${sessionScope.role eq 'admin'}">
        <div style="margin-bottom:20px;">
            <a href="student?action=new" class="btn btn-primary">➕ Add New Student</a>
        </div>
    </c:if>

    <!-- Search + Filter + Clear -->
    <div class="search-container">
        <form action="student" method="get">
            <input type="hidden" name="action" value="search">
            <input type="text" name="keyword" placeholder="Search by name or email" value="${searchKeyword}">
            <button type="submit">🔍 Search</button>
        </form>

        <form action="student" method="get">
            <input type="hidden" name="action" value="filter">
            <select name="major">
                <option value="">-- Filter by Major --</option>
                <option value="Computer Science" ${selectedMajor=='Computer Science'?'selected':''}>Computer Science</option>
                <option value="Information Technology" ${selectedMajor=='Information Technology'?'selected':''}>Information Technology</option>
                <option value="Software Engineering" ${selectedMajor=='Software Engineering'?'selected':''}>Software Engineering</option>
                <option value="Business Administration" ${selectedMajor=='Business Administration'?'selected':''}>Business Administration</option>
            </select>
            <input type="hidden" name="sortBy" value="${sortBy}">
            <input type="hidden" name="sortOrder" value="${sortOrder}">
            <button type="submit">⚙️ Filter</button>
        </form>

        <form action="student" method="get">
            <input type="hidden" name="action" value="list">
            <button type="submit">♻️ Clear Filter</button>
        </form>
    </div>

    <c:choose>
        <c:when test="${not empty students}">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Code</th>
                        <th>Full Name</th>
                        <th>Email</th>
                        <th>Major</th>

                        <!-- TODO 4: Add "Actions" header for admin only -->
                        <c:if test="${sessionScope.role eq 'admin'}">
                            <th>Actions</th>
                        </c:if>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="student" items="${students}">
                        <tr>
                            <td>${student.id}</td>
                            <td><strong>${student.studentCode}</strong></td>
                            <td>${student.fullName}</td>
                            <td>${student.email}</td>
                            <td>${student.major}</td>

                            <!-- TODO 5: Actions only for admin -->
                            <c:if test="${sessionScope.role eq 'admin'}">
                                <td class="actions">
                                    <a href="student?action=edit&id=${student.id}" class="btn btn-secondary">✏️ Edit</a>
                                    <a href="student?action=delete&id=${student.id}" class="btn btn-danger"
                                       onclick="return confirm('Are you sure you want to delete this student?')">🗑️ Delete</a>
                                </td>
                            </c:if>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <div class="empty-state">
                <div class="empty-state-icon">📭</div>
                <h3>No students found</h3>
                <p>Start by adding a new student</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>
