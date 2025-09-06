import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;

public class CadastroDoacao extends JFrame {

    private JTextField tfNome, tfSobrenome, tfEndereco, tfTelefone;
    private JTextArea taDescricao;
    private JComboBox<String> cbTipo;
    private JButton btnCadastrar, btnSelecionarFoto;
    private File fotoFile = null;
    private ArrayList<Integer> tiposIds = new ArrayList<>();

    private final String DB_URL = "jdbc:mysql://localhost:3306/troca_treco";
    private final String DB_USER = "root";
    private final String DB_PASS = "suasenha";

    private int idUsuario = 1; // Exemplo: usuário logado

    public CadastroDoacao() {
        setTitle("Cadastro de Doação");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 1, 5, 5));

        tfNome = new JTextField(); tfNome.setEditable(false);
        tfSobrenome = new JTextField(); tfSobrenome.setEditable(false);
        tfEndereco = new JTextField(); tfEndereco.setEditable(false);
        tfTelefone = new JTextField(); tfTelefone.setEditable(false);

        taDescricao = new JTextArea(3, 20);
        cbTipo = new JComboBox<>();

        btnSelecionarFoto = new JButton("Selecionar Foto");
        btnSelecionarFoto.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            int res = fc.showOpenDialog(null);
            if(res == JFileChooser.APPROVE_OPTION) fotoFile = fc.getSelectedFile();
        });

        btnCadastrar = new JButton("Cadastrar Doação");
        btnCadastrar.addActionListener(e -> cadastrarDoacao());

        add(new JLabel("Nome:")); add(tfNome);
        add(new JLabel("Sobrenome:")); add(tfSobrenome);
        add(new JLabel("Endereço:")); add(tfEndereco);
        add(new JLabel("Telefone:")); add(tfTelefone);
        add(new JLabel("Descrição:")); add(new JScrollPane(taDescricao));
        add(new JLabel("Tipo:")); add(cbTipo);
        add(btnSelecionarFoto); add(btnCadastrar);

        carregarUsuario();
        carregarTipos();

        setVisible(true);
    }

    private void carregarUsuario(){
        try(Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)){
            String sql = "SELECT nome, sobrenome, endereco, telefone FROM usuario WHERE id_usuario=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                tfNome.setText(rs.getString("nome"));
                tfSobrenome.setText(rs.getString("sobrenome"));
                tfEndereco.setText(rs.getString("endereco"));
                tfTelefone.setText(rs.getString("telefone"));
            }
        } catch(SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar usuário");
        }
    }

    private void carregarTipos(){
        try(Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)){
            String sql = "SELECT id, descricao FROM tipo";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                tiposIds.add(rs.getInt("id"));
                cbTipo.addItem(rs.getString("descricao"));
            }
        } catch(SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar tipos");
        }
    }

    private void cadastrarDoacao(){
        String descricao = taDescricao.getText();
        int idTipo = tiposIds.get(cbTipo.getSelectedIndex());
        String status = "Disponível";

        try(Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)){
            String sql = "INSERT INTO doacao (descricao, id_tipo, status, foto, id_usuario) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, descricao);
            ps.setInt(2, idTipo);
            ps.setString(3, status);
            if(fotoFile != null){
                FileInputStream fis = new FileInputStream(fotoFile);
                ps.setBlob(4, fis);
            } else {
                ps.setNull(4, Types.BLOB);
            }
            ps.setInt(5, idUsuario);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Doação cadastrada com sucesso!");
            taDescricao.setText("");
            cbTipo.setSelectedIndex(0);
            fotoFile = null;

        } catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar doação");
        }
    }

    public static void main(String[] args) {
        new CadastroDoacao();
    }
}
