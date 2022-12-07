/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman;
/**
 *
 * @author MED BTH
 */

//import des packages nécessaires à noter pour les interfaces graphiques swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class fonctions_easy extends JPanel implements ActionListener {
    //création des variables
    private Dimension d = new Dimension(400, 400);
    //police pour le score
    private final Font font = new Font("Arial", Font.BOLD, 14);
    private boolean enJeu = false;
    private boolean mort = false;
    //variables de mesure et de dimensionnement du jeu
    private final int BLOCK_SIZE = 24;
    private final int N_BLOCKS = 15;
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
        //vitesse de pacman
    private final int PACMAN_SPEED = 4;
        //nombre d'ennemies
    private final int N_ennemies = 4;
    //variables du joueur
    private int vies, score;
    //localisation du pacman
    private int pacman_x, pacman_y, pacmand_x, pacmand_y;
    private int req_dx, req_dy;
    //localisation des ennemies
    private int[] dx=new int[4], dy=new int[4];
    private int[] ennemie_x=new int[12], ennemie_y=new int[12], ennemie_dx=new int[12], ennemie_dy=new int[12], ennemie_speed=new int[12];
    //variables visuelles (images)
    private Image coeur, ennemie;
    private Image haut, bas, gauche, droite;
    //création de la map en version numérique
    private final int levelData[] = {
        12, 19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22, 9,
        19, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 22,
        17, 16, 16, 24, 16, 16, 16, 16, 16, 16, 16, 24, 16, 16, 20,
        17, 16, 20, 0 , 17, 16, 16, 24, 16, 16, 20, 0 , 17, 16, 20,
        17, 16, 16, 18, 16, 16, 20,  0, 17, 16, 16, 18, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 20,
        25, 24, 16, 16, 24, 24, 28,  0, 25, 24, 24, 16, 16, 24, 28,
        2 ,  2, 17, 20,  0,  0,  0,  0,  0,  0,  0, 17, 20,  2,  2,
        8 ,  8, 17, 20,  0,  0,  0,  0  ,0,  0,  0, 17, 20,  8,  8,
        19 , 18, 16, 16, 18, 18, 22, 0, 19, 18, 18, 16, 16, 18, 22,
        17 , 16, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 16, 16, 20,
        25, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 28,
        6, 25, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28, 3};
    
