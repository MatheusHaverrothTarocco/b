import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Scanner;

public class CadastroDoacao {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/troca_treco";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "suasenha";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Scanner sc = new Scanner(System.in)) {

            int idUsuario = 1; // Exemplo: usuário logado

            // Mostrar dados do usuário
            String sqlUser = "SELECT nome, sobrenome, endereco, telefone FROM usuario WHERE id_usuario=?";
            PreparedStatement psUser = conn.prepareStatement(sqlUser);
            psUser.setInt(1, idUsuario);
            ResultSet rsUser = psUser.executeQuery();
            if (rsUser.next()) {
                System.out.println("Usuário: " + rsUser.getString("nome") + " " + rsUser.getString("sobrenome"));
                System.out.println("Endereço: " + rsUser.getString("endereco"));
                System.out.println("Telefone: " + rsUser.getString("telefone"));
            }

            // Mostrar tipos de doação
            String sqlTipo = "SELECT id, descricao FROM tipo";
            ResultSet rsTipo = conn.createStatement().executeQuery(sqlTipo);
            System.out.println("Tipos disponíveis:");
            while(rsTipo.next()){
                System.out.println(rsTipo.getInt("id") + " - " + rsTipo.getString("descricao"));
            }

            // Receber entrada do usuário
            System.out.print("Descrição da doação: ");
            String descricao = sc.nextLine();

            System.out.print("Escolha o ID do tipo: ");
            int idTipo = Integer.parseInt(sc.nextLine());

            System.out.print("Caminho da foto (ou vazio): ");
            String caminhoFoto = sc.nextLine();
            FileInputStream foto = null;
            if(!caminhoFoto.isEmpty()){
                foto = new FileInputStream(new File(caminhoFoto));
            }

            // Inserir doação
            String sqlInsert = "INSERT INTO doacao (descricao, id_tipo, status, foto, id_usuario) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
            psInsert.setString(1, descricao);
            psInsert.setInt(2, idTipo);
            psInsert.setString(3, "Disponível");
            if(foto != null){
                psInsert.setBlob(4, foto);
            } else {
                psInsert.setNull(4, Types.BLOB);
            }
            psInsert.setInt(5, idUsuario);

            psInsert.executeUpdate();
            System.out.println("Doação cadastrada com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
