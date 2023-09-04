/* 
    Autor: Alarcon ABAP
*/

package cadastro.model;
import java.sql.ResultSet;
import java.sql.Statement;

public class SequenceManager {

    private final ConectorBD conectorBD; // Declaração de variável

    public SequenceManager() {
        this.conectorBD = new ConectorBD(); // Inicialização do objeto ConectorBD no construtor
    }

    public int getValue(String sequenceName) throws java.sql.SQLException {
        int nextValue = 0; // Variável para armazenar o próximo valor da sequência
        String sql = "SELECT nextval('" + sequenceName + "')"; // Consulta SQL para obter o próximo valor da sequência
        ResultSet rs = null; // Declaração de objeto ResultSet para armazenar o resultado da consulta
        Statement statement = null; // Declaração de objeto Statement para executar a consulta

        statement = conectorBD.getStatement(); // Obtém um objeto Statement do ConectorBD
        rs = statement.executeQuery(sql); // Executa a consulta SQL e armazena o resultado no ResultSet
        if (rs.next()) { // Verifica se há um próximo resultado no ResultSet
            nextValue = rs.getInt(1); // Obtém o valor do primeiro coluna do resultado e atribui à variável nextValue
        }
        conectorBD.close(rs); // Fecha o ResultSet
        conectorBD.close(statement); // Fecha o Statement

        return nextValue; // Retorna o próximo valor da sequência
    }
}
