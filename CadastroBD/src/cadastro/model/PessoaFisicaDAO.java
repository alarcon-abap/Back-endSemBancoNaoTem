/* 
    Autor: Alarcon ABAP
*/

package cadastro.model;
import java.sql.Statement;
import cadastrobd.model.PessoaFisica;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PessoaFisicaDAO {
    
    private final ConectorBD conectorBD;

    public PessoaFisicaDAO() {
        this.conectorBD = new ConectorBD();
    }

    // Método para obter uma PessoaFisica pelo ID
    public PessoaFisica getPessoa(int id) {
        PessoaFisica pessoaFisica = null;
        Connection connection = null;
        PreparedStatement statementPessoa = null;
        PreparedStatement statementPessoaFisica = null;
        ResultSet resultSetPessoa = null;
        ResultSet resultSetPessoaFisica = null;

        try {
            connection = conectorBD.getConnection();
            
            // Consultar a tabela Pessoa
            String sqlPessoa = "SELECT * FROM Pessoa WHERE idPessoa = ?";
            statementPessoa = connection.prepareStatement(sqlPessoa);
            statementPessoa.setInt(1, id);
            resultSetPessoa = statementPessoa.executeQuery();

            if (resultSetPessoa.next()) {
                pessoaFisica = new PessoaFisica();
                pessoaFisica.setId(resultSetPessoa.getInt("idPessoa"));
                pessoaFisica.setNome(resultSetPessoa.getString("nome"));
                pessoaFisica.setLogradouro(resultSetPessoa.getString("logradouro"));
                pessoaFisica.setCidade(resultSetPessoa.getString("cidade"));
                pessoaFisica.setEstado(resultSetPessoa.getString("estado"));
                pessoaFisica.setTelefone(resultSetPessoa.getString("telefone"));
                pessoaFisica.setEmail(resultSetPessoa.getString("email"));
            }

            // Consultar a tabela PessoaFisica
            String sqlPessoaFisica = "SELECT * FROM PessoaFisica WHERE idPessoa = ?";
            statementPessoaFisica = connection.prepareStatement(sqlPessoaFisica);
            statementPessoaFisica.setInt(1, id);
            resultSetPessoaFisica = statementPessoaFisica.executeQuery();

            if (resultSetPessoaFisica.next()) {
                pessoaFisica.setCpf(resultSetPessoaFisica.getString("CPF"));
                // Definir outros atributos específicos da tabela PessoaFisica, se houver
            }

        } catch (SQLException e) {
            // Tratar exceção, se necessário
        } finally {
            conectorBD.close(resultSetPessoaFisica);
            conectorBD.close(statementPessoaFisica);
            conectorBD.close(resultSetPessoa);
            conectorBD.close(statementPessoa);
            conectorBD.close(connection);
        }

        return pessoaFisica;
    }

    // Método para listar todas as pessoas físicas
    public List<PessoaFisica> listarTodasPessoasFisicas() {
        List<PessoaFisica> pessoasFisicas = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statementPessoa = null;
        PreparedStatement statementPessoaFisica = null;
        ResultSet resultSetPessoa = null;
        ResultSet resultSetPessoaFisica = null;

        try {
            connection = conectorBD.getConnection();
            
            // Consultar a tabela Pessoa
            String sqlPessoa = "SELECT * FROM PessoaFisica";
            statementPessoa = connection.prepareStatement(sqlPessoa);
            resultSetPessoa = statementPessoa.executeQuery();

            while (resultSetPessoa.next()) {
                PessoaFisica pessoaFisica = new PessoaFisica();
                pessoaFisica.setId(resultSetPessoa.getInt("idPessoa"));
                pessoaFisica.setNome(resultSetPessoa.getString("nome"));
                pessoaFisica.setLogradouro(resultSetPessoa.getString("logradouro"));
                pessoaFisica.setCidade(resultSetPessoa.getString("cidade"));
                pessoaFisica.setEstado(resultSetPessoa.getString("estado"));
                pessoaFisica.setTelefone(resultSetPessoa.getString("telefone"));
                pessoaFisica.setEmail(resultSetPessoa.getString("email"));
                
                // Consultar a tabela PessoaFisica para buscar o CPF, se houver
                String sqlPessoaFisica = "SELECT * FROM PessoaFisica WHERE idPessoa = ?";
                statementPessoaFisica = connection.prepareStatement(sqlPessoaFisica);
                statementPessoaFisica.setInt(1, pessoaFisica.getId());
                resultSetPessoaFisica = statementPessoaFisica.executeQuery();

                if (resultSetPessoaFisica.next()) {
                    pessoaFisica.setCpf(resultSetPessoaFisica.getString("CPF"));
                    // Definir outros atributos específicos da tabela PessoaFisica, se houver
                }
                
                pessoasFisicas.add(pessoaFisica);
            }
        } catch (SQLException e) {
            // Tratar exceção, se necessário
        } finally {
            conectorBD.close(resultSetPessoaFisica);
            conectorBD.close(statementPessoaFisica);
            conectorBD.close(resultSetPessoa);
            conectorBD.close(statementPessoa);
            conectorBD.close(connection);
        }

        return pessoasFisicas;
    }

    // Método para incluir uma PessoaFisica no banco de dados
   public void incluir(PessoaFisica pessoaFisica) {
    Connection connection = null;
    PreparedStatement statement = null;

    try {
        connection = conectorBD.getConnection();
        connection.setAutoCommit(false); // Inicia uma transação

        // Inserir na tabela Pessoa
        String sqlPessoa = "INSERT INTO Pessoa (nome, logradouro, cidade, estado, telefone, email) VALUES (?, ?, ?, ?, ?, ?)";
        statement = connection.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, pessoaFisica.getNome());
        statement.setString(2, pessoaFisica.getLogradouro());
        statement.setString(3, pessoaFisica.getCidade());
        statement.setString(4, pessoaFisica.getEstado());
        statement.setString(5, pessoaFisica.getTelefone());
        statement.setString(6, pessoaFisica.getEmail());
        statement.executeUpdate();

        // Obter o ID gerado para a Pessoa
        ResultSet generatedKeys = statement.getGeneratedKeys();
        int idPessoa = 0;
        if (generatedKeys.next()) {
            idPessoa = generatedKeys.getInt(1);
        }

        // Inserir na tabela PessoaFisica
        String sqlPessoaFisica = "INSERT INTO PessoaFisica (idPessoa, nome, CPF, logradouro, cidade, estado, telefone, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        statement = connection.prepareStatement(sqlPessoaFisica);
        statement.setInt(1, idPessoa);
        statement.setString(2, pessoaFisica.getNome());
        statement.setString(3, pessoaFisica.getCpf());
        statement.setString(4, pessoaFisica.getLogradouro());
        statement.setString(5, pessoaFisica.getCidade());
        statement.setString(6, pessoaFisica.getEstado());
        statement.setString(7, pessoaFisica.getTelefone());
        statement.setString(8, pessoaFisica.getEmail());
        statement.executeUpdate();

        connection.commit(); // Confirma a transação
    } catch (SQLException e) {
        if (connection != null) {
            try {
                connection.rollback(); // Desfaz a transação em caso de erro
            } catch (SQLException ex) {
            }
        }
    } finally {
        conectorBD.close(statement);
        conectorBD.close(connection);
    }
}



    public void alterar(PessoaFisica pessoaFisica) {
       Connection connection = null;
       PreparedStatement statement = null;

       try {
           connection = conectorBD.getConnection();
           connection.setAutoCommit(false); // Inicia uma transação

           // Verifica se a PessoaFisica existe no banco de dados
           String sqlVerificarExistencia = "SELECT idPessoa FROM PessoaFisica WHERE idPessoa = ?";
           statement = connection.prepareStatement(sqlVerificarExistencia);
           statement.setInt(1, pessoaFisica.getId());
           ResultSet resultSet = statement.executeQuery();

           if (resultSet.next()) {
               // Atualizar tabela PessoaFisica
               String sqlPessoaFisica = "UPDATE PessoaFisica SET nome = ?, cpf = ?, logradouro = ?, cidade = ?, estado = ?, telefone = ?, email = ? WHERE idPessoa = ?";
               statement = connection.prepareStatement(sqlPessoaFisica);
               statement.setString(1, pessoaFisica.getNome());
               statement.setString(2, pessoaFisica.getCpf());
               statement.setString(3, pessoaFisica.getLogradouro());
               statement.setString(4, pessoaFisica.getCidade());
               statement.setString(5, pessoaFisica.getEstado());
               statement.setString(6, pessoaFisica.getTelefone());
               statement.setString(7, pessoaFisica.getEmail());
               statement.setInt(8, pessoaFisica.getId());

               int rowsAffected = statement.executeUpdate();

               if (rowsAffected == 0) {
                   throw new SQLException("Nenhuma linha foi atualizada. Verifique se o ID da PessoaFisica está correto.");
               }

               connection.commit(); // Confirma a transação
           } else {
               throw new SQLException("A PessoaFisica com o ID especificado não existe no banco de dados.");
           }
       } catch (SQLException e) {
           if (connection != null) {
               try {
                   connection.rollback(); // Desfaz a transação em caso de erro
               } catch (SQLException ex) {
               }
           }
       } finally {
           conectorBD.close(statement);
           conectorBD.close(connection);
       }
   }


    public void excluir(int id) {
    Connection connection = null;
    PreparedStatement statement = null;

    try {
        connection = conectorBD.getConnection();
        connection.setAutoCommit(false); // Inicia uma transação

        // Verificar se a PessoaFisica existe no banco de dados
        String sqlVerificarExistencia = "SELECT idPessoa FROM PessoaFisica WHERE idPessoa = ?";
        statement = connection.prepareStatement(sqlVerificarExistencia);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            // Excluir da tabela PessoaFisica
            String sqlExcluirPessoaFisica = "DELETE FROM PessoaFisica WHERE idPessoa = ?";
            statement = connection.prepareStatement(sqlExcluirPessoaFisica);
            statement.setInt(1, id);
            statement.executeUpdate();

            // Excluir da tabela Pessoa
            String sqlExcluirPessoa = "DELETE FROM Pessoa WHERE idPessoa = ?";
            statement = connection.prepareStatement(sqlExcluirPessoa);
            statement.setInt(1, id);
            statement.executeUpdate();

            connection.commit(); // Confirma a transação
            System.out.println("Pessoa física excluída com sucesso.");
        } else {
            System.out.println("A pessoa com o ID especificado não foi encontrada.");
        }
    } catch (SQLException e) {
        if (connection != null) {
            try {
                connection.rollback(); // Desfaz a transação em caso de erro
            } catch (SQLException ex) {
            }
        }
    } finally {
        conectorBD.close(statement);
        conectorBD.close(connection);
    }
}






    // Método para buscar uma PessoaFisica por ID
    public PessoaFisica buscarPorId(int idPessoa) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        PessoaFisica pessoaFisica = null;

        try {
            connection = conectorBD.getConnection();

            String sql = "SELECT * FROM PessoaFisica WHERE idPessoa = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, idPessoa);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Obter os dados da pessoa física do ResultSet
                int id = resultSet.getInt("idPessoaFisica");
                String nome = resultSet.getString("nome");
                String cpf = resultSet.getString("CPF");
                String logradouro = resultSet.getString("logradouro");
                String cidade = resultSet.getString("cidade");
                String estado = resultSet.getString("estado");
                String telefone = resultSet.getString("telefone");
                String email = resultSet.getString("email");

                // Outros atributos da pessoa física...

                // Criar um objeto PessoaFisica com os dados obtidos
                pessoaFisica = new PessoaFisica();
                pessoaFisica.setId(id);
                pessoaFisica.setNome(nome);
                pessoaFisica.setCpf(cpf);
                pessoaFisica.setLogradouro(logradouro);
                pessoaFisica.setCidade(cidade);
                pessoaFisica.setEstado(estado);
                pessoaFisica.setTelefone(telefone);
                pessoaFisica.setEmail(email);
                // Definir os outros atributos da pessoa física no objeto
            }

        } catch (SQLException e) {
            // Tratar exceção, se necessário
        } finally {
            // Fechar recursos (ResultSet, PreparedStatement, Connection), se necessário
        }

        return pessoaFisica;
    }

    // Método para listar todas as pessoas físicas
   public List<PessoaFisica> getPessoasFisicasPorId(int id) {
    List<PessoaFisica> pessoasFisicas = new ArrayList<>();
    Connection connection = null;
    PreparedStatement statementPessoaFisica = null;
    ResultSet resultSetPessoaFisica = null;

    try {
        connection = conectorBD.getConnection();
        
        // Consultar a tabela PessoaFisica
        String sqlPessoaFisica = "SELECT * FROM PessoaFisica"; // Verifique se o nome da tabela está correto
        statementPessoaFisica = connection.prepareStatement(sqlPessoaFisica);
        resultSetPessoaFisica = statementPessoaFisica.executeQuery();

        while (resultSetPessoaFisica.next()) {
            PessoaFisica pessoaFisica = new PessoaFisica();
            pessoaFisica.setId(resultSetPessoaFisica.getInt("idPessoaFisica")); // Verifique se o nome da coluna está correto
            pessoaFisica.setNome(resultSetPessoaFisica.getString("nome")); // Verifique se o nome da coluna está correto
            pessoaFisica.setCpf(resultSetPessoaFisica.getString("CPF")); // Verifique se o nome da coluna está correto
            pessoaFisica.setLogradouro(resultSetPessoaFisica.getString("logradouro")); // Verifique se o nome da coluna está correto
            pessoaFisica.setCidade(resultSetPessoaFisica.getString("cidade")); // Verifique se o nome da coluna está correto
            pessoaFisica.setEstado(resultSetPessoaFisica.getString("estado")); // Verifique se o nome da coluna está correto
            pessoaFisica.setTelefone(resultSetPessoaFisica.getString("telefone")); // Verifique se o nome da coluna está correto
            pessoaFisica.setEmail(resultSetPessoaFisica.getString("email")); // Verifique se o nome da coluna está correto
            // Definir outros atributos da pessoa física, se houver
            
            pessoasFisicas.add(pessoaFisica);
        }
    } catch (SQLException e) {
        // Tratar exceção, se necessário
        e.printStackTrace(); // Imprime a stack trace do erro para facilitar a depuração
    } finally {
        conectorBD.close(resultSetPessoaFisica);
        conectorBD.close(statementPessoaFisica);
        conectorBD.close(connection);
    }

    return pessoasFisicas;
}
}