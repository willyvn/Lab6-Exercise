package controller;

import dao.UserDAO;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginController extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    /**
     * Display login page
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Nếu đã login, redirect đến dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect("dashboard");
            return;
        }

        // Hiển thị trang login
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }

    /**
     * Process login form
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("remember");

        // Validate input
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {

            request.setAttribute("error", "Username and password are required");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }

        // Authenticate user
        User user = userDAO.authenticate(username, password);

        if (user != null) {
            // Authentication successful

            // Invalidate old session để tránh session fixation
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            // Tạo session mới
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole());
            session.setAttribute("fullName", user.getFullName());

            // Set timeout 30 phút
            session.setMaxInactiveInterval(30 * 60);

            // Xử lý "Remember Me" (nếu muốn, hiện chưa implement)
            if ("on".equals(rememberMe)) {
                // TODO: Implement remember me with cookie
            }

            // Redirect dựa trên role
            if ("admin".equalsIgnoreCase(user.getRole())) {
                response.sendRedirect("dashboard");
            } else {
                response.sendRedirect("student?action=list");
            }

        } else {
            // Authentication failed
            request.setAttribute("error", "Invalid username or password");
            request.setAttribute("username", username); // giữ username trong form
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }
}
