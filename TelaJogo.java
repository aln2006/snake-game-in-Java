/* 
Descrição : Snake Game ( Jogo da Cobrinha )
Nome : Alan Santana Leão e Geovanni Almeida
Data : 11/07/2023 
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class TelaJogo extends JPanel implements ActionListener {

    // Definição das constantes
    private static final int LARGURA_TELA = 1000;
    private static final int ALTURA_TELA = 600;
    private static final int TAMANHO_BLOCO = 50;
    private static final int UNIDADES = LARGURA_TELA * ALTURA_TELA / (TAMANHO_BLOCO * TAMANHO_BLOCO);
    private static final int INTERVALO = 150;
    private static final String NOME_FONTE = "Baloo";

    // Variáveis de estado do jogo
    private final int[] eixoX = new int[UNIDADES];
    private final int[] eixoY = new int[UNIDADES];
    private int corpoCobra = 6;
    private int blocosComidos;
    private int blocoX;
    private int blocoY;
    private char direcao = 'D'; // C - Cima, B - Baixo, E - Esquerda, D - Direita
    private boolean estaRodando = false;

    // Componentes Swing
    Timer timer;
    Random random;

    TelaJogo() {
        random = new Random();
        setPreferredSize(new Dimension(LARGURA_TELA, ALTURA_TELA));
        setBackground(Color.lightGray);
        setFocusable(true);
        addKeyListener(new LeitorDeTeclasAdapter());
        iniciarJogo();
    }

    public void iniciarJogo() {
        criarBloco();
        estaRodando = true;
        timer = new Timer(INTERVALO, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        desenharTela(g);
    }

    public void desenharTela(Graphics g) {
        if (estaRodando) {
            // Desenha o bloco de comida
            g.setColor(Color.red);
            g.fillOval(blocoX, blocoY, TAMANHO_BLOCO, TAMANHO_BLOCO);

            // Desenha a cobra
            for (int i = 0; i < corpoCobra; i++) {
                if (i == 0) {
                    g.setColor(Color.green); // Cabeça da cobra
                    g.fillRect(eixoX[0], eixoY[0], TAMANHO_BLOCO, TAMANHO_BLOCO);
                } else {
                    g.setColor(Color.green); // Corpo da cobra
                    g.fillRect(eixoX[i], eixoY[i], TAMANHO_BLOCO, TAMANHO_BLOCO);
                }
            }
            // Desenha a pontuação
            g.setColor(Color.black);
            g.setFont(new Font(NOME_FONTE, Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Pontos: " + blocosComidos, (LARGURA_TELA - metrics.stringWidth("Pontos: " + blocosComidos)) / 2, g.getFont().getSize());
        } else {
            // Tela de fim de jogo
            fimDeJogo(g);
        }
    }

    private void criarBloco() {
        // Gera a posição aleatória do bloco de comida
        blocoX = random.nextInt(LARGURA_TELA / TAMANHO_BLOCO) * TAMANHO_BLOCO;
        blocoY = random.nextInt(ALTURA_TELA / TAMANHO_BLOCO) * TAMANHO_BLOCO;
    }

    public void fimDeJogo(Graphics g) {
        // Desenha a pontuação na tela de fim de jogo
        g.setColor(Color.black);
        g.setFont(new Font(NOME_FONTE, Font.BOLD, 40));
        FontMetrics fontePontuacao = getFontMetrics(g.getFont());
        g.drawString("Pontos: " + blocosComidos, (LARGURA_TELA - fontePontuacao.stringWidth("Pontos: " + blocosComidos)) / 2, g.getFont().getSize());

        // Desenha a mensagem de fim de jogo
        g.setColor(Color.black);
        g.setFont(new Font(NOME_FONTE, Font.BOLD, 75));
        FontMetrics fonteFinal = getFontMetrics(g.getFont());
        g.drawString("\uD83D\uDE1D Fim do Jogo.", (LARGURA_TELA - fonteFinal.stringWidth("Fim do Jogo")) / 2, ALTURA_TELA / 2);
    }

    public void actionPerformed(ActionEvent e) {
        if (estaRodando) {
            andar();
            alcancarBloco();
            validarLimites();
        }
        repaint();
    }

    private void andar() {
        // Atualiza a posição da cobra movendo cada segmento
        for (int i = corpoCobra; i > 0; i--) {
            eixoX[i] = eixoX[i - 1];
            eixoY[i] = eixoY[i - 1];
        }

        // Movimenta a cabeça da cobra com base na direção atual
        switch (direcao) {
            case 'C':
                eixoY[0] = eixoY[0] - TAMANHO_BLOCO; // Movimento para cima
                break;
            case 'B':
                eixoY[0] = eixoY[0] + TAMANHO_BLOCO; // Movimento para baixo
                break;
            case 'E':
                eixoX[0] = eixoX[0] - TAMANHO_BLOCO; // Movimento para a esquerda
                break;
            case 'D':
                eixoX[0] = eixoX[0] + TAMANHO_BLOCO; // Movimento para a direita
                break;
            default:
                break;
        }
    }

    private void alcancarBloco() {
        // Verifica se a cabeça da cobra alcançou o bloco de comida
        if (eixoX[0] == blocoX && eixoY[0] == blocoY) {
            corpoCobra++; // Aumenta o tamanho da cobra
            blocosComidos++; // Incrementa a pontuação
            criarBloco(); // Gera um novo bloco de comida
        }
    }

    private void validarLimites() {
        // Verifica colisões com o próprio corpo da cobra
        for (int i = corpoCobra; i > 0; i--) {
            if (eixoX[0] == eixoX[i] && eixoY[0] == eixoY[i]) {
                estaRodando = false; // Fim do jogo se a cabeça colidir com o corpo
                break;
            }
        }

        // Verifica colisões com as bordas da tela
        if (eixoX[0] < 0 || eixoX[0] > LARGURA_TELA) {
            estaRodando = false; // Fim do jogo se a cabeça atingir as bordas direita ou esquerda
        }
        if (eixoY[0] < 0 || eixoY[0] > ALTURA_TELA) {
            estaRodando = false; // Fim do jogo se a cabeça atingir o teto ou o chão
        }

        if (!estaRodando) {
            timer.stop(); // Para o temporizador do jogo
        }
    }

    public class LeitorDeTeclasAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            // Atualiza a direção da cobra com base na tecla pressionada
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direcao != 'D') {
                        direcao = 'E'; // Movimento para a esquerda
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direcao != 'E') {
                        direcao = 'D'; // Movimento para a direita
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direcao != 'B') {
                        direcao = 'C'; // Movimento para cima
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direcao != 'C') {
                        direcao = 'B'; // Movimento para baixo
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
