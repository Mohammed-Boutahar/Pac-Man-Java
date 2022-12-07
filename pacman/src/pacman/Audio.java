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

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;


public class Audio {
    public static void playSound() {
        //import du fichier audio et execution grace a la methode .start()
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("C:\\Users\\bouta\\Documents\\NetBeansProjects\\pacman\\src\\audios\\pacman.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch(IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
            System.out.println("Error");
        }
    }
}   

