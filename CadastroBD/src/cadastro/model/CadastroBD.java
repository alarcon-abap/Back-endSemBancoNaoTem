/* Alarcon ABAP
 */
package cadastro.model;
import cadastrointerface.InterfaceCadastro;
import java.util.Scanner;

public class CadastroBD {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            PessoaFisicaDAO pessoaFisicaDAO = new PessoaFisicaDAO();
            PessoaJuridicaDAO pessoaJuridicaDAO = new PessoaJuridicaDAO();

            boolean continuar = true;
            while (continuar) {
                InterfaceCadastro.exibirOpcoes();

                int opcao = InterfaceCadastro.lerOpcao(scanner);

                switch (opcao) {
                    case 1->    {
                                InterfaceCadastro.realizarInclusao
                                (scanner, pessoaFisicaDAO, pessoaJuridicaDAO);
                    }
                    case 2 -> {
                                InterfaceCadastro.realizarAlteracao
                                (scanner, pessoaFisicaDAO, pessoaJuridicaDAO);
                    break;
                    }
                    case 3 -> {
                                InterfaceCadastro.realizarExclusao
                                (scanner, pessoaFisicaDAO, pessoaJuridicaDAO);
                    break;
                    }
                    case 4 -> {
                                InterfaceCadastro.realizarObtencaoPorID
                                (scanner, pessoaFisicaDAO, pessoaJuridicaDAO);
                    break;
                    }
                    case 5 -> {
                                InterfaceCadastro.realizarListagem
                                (scanner, pessoaFisicaDAO, pessoaJuridicaDAO);
                    break;
                    }
                    case 0 -> continuar = false;
                    default -> System.out.println("Opção inválida.");
                }
            }
        }
    }
}

