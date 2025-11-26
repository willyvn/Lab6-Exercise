<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
        <c:choose>
            <c:when test="${student != null && isNew == null}">Edit Student</c:when>
            <c:otherwise>Add New Student</c:otherwise>
        </c:choose>
    </title>
    <style>
        /* --- CSS giữ nguyên --- */
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; display: flex; justify-content: center; align-items: center; padding: 20px; }
        .container { background: white; border-radius: 10px; padding: 40px; box-shadow: 0 10px 40px rgba(0,0,0,0.2); width: 100%; max-width: 600px; }
        h1 { color: #333; margin-bottom: 30px; font-size: 28px; text-align: center; }
        .form-group { margin-bottom: 25px; }
        label { display: block; margin-bottom: 8px; color: #555; font-weight: 500; font-size: 14px; }
        input[type="text"], input[type="email"], select { width: 100%; padding: 12px 15px; border: 2px solid #ddd; border-radius: 5px; font-size: 14px; transition: border-color 0.3s; }
        input:focus, select:focus { outline: none; border-color: #667eea; }
        .required { color: #dc3545; }
        .error { color: red; font-size: 14px; display: block; margin-top: 5px; }
        .button-group { display: flex; gap: 15px; margin-top: 30px; }
        .btn { flex: 1; padding: 14px; border: none; border-radius: 5px; font-size: 16px; font-weight: 600; cursor: pointer; transition: all 0.3s; text-align: center; }
        .btn-primary { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; }
        .btn-secondary { background-color: #6c757d; color: white; }
        .info-text { font-size: 12px; color: #666; margin-top: 5px; }
    </style>
</head>
<body>
<div class="container">
    <h1>
        <c:choose>
            <c:when test="${student != null && isNew == null}">✏️ Edit Student</c:when>
            <c:otherwise>➕ Add New Student</c:otherwise>
        </c:choose>
    </h1>

    <form action="student" method="POST">
        <input type="hidden" name="action" value="${isNew != null ? 'insert' : (student != null ? 'update' : 'insert')}">
        <c:if test="${student != null && isNew == null}">
            <input type="hidden" name="id" value="${student.id}">
        </c:if>

        <div class="form-group">
            <label for="studentCode">Student Code <span class="required">*</span></label>
            <input type="text" id="studentCode" name="studentCode" value="${student.studentCode}" ${student != null && isNew == null ? "readonly" : ""} placeholder="e.g., SV001, IT123">
            <p class="info-text">Format: 2 letters + 3+ numbers</p>
            <c:if test="${not empty errorCode}"><span class="error">${errorCode}</span></c:if>
        </div>

        <div class="form-group">
            <label for="fullName">Full Name <span class="required">*</span></label>
            <input type="text" id="fullName" name="fullName" value="${student.fullName}" placeholder="Enter full name">
            <c:if test="${not empty errorName}"><span class="error">${errorName}</span></c:if>
        </div>

        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" value="${student.email}" placeholder="student@example.com">
            <c:if test="${not empty errorEmail}"><span class="error">${errorEmail}</span></c:if>
        </div>

        <div class="form-group">
            <label for="major">Major <span class="required">*</span></label>
            <select id="major" name="major">
                <option value="">-- Select Major --</option>
                <option value="Computer Science" ${student.major == 'Computer Science' ? 'selected' : ''}>Computer Science</option>
                <option value="Information Technology" ${student.major == 'Information Technology' ? 'selected' : ''}>Information Technology</option>
                <option value="Software Engineering" ${student.major == 'Software Engineering' ? 'selected' : ''}>Software Engineering</option>
                <option value="Business Administration" ${student.major == 'Business Administration' ? 'selected' : ''}>Business Administration</option>
            </select>
            <c:if test="${not empty errorMajor}"><span class="error">${errorMajor}</span></c:if>
        </div>

        <div class="button-group">
            <button type="submit" class="btn btn-primary">
                <c:choose>
                    <c:when test="${student != null && isNew == null}">💾 Update Student</c:when>
                    <c:otherwise>➕ Add Student</c:otherwise>
                </c:choose>
            </button>
            <a href="student?action=list" class="btn btn-secondary">❌ Cancel</a>
        </div>
    </form>
</div>
</body>
</html>
