import javax.swing.*;

public class IniciarJogo extends JFrame {

    public static void main(String[] args) {
        new IniciarJogo();
        JFrame frame = new JFrame("Jogo da Cobrinha");
        TelaJogo telaJogo = new TelaJogo();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(telaJogo);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}