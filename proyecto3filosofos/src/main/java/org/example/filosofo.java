package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class filosofo implements Runnable {
    private final int id;
    private final JLabel labelEstado;
    private final Random random = new Random();
    private final mesa_gui gui;

    public filosofo(int id, JLabel labelEstado, mesa_gui gui) {
        this.id = id;
        this.labelEstado = labelEstado;
        this.gui = gui;
    }

    @Override
    public void run() {
        try {
            while (true) {
                pensar();
                gui.tomarTenedores(id);
                comer();
                gui.dejarTenedores(id);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void pensar() throws InterruptedException {
        labelEstado.setText("Pensando");
        labelEstado.setForeground(Color.BLUE);
        int tiempoPensar = random.nextInt(16) + 5; // Aleatorio entre 5 y 20 segundos
        for (int i = tiempoPensar; i > 0; i--) {
            gui.actualizarContadorPensando(i);
            Thread.sleep(1000);
        }
    }

    private void comer() throws InterruptedException {
        labelEstado.setText("Comiendo");
        labelEstado.setForeground(Color.RED);
        int tiempoComer = random.nextInt(5) + 2; // Aleatorio entre 2 y 6 segundos
        for (int i = tiempoComer; i > 0; i--) {
            gui.actualizarContadorComiendo(i);
            Thread.sleep(1000);
        }
    }
}