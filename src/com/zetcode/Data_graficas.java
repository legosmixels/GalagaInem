package com.zetcode;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Data_graficas {
    private JTextField textField;
    private JLabel label;
    private JButton boton;
    private String nombre;



    private void data_nombres() {
        // Crear un nuevo JFrame
        JFrame frame = new JFrame("Ingresar Nombre");
        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        // Crear el JLabel
        label = new JLabel("Ingresa tu nombre:");
        frame.add(label);

        // Crear el JTextField
        textField = new JTextField(15);
        frame.add(textField);

        // Crear el botón
        boton = new JButton("Guardar");
        frame.add(boton);

        // Añadir ActionListener al botón
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Guardar el nombre ingresado en la variable
                nombre = textField.getText();
                // Mostrar el nombre en una ventana emergente
                JOptionPane.showMessageDialog(frame, "Nombre guardado: " + nombre);
            }
        });

        // Hacer visible el JFrame
        frame.setVisible(true);

    }
}