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
        actualizarEstado("Pensando - " + id, Color.BLUE);
        int tiempoPensar = gui.getTiempoPensando();
        for (int i = tiempoPensar; i > 0; i--) {
            actualizarEstado("Pensando - " + i + " seg", Color.BLUE);
            Thread.sleep(1000);
        }
    }

    private void comer() throws InterruptedException {
        actualizarEstado("Comiendo - " + id, Color.RED);
        int tiempoComer = gui.getTiempoComiendo();
        for (int i = tiempoComer; i > 0; i--) {
            actualizarEstado("Comiendo - " + i + " seg", Color.RED);
            Thread.sleep(1000);
        }
    }

    private void actualizarEstado(String estado, Color color) {
        labelEstado.setText(estado);
        labelEstado.setForeground(color);
    }
}
