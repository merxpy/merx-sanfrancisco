<%-- 
    Document   : httpSession
    Created on : 24/02/2017, 08:26:22 PM
    Author     : Akio
--%>

<%
    HttpSession sesion = request.getSession(false);
    String usuario = String.valueOf(sesion.getAttribute("user"));
    int interval = 1000;
    sesion.setMaxInactiveInterval(interval);
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
    response.setDateHeader("Expires", 0);
    if (usuario.equals("null")) {
        response.sendRedirect("login.jsp");
    }
%>