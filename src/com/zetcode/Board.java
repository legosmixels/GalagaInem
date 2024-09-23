package com.zetcode;

import com.zetcode.sprite.Alien;
import com.zetcode.sprite.Player;
import com.zetcode.sprite.Shot;

import javax.swing.*;

import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;


public class Board extends JPanel {

    private Image backgroundImage; // La imagen sioq
    private JTextField textField;
    private JLabel label;
    private JButton boton;
    private String nombre;

    private Dimension d;
    private List<Alien> aliens;
    private Player player;
    private Shot shot;

    private int direction = -1;
    private int deaths = 0;
    private int dis = 0 ;

    private boolean inGame = true;
    private String explImg = "src/images/explosion2.png";
    private String message = "Game Over";

    private Timer timer;


    public Board() {
        initBoard();
        gameInit();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);


        //setBackground(Color.black); # aqui se define el color del background
        backgroundImage = new ImageIcon("src/images/fondogalax.gif").getImage();



        timer = new Timer(Commons.DELAY, new GameCycle());
        timer.start();
        temporizador.start();

        gameInit();
    }


    private void gameInit() {

        aliens = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {

                var alien = new Alien(Commons.ALIEN_INIT_X + 36 * j,
                        Commons.ALIEN_INIT_Y + 36 * i);
                aliens.add(alien);
            }
        }

        player = new Player();
        shot = new Shot();
    }

    private void drawAliens(Graphics g) {

        for (Alien alien : aliens) {

            if (alien.isVisible()) {

                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }

            if (alien.isDying()) {

                alien.die();
            }
        }
    }

    private void drawPlayer(Graphics g) {

        if (player.isVisible()) {

            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        if (player.isDying()) {

            player.die();
            inGame = false;

        }
    }

    private void drawShot(Graphics g) {

        if (shot.isVisible()) {

            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);

        }
    }

    private void drawBombing(Graphics g) {

        for (Alien a : aliens) {

            Alien.Bomb b = a.getBomb();

            if (!b.isDestroyed()) {

                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        // Draw the background image
        g.drawImage(backgroundImage, 0, 0, d.width, d.height, this); //escribimos la imagen de fondo
        g.setColor(Color.green);

        if (inGame) {

            g.drawLine(0, Commons.GROUND,
                    Commons.BOARD_WIDTH, Commons.GROUND);

            drawAliens(g);
            drawPlayer(g);
            drawShot(g);
            drawBombing(g);

        } else {

            if (timer.isRunning()) {
                timer.stop();
            }

            gameOver(g);



            System.out.println("diste " + dis + " disparos");

            System.out.println("mataste " + deaths + " Aliens");

            System.out.println("Duraste " + tS + "s");


        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void gameOver(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, Commons.BOARD_WIDTH / 2 - 30, Commons.BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, Commons.BOARD_WIDTH / 2 - 30, Commons.BOARD_WIDTH - 100, 50);

        var small = new Font("Helvetica", Font.BOLD, 14);
        var fontMetrics = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (Commons.BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2,
                Commons.BOARD_WIDTH / 2);
        data_nombres();

    }

    private static int tS = 0;  // Tiempo en segundos


    Timer temporizador = new Timer(1000, new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (inGame) {  // Solo sigue contando si no se ha terminado la partida
                tS++;
            } else {
                ((Timer) e.getSource()).stop();  // Detener el temporizador si ya termino la parida
            }
        }
    });
    private void update() {




        if (deaths == Commons.NUMBER_OF_ALIENS_TO_DESTROY) {

            inGame = false;
            timer.stop();
            message = "Game won!";
            data_nombres();

        }
        // player
        player.act();

        // shot
        if (shot.isVisible()) {

            int shotX = shot.getX();
            int shotY = shot.getY();

            for (Alien alien : aliens) {

                int alienX = alien.getX();
                int alienY = alien.getY();

                if (alien.isVisible() && shot.isVisible()) {
                    if (shotX >= (alienX)
                            && shotX <= (alienX + Commons.ALIEN_WIDTH)
                            && shotY >= (alienY)
                            && shotY <= (alienY + Commons.ALIEN_HEIGHT)) {

                        var ii = new ImageIcon(explImg);
                        alien.setImage(ii.getImage());
                        alien.setDying(true);
                        deaths++;
                        shot.die();
                    }
                }
            }

            int y = shot.getY();
            y -= 8;

            if (y < 0) {
                shot.die();
            } else {
                shot.setY(y);
            }
        }

        // aliens

        for (Alien alien : aliens) {

            int x = alien.getX();

            if (x >= Commons.BOARD_WIDTH - Commons.BORDER_RIGHT && direction != -1) {

                direction = -2;

                Iterator<Alien> i1 = aliens.iterator();

                while (i1.hasNext()) {

                    Alien a2 = i1.next();
                    a2.setY(a2.getY() + Commons.GO_DOWN);
                }
            }

            if (x <= Commons.BORDER_LEFT && direction != 1) {

                direction = 2;

                Iterator<Alien> i2 = aliens.iterator();

                while (i2.hasNext()) {

                    Alien a = i2.next();
                    a.setY(a.getY() + Commons.GO_DOWN);
                }
            }
        }

        Iterator<Alien> it = aliens.iterator();

        while (it.hasNext()) {

            Alien alien = it.next();

            if (alien.isVisible()) {

                int y = alien.getY();

                if (y > Commons.GROUND - Commons.ALIEN_HEIGHT) {
                    inGame = false;
                    message = "Invasion!";
                }

                alien.act(direction);
            }
        }

        // bombs
        var generator = new Random();

        for (Alien alien : aliens) {

            int shot = generator.nextInt(15);
            Alien.Bomb bomb = alien.getBomb();

            if (shot == Commons.CHANCE && alien.isVisible() && bomb.isDestroyed()) {

                bomb.setDestroyed(false);
                bomb.setX(alien.getX());
                bomb.setY(alien.getY());
            }

            int bombX = bomb.getX();
            int bombY = bomb.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if (player.isVisible() && !bomb.isDestroyed()) {

                if (bombX >= (playerX)
                        && bombX <= (playerX + Commons.PLAYER_WIDTH)
                        && bombY >= (playerY)
                        && bombY <= (playerY + Commons.PLAYER_HEIGHT)) {

                    var ii = new ImageIcon(explImg);
                    player.setImage(ii.getImage());
                    player.setDying(true);
                    bomb.setDestroyed(true);
                }
            }

            if (!bomb.isDestroyed()) {

                bomb.setY(bomb.getY() + 2);

                if (bomb.getY() >= Commons.GROUND - Commons.BOMB_HEIGHT) {

                    bomb.setDestroyed(true);
                }
            }
        }
    }

    private void doGameCycle() {

        update();
        repaint();
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {

            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {

            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) {

                if (inGame) {

                    if (!shot.isVisible()) {


                        shot = new Shot(x, y);
                        dis++;

                    }
                }
            }
        }
    }
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
                crearDatos();
                System.out.println(nombre);
                // Mostrar el nombre en una ventana emergente
                JOptionPane.showMessageDialog(frame, "Nombre guardado: " + nombre);
                frame.setVisible(false);


            }

        });

        //Centrar frame
        frame.setLocationRelativeTo(null);
        // Hacer visible el JFrame
        frame.setVisible(true);

    }
    // Método para guardar datos en un archivo en una nueva línea
    public static void guardarEnArchivo(String nombreArchivo, String datos) throws IOException {
        // FileWriter en modo "append" para agregar datos al final del archivo
        FileWriter writer = new FileWriter(nombreArchivo, true);
        writer.write(datos + "\n");  // Escribir los datos y saltar a la siguiente línea
        writer.close();  // Cerrar el archivo
    }

    private void crearDatos() {
        // Crear los datos a guardar
        String datoos = nombre+";"+dis + ";" + deaths + ";" + tS;

        // Guardar el resultado en un archivo
        try {
            guardarEnArchivo("C:\\Proyectos Expo modalidad\\Graficas_galaga_modalidad\\.venv\\dataGalaga.txt", datoos);
            System.out.println("Datos guardados en el archivo.");
        } catch (IOException ex) {
            System.out.println("Ocurrió un error al guardar los datos.");
            ex.printStackTrace();
        }
    }



}
