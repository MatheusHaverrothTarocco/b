import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LoginUsuario")
public class LoginUsuario extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Configuração do MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/trocatreco?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";       // seu usuário MySQL
    private static final String PASSWORD = "senha"; // sua senha MySQL

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String usuario = request.getParameter("usuario");
        String senha = request.getParameter("senha");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

            String sql = "SELECT * FROM usuarios WHERE usuario = ? AND senha = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, senha);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Se encontrou o usuário → cria sessão
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);

                response.sendRedirect("index.html"); // redireciona para a página inicial
            } else {
                out.println("<h2>Usuário ou senha inválidos!</h2>");
                out.println("<a href='Login.html'>Tentar novamente</a>");
            }

            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h2>Erro: " + e.getMessage() + "</h2>");
        }
    }
}
