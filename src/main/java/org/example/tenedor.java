package org.example;

import javax.swing.*;
import java.awt.*;

public class tenedor {
    private final int id;
    private final JLabel labelTenedor;
    private boolean ocupado;

    // Constructor que acepta un int y un JLabel
    public tenedor(int id, JLabel labelTenedor) {
        this.id = id;
        this.labelTenedor = labelTenedor;
        this.ocupado = false;
    }

    public synchronized void tomar() {
        ocupado = true;
        labelTenedor.setForeground(Color.RED); // Cambiar color del JLabel a rojo cuando el tenedor esté ocupado
    }

    public synchronized void dejar() {
        ocupado = false;
        labelTenedor.setForeground(Color.GREEN); // Cambiar color del JLabel a verde cuando el tenedor esté libre
    }

    public synchronized boolean estaOcupado() {
        return ocupado;
    }
}
