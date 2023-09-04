/* 
    Autor: Alarcon ABAP
*/

package cadastro.model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConectorBD {

    // Obtém uma conexão com o banco de dados
    public Connection getConnection() throws SQLException {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=Loja;encrypt=true;trustServerCertificate=true";
        String user = "sa";
        String password = "sarava";
        return DriverManager.getConnection(url, user, password);
    }

    // Cria um PreparedStatement para executar uma consulta parametrizada
    public PreparedStatement getPrepared(String sql) throws SQLException {
        Connection connection = getConnection();
        return connection.prepareStatement(sql);
    }

    // Executa uma consulta SELECT e retorna o resultado como um ResultSet
    public ResultSet getSelect(String sql) throws SQLException {
        Statement statement = getConnection().createStatement();
        return statement.executeQuery(sql);
    }

    // Fecha um objeto Statement
    public void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
        }
    }

    // Fecha um objeto ResultSet
    public void close(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
        }
    }

    // Fecha uma conexão com o banco de dados
    public void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
        }
    }

        Statement getStatement() {
         return null; // ou return new Statement();
     }

}
