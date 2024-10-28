package org.example;

import javax.swing.*;
import java.awt.*;

public class tenedorPanel extends JPanel {
    private boolean ocupado;

    public tenedorPanel() {
        this.ocupado = false;
        setOpaque(false); // Hacer el panel transparente
        setPreferredSize(new Dimension(100, 100)); // Aumentar el tamaño del panel para acomodar el círculo grande
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
        repaint(); // Redibujar el panel para reflejar el cambio de estado
    }

    public boolean estaOcupado() {
        return ocupado;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (ocupado) {
            // Convertir Graphics a Graphics2D para mayor control
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Suavizar bordes
            g2d.setStroke(new BasicStroke(5)); // Ajustar grosor de la línea del contorno a 5 px
            g2d.setColor(Color.RED);

            // Dibujar un círculo más grande alrededor del tenedor
            int padding = 2; // Aumentar el círculo hacia adentro, no usar un valor negativo muy grande
            g2d.drawOval(padding, padding, getWidth() - 4 * padding, getHeight() - 2 * padding);
        }
    }
}