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

@WebServlet("/CadastroDoacao")
public class CadastroDoacao extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Configuração do banco
    private static final String URL = "jdbc:mysql://localhost:3306/trocatreco";
    private static final String USER = "root";
    private static final String PASSWORD = "senha";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Pega os parâmetros do formulário
        String nome = request.getParameter("nome");
        String sobrenome = request.getParameter("sobrenome");
        String cpf = request.getParameter("cpf");
        String endereco = request.getParameter("endereco");
        String cep = request.getParameter("cep");
        String numero = request.getParameter("numero");
        String telefone = request.getParameter("telefone");
        String usuario = request.getParameter("usuario");
        String senha = request.getParameter("senha");
        String descricao = request.getParameter("descricao");
        String tipo = request.getParameter("tipo");
        String status = request.getParameter("status");

        try {
            // Conecta no banco
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

            // Exemplo simples: inserir em uma tabela unica (ajuste conforme seu banco)
            String sql = "INSERT INTO doacoes (nome, sobrenome, cpf, endereco, cep, numero, telefone, usuario, senha, descricao, tipo, status) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
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
            ps.setString(10, descricao);
            ps.setString(11, tipo);
            ps.setString(12, status);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                out.println("<h2>Cadastro realizado com sucesso!</h2>");
            } else {
                out.println("<h2>Erro ao cadastrar.</h2>");
            }

            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h2>Erro: " + e.getMessage() + "</h2>");
        }
    }
}
