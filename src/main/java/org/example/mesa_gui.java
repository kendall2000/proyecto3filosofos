package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;

public class mesa_gui extends JFrame {
    private final filosofo[] filosofos;
    private final tenedorPanel[] tenedores;
    private final Lock lock = new ReentrantLock();
    private final Condition[] condiciones;
    private final JLabel contadorPensandoLabel;
    private final JLabel contadorComiendoLabel;
    private final JTextField pensandoMinField, pensandoMaxField, comiendoMinField, comiendoMaxField;
    private final Random random = new Random();
    private int tiempoPensandoActual;
    private int tiempoComiendoActual;

    public mesa_gui() {
        setTitle("Los Filósofos que Cenan");
        setSize(900, 900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        filosofos = new filosofo[5];
        tenedores = new tenedorPanel[5];
        condiciones = new Condition[5];

        // Inicializar contadores y campos de entrada para límites de tiempo
        contadorPensandoLabel = new JLabel("Tiempo pensando: 5-20 seg.");
        contadorPensandoLabel.setBounds(10, 10, 200, 20);
        add(contadorPensandoLabel);

        pensandoMinField = new JTextField("5");
        pensandoMinField.setBounds(220, 10, 50, 20);
        add(pensandoMinField);

        pensandoMaxField = new JTextField("20");
        pensandoMaxField.setBounds(280, 10, 50, 20);
        add(pensandoMaxField);

        contadorComiendoLabel = new JLabel("Tiempo comiendo: 2-6 seg.");
        contadorComiendoLabel.setBounds(10, 40, 200, 20);
        add(contadorComiendoLabel);

        comiendoMinField = new JTextField("2");
        comiendoMinField.setBounds(220, 40, 50, 20);
        add(comiendoMinField);

        comiendoMaxField = new JTextField("6");
        comiendoMaxField.setBounds(280, 40, 50, 20);
        add(comiendoMaxField);

        // Botón para actualizar los límites de tiempo
        JButton actualizarBoton = new JButton("Actualizar Límites");
        actualizarBoton.setBounds(350, 25, 150, 30);
        actualizarBoton.addActionListener(e -> actualizarLimites());
        add(actualizarBoton);

        inicializarComponentes();
        iniciarSimulacion();
    }

    private void inicializarComponentes() {
        JPanel panelMesa = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon mesaImage = new ImageIcon(getClass().getResource("/imagenes/filosofos.png"));
                g.drawImage(mesaImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panelMesa.setLayout(null);
        panelMesa.setBounds(0, 0, 900, 800);
        add(panelMesa);

        int[][] coordenadasFilosofos = {
                {674, 251, 125, 172},
                {4, 245, 126, 172},
                {127, 657, 124, 168},
                {549, 679, 116, 145},
                {350, 50, 100, 150}
        };

        for (int i = 0; i < 5; i++) {
            JLabel filosofoLabel = new JLabel("Pensando");
            filosofoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            filosofoLabel.setForeground(Color.BLUE);
            filosofoLabel.setBounds(coordenadasFilosofos[i][0], coordenadasFilosofos[i][1], coordenadasFilosofos[i][2], coordenadasFilosofos[i][3]);
            panelMesa.add(filosofoLabel);

            filosofos[i] = new filosofo(i, filosofoLabel, this);
            condiciones[i] = lock.newCondition();
        }

        int[][] coordenadasTenedores = {
                {290, 250}, {530, 260}, {585, 460}, {390, 570}, {200, 445}
        };

        for (int i = 0; i < 5; i++) {
            tenedorPanel tenedor = new tenedorPanel();
            tenedor.setBounds(coordenadasTenedores[i][0], coordenadasTenedores[i][1], 100, 70);
            panelMesa.add(tenedor);
            tenedores[i] = tenedor;
        }
    }

    private void iniciarSimulacion() {
        for (filosofo filosofo : filosofos) {
            new Thread(filosofo).start();
        }
    }

    public int getTiempoPensando() {
        int min = Integer.parseInt(pensandoMinField.getText());
        int max = Integer.parseInt(pensandoMaxField.getText());
        tiempoPensandoActual = random.nextInt((max - min) + 1) + min;
        return tiempoPensandoActual;
    }

    public int getTiempoComiendo() {
        int min = Integer.parseInt(comiendoMinField.getText());
        int max = Integer.parseInt(comiendoMaxField.getText());
        tiempoComiendoActual = random.nextInt((max - min) + 1) + min;
        return tiempoComiendoActual;
    }

    public void actualizarContadorPensando(int tiempo) {
        contadorPensandoLabel.setText("Tiempo pensando: " + tiempoPensandoActual + " seg. (Restante: " + tiempo + " seg.)");
    }

    public void actualizarContadorComiendo(int tiempo) {
        contadorComiendoLabel.setText("Tiempo comiendo: " + tiempoComiendoActual + " seg. (Restante: " + tiempo + " seg.)");
    }

    private void actualizarLimites() {
        try {
            Integer.parseInt(pensandoMinField.getText());
            Integer.parseInt(pensandoMaxField.getText());
            Integer.parseInt(comiendoMinField.getText());
            Integer.parseInt(comiendoMaxField.getText());
            JOptionPane.showMessageDialog(this, "Límites actualizados correctamente.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese valores numéricos válidos.");
        }
    }

    public void tomarTenedores(int idFilosofo) {
        lock.lock();
        try {
            int tenedorIzquierda = (idFilosofo == 0) ? tenedores.length - 1 : idFilosofo - 1;
            int tenedorDerecha = idFilosofo;
            while (tenedores[tenedorIzquierda].estaOcupado() || tenedores[tenedorDerecha].estaOcupado()) {
                condiciones[idFilosofo].await();
            }
            tenedores[tenedorIzquierda].setOcupado(true);
            tenedores[tenedorDerecha].setOcupado(true);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public void dejarTenedores(int idFilosofo) {
        lock.lock();
        try {
            int tenedorIzquierda = (idFilosofo == 0) ? tenedores.length - 1 : idFilosofo - 1;
            int tenedorDerecha = idFilosofo;
            tenedores[tenedorIzquierda].setOcupado(false);
            tenedores[tenedorDerecha].setOcupado(false);
            condiciones[(idFilosofo + 4) % 5].signal();
            condiciones[(idFilosofo + 1) % 5].signal();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            mesa_gui gui = new mesa_gui();
            gui.setVisible(true);
        });
    }
}