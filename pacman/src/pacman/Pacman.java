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

import javax.swing.JFrame;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

//création de la classe Pacman qui permettera l'affichage du programme dans un JFrame
public class Pacman extends JFrame{

	public Pacman() {}
	public static void main(String[] args) {
		Pacman pac = new Pacman();
		pac.setVisible(true);
		pac.setTitle("Pacman");
		pac.setSize(380,420);
		pac.setDefaultCloseOperation(EXIT_ON_CLOSE);
		pac.setLocationRelativeTo(null);
	}
    }

