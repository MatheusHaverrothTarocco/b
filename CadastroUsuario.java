import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/CadastroUsuario")
public class CadastroUsuario extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Configuração do MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/trocatreco?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";   // seu usuário do MySQL
    private static final String PASSWORD = "senha"; // sua senha do MySQL

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Pega os dados do formulário
        String nome = request.getParameter("nome");
        String sobrenome = request.getParameter("sobrenome");
        String cpf = request.getParameter("cpf");
        String endereco = request.getParameter("endereco");
        String cep = request.getParameter("cep");
        String numero = request.getParameter("numero");
        String telefone = request.getParameter("telefone");
        String usuario = request.getParameter("usuario");
        String senha = request.getParameter("senha");

        try {
            // Carrega o driver do MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Conecta no banco
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

            // Query de insert
            String sql = "INSERT INTO usuarios (nome, sobrenome, cpf, endereco, cep, numero, telefone, usuario, senha) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nome);
            ps.setString(2, sobrenome);
            ps.setString(3, cpf);
            ps.setString(4, endereco);
            ps.setString(5, cep);
            ps.setString(6, numero);
            ps.setString(7, telefone);
            ps.setString(8, usuario);
            ps.setString(9, senha);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                out.println("<h2>Usuário cadastrado com sucesso!</h2>");
                out.println("<a href='Login.html'>Ir para Login</a>");
            } else {
                out.println("<h2>Erro ao cadastrar usuário.</h2>");
            }

            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h2>Erro: " + e.getMessage() + "</h2>");
        }
    }
}