//nous reprenons les données du tableau précedent pour eviter de modifier les donnée de la map pendant le déroulement du jeu
    private int[] screenData = new int[N_BLOCKS * N_BLOCKS];
    //on a introduit ces vitteses pour que la vitesse de chaque fantôme soit random et varie après chaque mort
    private final int validSpeeds[] = {1, 2, 3, 4, 6, 8};
    private int vitesseActu = 3;
    private Timer timer;
    
    //--------------------------------------------//
    //là on passe au développement des fonctions du jeu//
    //-------------------------------------------//
    
    //fonction qui initialise le controle par clavier et assure l'interaction utilisateur-jeu
    public fonctions_easy() {
        loadImages();
        timer = new Timer(40, this);
        timer.start();
        addKeyListener(new clavier());
        setFocusable(true);
        initialise_jeu();
    }
    //import des images
    private void loadImages() {
    	bas = new ImageIcon("C:\\Users\\bouta\\Documents\\NetBeansProjects\\pacman\\src\\images\\bas.gif").getImage();
    	haut = new ImageIcon("C:\\Users\\bouta\\Documents\\NetBeansProjects\\pacman\\src\\images\\haut.gif").getImage();
    	gauche = new ImageIcon("C:\\Users\\bouta\\Documents\\NetBeansProjects\\pacman\\src\\images\\gauche.gif").getImage();
    	droite = new ImageIcon("C:\\Users\\bouta\\Documents\\NetBeansProjects\\pacman\\src\\images\\droite.gif").getImage();
        ennemie = new ImageIcon("C:\\Users\\bouta\\Documents\\NetBeansProjects\\pacman\\src\\images\\ennemie.gif").getImage();
        coeur = new ImageIcon("C:\\Users\\bouta\\Documents\\NetBeansProjects\\pacman\\src\\images\\coeur.png").getImage();
    }
    
    //initialise les parametres du niveau choisi
    private void initialise_jeu() {
        //nombre de coeurs et score initiaux
    	vies = 4;
        score = 0;
        //creer une copie de la map numérique
        for (int i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            screenData[i] = levelData[i];
        }
        continue_niveau();
    }
    
    //remet les variables de position à leur valeur initiale au début du jeu ou après qu'un ennemie nous touche
    private void continue_niveau() {
        int random;
        for (int i = 0; i < N_ennemies; i++) {
            //position initiale ennemies
            ennemie_y[i] = 5 * BLOCK_SIZE; 
            ennemie_x[i] = 4 * BLOCK_SIZE;
            random = (int) (Math.random() * (vitesseActu + 1));
            if (random > vitesseActu) {
                random = vitesseActu;
            }
            ennemie_speed[i] = validSpeeds[random];
        }
        //position initiale pacman
        pacman_x = 7 * BLOCK_SIZE;
        pacman_y = 12 * BLOCK_SIZE;
        mort = false;
    }
    
    //assure la continuité du jeu en verifiant si pacman est toujours en vie
    private void play(Graphics2D g2d) {
        if (mort) {
            elimine();
        } else {
            movePacman();
            drawPacman(g2d);
            moveennemies(g2d);
            checkScore();
        }
    }
    
    //affiche le score
    private void drawScore(Graphics2D g) {
        g.setFont(font);
        g.setColor(new Color(5, 181, 79));
        String s = "Score: " + score;
        g.drawString(s, SCREEN_SIZE / 2 + 96, SCREEN_SIZE + 16);
        for (int i = 0; i < vies; i++) {
            g.drawImage(coeur, i * 28 + 8, SCREEN_SIZE + 1, this);
        }
    }
    
    //vérifie si pacman a mangé tous les points blancs
    private void checkScore() {
         if (score==191){
            enJeu = false;
            new Niveau_reussi().setVisible(true);
        }
    }
    
    //verifie si pacman peut encore jouer ou bien il a perdu la session
    private void elimine() {
    	vies--;
        if (vies == 0) {
            enJeu = false;
            new Niveau().setVisible(true);
            System.out.println("GAME OVER");
            System.out.println("Try again ^^");
        }
        continue_niveau();
    }
    
    //assure le deplacement des ennemies
    private void moveennemies(Graphics2D g2d) {
        int pos;
        int count;
        for (int i = 0; i < N_ennemies; i++) {
            if (ennemie_x[i] % BLOCK_SIZE == 0 && ennemie_y[i] % BLOCK_SIZE == 0) {  //s'assurer que l'ennemie bouge dans une trajectoire donnée et non entre les points blancs
                pos = ennemie_x[i] / BLOCK_SIZE + N_BLOCKS * (ennemie_y[i] / BLOCK_SIZE);
                count = 0;
                if ((screenData[pos] & 1)== 0 && ennemie_dx[i] != 1) {
                    dx[count] = -1;
                    dy[count] = 0;
                    count++;
                }
                if ((screenData[pos] & 2) == 0 && ennemie_dy[i] != 1) {
                    dx[count] = 0;
                    dy[count] = -1;
                    count++;
                }
                if ((screenData[pos] & 4) == 0 && ennemie_dx[i] != -1) {
                    dx[count] = 1;
                    dy[count] = 0;
                    count++;
                }
                if ((screenData[pos] & 8) == 0 && ennemie_dy[i] != -1) {
                    dx[count] = 0;
                    dy[count] = 1;
                    count++;
                }
                if (count == 0) {
                    if (screenData[pos] == 15) {
                        ennemie_dx[i] = 0;
                        ennemie_dy[i] = 0;
                    } else {
                        ennemie_dx[i] = -ennemie_dx[i];
                        ennemie_dy[i] = -ennemie_dy[i];
                    }
                } else {
                    count = (int) (Math.random() * count);
                    if (count > 3) {
                        count = 3;
                    }
                    ennemie_dx[i] = dx[count];
                    ennemie_dy[i] = dy[count];
                }
            }
            ennemie_x[i] = ennemie_x[i] + (ennemie_dx[i] * ennemie_speed[i]);
            ennemie_y[i] = ennemie_y[i] + (ennemie_dy[i] * ennemie_speed[i]);
            drawennemie(g2d, ennemie_x[i] + 1, ennemie_y[i] + 1);
            if (pacman_x > (ennemie_x[i] - 12) && pacman_x < (ennemie_x[i] + 12)
                    && pacman_y > (ennemie_y[i] - 12) && pacman_y < (ennemie_y[i] + 12)
                    && enJeu) {
                mort = true;
            }
        }
    }
    
    //affiche l'ennemie
    private void drawennemie(Graphics2D g2d, int x, int y) {
    	g2d.drawImage(ennemie, x, y, this);
        }
    
    //assure le deplacement du pacman après saisi de la commande clavier
    private void movePacman() {
        int pos;
        int count;
        if (pacman_x % BLOCK_SIZE == 0 && pacman_y % BLOCK_SIZE == 0) {
            pos = pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE);
            count = screenData[pos];
            if ((count & 16) != 0) {
                screenData[pos] = (int) (count & 15);
                score++;
            }
            if (req_dx != 0 || req_dy != 0) {
                if (!((req_dx == -1 && req_dy == 0 && (count & 1) != 0)
                        || (req_dx == 1 && req_dy == 0 && (count & 4) != 0)
                        || (req_dx == 0 && req_dy == -1 && (count & 2) != 0)
                        || (req_dx == 0 && req_dy == 1 && (count & 8) != 0))) {
                    pacmand_x = req_dx;
                    pacmand_y = req_dy;
                }
            }
            // Cherche les obstacles
            if ((pacmand_x == -1 && pacmand_y == 0 && (count & 1) != 0)
                    || (pacmand_x == 1 && pacmand_y == 0 && (count & 4) != 0)
                    || (pacmand_x == 0 && pacmand_y == -1 && (count & 2) != 0)
                    || (pacmand_x == 0 && pacmand_y == 1 && (count & 8) != 0)) {
                pacmand_x = 0;
                pacmand_y = 0;
            }
        } 
        pacman_x = pacman_x + PACMAN_SPEED * pacmand_x;
        pacman_y = pacman_y + PACMAN_SPEED * pacmand_y;
    }
    
    //synchroniser l'image avec les actions du pacman (utilisateur)
    private void drawPacman(Graphics2D g2d) {
        if (req_dx == -1) {
        	g2d.drawImage(gauche, pacman_x + 1, pacman_y + 1, this);
        } else if (req_dx == 1) {
        	g2d.drawImage(droite, pacman_x + 1, pacman_y + 1, this);
        } else if (req_dy == -1) {
        	g2d.drawImage(haut, pacman_x + 1, pacman_y + 1, this);
        } else {
        	g2d.drawImage(bas, pacman_x + 1, pacman_y + 1, this);
        }
    }
    
    //affichage des obstacles et les points blancs à manger
    private void drawMaze(Graphics2D g2d) {
        int i = 0;
        int x, y;
        for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
            for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {
                g2d.setColor(new Color(0,0,255));
                g2d.setStroke(new BasicStroke(5));
                if ((levelData[i] == 0)) { 
                	g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                 }
                if ((screenData[i] & 1) != 0) { 
                    g2d.drawLine(x, y, x, y + BLOCK_SIZE - 1);
                }
                if ((screenData[i] & 2) != 0) { 
                    g2d.drawLine(x, y, x + BLOCK_SIZE - 1, y);
                }
                if ((screenData[i] & 4) != 0) { 
                    g2d.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }
                if ((screenData[i] & 8) != 0) { 
                    g2d.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }
                if ((screenData[i] & 16) != 0) { 
                    g2d.setColor(new Color(255,255,255));
                    g2d.fillOval(x + 10, y + 10, 6, 6);
                }
                i++;
            }
        }
    }    
    //le passage du numérique au graphique (coté visuel)
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);
        drawMaze(g2d);
        drawScore(g2d);
        if (enJeu) {
            play(g2d);
        } 
    }

    //les controles du clavier
    class clavier extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (enJeu) {
                if (key == KeyEvent.VK_LEFT) {
                    req_dx = -1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_RIGHT) {
                    req_dx = 1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_UP) {
                    req_dx = 0;
                    req_dy = -1;
                } else if (key == KeyEvent.VK_DOWN) {
                    req_dx = 0;
                    req_dy = 1;
                } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
                    enJeu = false;
                    new Niveau().setVisible(true);                  
                } 
            } else {
                if (key == KeyEvent.VK_SPACE) {
                    enJeu = true;
                    initialise_jeu();
                }
            }
        }
}

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}