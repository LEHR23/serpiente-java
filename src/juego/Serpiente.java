package juego;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Serpiente extends JFrame implements KeyListener, ActionListener {

    private ArrayList<JLabel> serpiente;
    private ArrayList<Rectangle> serpi;
    private Icon cabezadarr, cabezadabaj, cabezadder, cabezadizq, cuerpo, fruta;
    private Random random;
    private int aleatoriox, aleatorioy, x, y;
    private JLabel frutas;
    private Timer timer, cambio;
    private Rectangle serp, frut;
    private JLabel score;

    public Serpiente() {
        random = new Random();
        aleatoriox = random.nextInt(650);
        aleatorioy = random.nextInt(450);
        serpiente = new ArrayList<JLabel>();
        serpi = new ArrayList<Rectangle>();
        //ventana
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setBounds(0, 0, 700, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Color.ORANGE);
        setFocusable(true);
        addKeyListener(this);

        //carga de imagenes
        cabezadarr = new ImageIcon(getClass().getResource("/imagenes/serpca.png"));
        cabezadabaj = new ImageIcon(getClass().getResource("/imagenes/serpcab.png"));
        cabezadder = new ImageIcon(getClass().getResource("/imagenes/serpcd.png"));
        cabezadizq = new ImageIcon(getClass().getResource("/imagenes/serpci.png"));
        cuerpo = new ImageIcon(getClass().getResource("/imagenes/serpcc.png"));
        fruta = new ImageIcon(getClass().getResource("/imagenes/fruta.png"));

        //fruta
        frutas = new JLabel(fruta);
        frutas.setBounds(aleatoriox, aleatorioy, 25, 25);
        add(frutas);
        frut = new Rectangle(frutas.getBounds());

        //serpiente
        JLabel s = new JLabel(cabezadarr);
        s.setBounds(350, 250, 25, 25);
        serpiente.add(s);
        add(serpiente.get(0));
        serp = new Rectangle(s.getBounds());
        serpi.add(serp);

        //timer
        timer = new Timer(80, this);
        timer.start();
        cambio = new Timer(15000, saltodefruta);
        cambio.start();
        
        //score
        score = new JLabel("SCORE: 0");
        score.setBounds(50,10,100,30);
        score.setFont(new Font("DigifaceWide", 2, 16));
        add(score);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //mover arriba
        if (e.getKeyCode() == KeyEvent.VK_UP && y == 0) {
            serpiente.get(0).setIcon(cabezadarr);
            x = 0;
            y = -10;
        }
        //mober abajo
        if (e.getKeyCode() == KeyEvent.VK_DOWN && y == 0) {
            serpiente.get(0).setIcon(cabezadabaj);
            x = 0;
            y = 10;
        }
        //mover derecha
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && x == 0) {
            serpiente.get(0).setIcon(cabezadder);
            x = 10;
            y = 0;
        }
        //mover izquierda
        if (e.getKeyCode() == KeyEvent.VK_LEFT && x == 0) {
            serpiente.get(0).setIcon(cabezadizq);
            x = -10;
            y = 0;
        }
        //pausa
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            timer.stop();
        }
        //resumen
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            timer.start();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //se mueve
        serpi.get(0).setLocation(serpiente.get(0).getLocation());
        for (int i = serpiente.size() - 1; i > 0; i--) {
            serpiente.get(i).setLocation(serpiente.get(i - 1).getLocation());
            serpi.get(i).setLocation(serpiente.get(i).getLocation());
        }
        serpiente.get(0).setLocation(serpiente.get(0).getX() + x,
                serpiente.get(0).getY() + y);
        serp.setLocation(serpiente.get(0).getLocation());
        //comer fruta
        if (frut.intersects(serp)) {
            frutas.setVisible(false);
            JLabel aux = new JLabel(cuerpo);
            aux.setBounds(serpiente.get(serpiente.size() - 1).getBounds());
            add(aux);
            serpiente.add(aux);
            Rectangle auxr = new Rectangle(aux.getBounds());
            serpi.add(auxr);
            System.out.println(serpiente.size());
            score.setText("SCORE: "+(serpiente.size()-1));
            cambio.stop();
            cambio.start();
            metodofruta();
        }
        //serpiente muere muros
        if (serp.getX() < 0 || serp.getX() > 675 || serp.getY() < 0
                || serp.getY() > 450) {
            timer.stop();
            cambio.stop();
            JOptionPane.showMessageDialog(null, "MUERTO");
            murio();
        }
        //serpiente muere por si misma
        if (serpiente.size() >= 10) {
            for (int i = 10 - 1; i < serpiente.size(); i++) {
                if (serpi.get(i).intersects(serp)) {
                    timer.stop();
                    cambio.stop();
                    JOptionPane.showMessageDialog(null, "MUERTO");
                    murio();
                }
            }
        }
    }

    private ActionListener saltodefruta = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            metodofruta();
        }
    };

    private void metodofruta() {
        aleatoriox = random.nextInt(650);
        aleatorioy = random.nextInt(450);
        frutas.setLocation(aleatoriox, aleatorioy);
        frutas.setVisible(true);
        frut.setLocation(frutas.getLocation());
    }

    private void murio() {
        for (JLabel jLabel : serpiente) {
            jLabel.setVisible(false);
            jLabel.removeAll();
        }
        serpiente.clear();
        serpi.clear();
        JLabel s = new JLabel(cabezadarr);
        s.setBounds(350, 250, 25, 25);
        s.setVisible(true);
        serpiente.add(s);
        add(serpiente.get(0));
        serp = new Rectangle(s.getBounds());
        serpi.add(serp);
        x = 0;
        y = -10;
        cambio.start();
        timer.start();
    }
}
