
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProdutosDAO {

    Connection conn;
    PreparedStatement prep;
    ResultSet resultset;
    ArrayList<ProdutosDTO> listagem = new ArrayList<>();

    public void cadastrarProduto(ProdutosDTO produto) {
        conectaDAO dao = new conectaDAO();
        Connection conn = dao.connectDB();

        String sql = "INSERT INTO produtos (nome, valor, status) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, produto.getNome());
            statement.setInt(2, produto.getValor());
            statement.setString(3, produto.getStatus());

            statement.executeUpdate();  // Executa a operação de inserção
            JOptionPane.showMessageDialog(null, "Produto cadastrado com sucesso");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar o produto.  (" + e.getMessage() + ")");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar a conexão com o banco de dados: " + e.getMessage());
                }
            }
        }
    }

    public void venderProduto(int id) {
        conectaDAO dao = new conectaDAO();
        Connection conn = dao.connectDB();

        String sql = "UPDATE produtos SET status = 'Vendido' WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, id);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Produto vendido com sucesso");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao vender o produto. (" + e.getMessage() + ")");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar a conexão com o banco de dados: " + e.getMessage());
                }
            }
        }
    }

    public List<ProdutosDTO> listarProdutos() {
        List<ProdutosDTO> produtos = new ArrayList<>();

        Connection conn = null;
        try {
            conn = new conectaDAO().connectDB();

            String sql = "SELECT * FROM produtos";

            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String nome = resultSet.getString("nome");
                        int valor = resultSet.getInt("valor");
                        String status = resultSet.getString("status");

                        ProdutosDTO produto = new ProdutosDTO(id, nome, valor, status);
                        produtos.add(produto);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos: " + e.getMessage());
        } finally {

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar a conexão com o banco de dados: " + e.getMessage());
                }
            }
        }

        return produtos;
    }

}
