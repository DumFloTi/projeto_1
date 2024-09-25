import dao.ClienteMapDAO;
import dao.IClienteDAO;
import domain.Cliente;

import java.io.File;
import javax.swing.*;
import java.awt.*;

public class App {

    private static IClienteDAO iClienteDAO;

    public static void main(String args[]) {
        iClienteDAO = new ClienteMapDAO();

        String opcao = JOptionPane.showInputDialog(null,
                "Digite 1 para cadastro, 2 para consultar, 3 para exclusão, 4 para alteração ou 5 para sair",
                "Cadastro", JOptionPane.INFORMATION_MESSAGE);

        while (!isOpcaoValida(opcao)) {
            if ("".equals(opcao)) {
                sair();
            }
            opcao = JOptionPane.showInputDialog(null,
                    "Opção inválida digite 1 para cadastro, 2 para consulta, 3 para exclusão, 4 para alteração ou 5 para sair",
                    "Green dinner", JOptionPane.INFORMATION_MESSAGE);
        }

        while (isOpcaoValida(opcao)) {
            if (isOpcaoSair(opcao)) {
                sair();
            } else if (isCadastro(opcao)) {
                String dados = JOptionPane.showInputDialog(null,
                        "Digite os dados do cliente separados por vígula, conforme exemplo: Nome, CPF, Telefone, Endereço, Número, Cidade e Estado",
                        "Cadastro", JOptionPane.INFORMATION_MESSAGE);
                cadastrar(dados);
            } else if(isConsultar(opcao)) {
                String dados = JOptionPane.showInputDialog(null,
                        "Digite o cpf",
                        "Consultar", JOptionPane.INFORMATION_MESSAGE);
                consultar(dados);
            } else if(isExcluir(opcao)) {
                String dados = JOptionPane.showInputDialog(null,
                        "Digite o cpf",
                        "Excluir", JOptionPane.INFORMATION_MESSAGE);
                excluir(dados);
            } else if(isAlterar(opcao)) {
                String dados = JOptionPane.showInputDialog(null,
                        "Digite o cpf",
                        "Atualização", JOptionPane.INFORMATION_MESSAGE);
                alterar(dados);
            }

            opcao = JOptionPane.showInputDialog(null,
                    "Digite 1 para cadastro, 2 para consulta, 3 para exclusão, 4 para alteração ou 5 para sair",
                    "Green dinner", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    //Consultar cliente
    private static void consultar(String dados) {
        Cliente cliente = iClienteDAO.consultar(Long.parseLong(dados));
        if (cliente != null) {
            JOptionPane.showMessageDialog(null, "Cliente encontrado: " + cliente.toString(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            // Exibir a foto, se disponível
            String caminhoFoto = cliente.getCaminhoFoto();
            if (caminhoFoto != null && !caminhoFoto.isEmpty()) {
                ImageIcon foto = new ImageIcon(caminhoFoto);
                // Ajustar o tamanho da imagem para que se ajuste à tela
                Image img = foto.getImage();
                Image newImg = img.getScaledInstance(400, 300, Image.SCALE_SMOOTH);
                foto = new ImageIcon(newImg);
                JOptionPane.showMessageDialog(null, "", "Foto do Cliente", JOptionPane.INFORMATION_MESSAGE, foto);
            } else {
                JOptionPane.showMessageDialog(null, "Foto não disponível", "Informação", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Cliente não encontrado", "Erro", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    //Cadastrar cliente
    private static void cadastrar(String dados) {
        String[] dadosSeparados = dados.split(",");
        
        // Usar JFileChooser para selecionar a foto
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecionar Foto do Cliente");
        
        int userSelection = fileChooser.showOpenDialog(null);
        
        String caminhoFoto = null;
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            caminhoFoto = file.getAbsolutePath();
        }
        
        Cliente cliente = new Cliente(dadosSeparados[0], dadosSeparados[1], dadosSeparados[2], dadosSeparados[3], dadosSeparados[4], dadosSeparados[5], dadosSeparados[6], caminhoFoto);
        Boolean isCadastrado = iClienteDAO.cadastrar(cliente);
        if (isCadastrado) {
            JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso ", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Cliente já se encontra cadastrado", "Erro", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    //Excluir cliente
    private static void excluir(String dados) {
        iClienteDAO.excluir(Long.parseLong(dados));
        JOptionPane.showMessageDialog(null, "Cliente excluido com sucesso! ", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    private static void alterar(String cpfOriginal) {
        // Verificar se o cliente com o CPF original existe
        Cliente clienteExistente = iClienteDAO.consultar(Long.parseLong(cpfOriginal));
        
        if (clienteExistente == null) {
            JOptionPane.showMessageDialog(null, "Cliente com CPF fornecido não encontrado.", "Erro", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    
        // Solicitar novos dados do cliente
        String novosDados = JOptionPane.showInputDialog(null,
                "Digite os novos dados do cliente separados por vírgula, conforme exemplo: Nome, Telefone, Endereço, Número, Cidade e Estado",
                "Atualização", JOptionPane.INFORMATION_MESSAGE);
    
        if (novosDados == null || novosDados.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum dado fornecido para atualização.", "Erro", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    
        String[] dadosSeparados = novosDados.split(",");
    
        // Usar JFileChooser para selecionar a foto
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecionar Nova Foto do Cliente");
        
        int userSelection = fileChooser.showOpenDialog(null);
        
        String caminhoFoto = null;
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            caminhoFoto = file.getAbsolutePath();
        }
    
        // Atualizar os dados do cliente existente com os novos valores fornecidos
        Cliente clienteAtualizado = new Cliente(dadosSeparados[0], cpfOriginal, dadosSeparados[1], dadosSeparados[2], dadosSeparados[3], dadosSeparados[4], dadosSeparados[5], caminhoFoto);
        iClienteDAO.alterar(clienteAtualizado);
        JOptionPane.showMessageDialog(null, "Cliente atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

    }
    
    /* 
    Neste metodo, não estava a conseguir alterar e guardar as alterações, decide manter em comentario como lição
    
    private static void alterar(String dados) {
            // Solicitar novos dados do cliente
            String novosDados = JOptionPane.showInputDialog(null,
                    "Digite os novos dados do cliente separados por vírgula, conforme exemplo: Nome, CPF, Telefone, Endereço, Número, Cidade e Estado",
                    "Atualização", JOptionPane.INFORMATION_MESSAGE);
    
            if (novosDados == null || novosDados.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nenhum dado fornecido para atualização.", "Erro", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
    
            String[] dadosSeparados = novosDados.split(",");
    
            // Usar JFileChooser para selecionar a foto
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Selecionar Foto do Cliente");
            
            int userSelection = fileChooser.showOpenDialog(null);
            
            String caminhoFoto = null;
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                caminhoFoto = file.getAbsolutePath();
            }
    
            // Criar cliente com os dados atualizados com o cpf original
            Cliente cliente = new Cliente(dadosSeparados[0], dadosSeparados[1], dadosSeparados[2], dadosSeparados[3], dadosSeparados[4], dadosSeparados[5], dadosSeparados[6], caminhoFoto);
            iClienteDAO.alterar(cliente);
            JOptionPane.showMessageDialog(null, "Cliente atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    */
    private static boolean isCadastro(String opcao) {
        if ("1".equals(opcao)) {
            return true;
        }
        return false;
    }

    private static boolean isConsultar(String opcao) {
        if ("2".equals(opcao)) {
            return true;
        }
        return false;
    }
    
    private static boolean isExcluir(String opcao) {
        if ("3".equals(opcao)) {
            return true;
        }
        return false;
    }
    
    private static boolean isAlterar(String opcao) {
        if ("4".equals(opcao)) {
            return true;
        }
        return false;
    }

    private static boolean isOpcaoSair(String opcao) {
        if ("5".equals(opcao)) {
            return true;
        }
        return false;
    }

    //Sair da APP
    private static void sair() {
        JOptionPane.showMessageDialog(null, "Até logo ", "Sair",JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    private static boolean isOpcaoValida(String opcao) {
        if ("1".equals(opcao) || "2".equals(opcao) || "3".equals(opcao) || "4".equals(opcao) || "5".equals(opcao)) {
            return true;
        }
        return false;
    }
}