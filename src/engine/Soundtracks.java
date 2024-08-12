/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package engine;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author alexc
 */
public class Soundtracks {
    
       private Clip clip;
       public void reproduceMenuAudio(String rutaArchivo) {
        try {
            // Abrir el archivo de audio como un stream de entrada
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(rutaArchivo));

            // Obtener un clip de sonido
            clip = AudioSystem.getClip();

            // Abrir el clip con el stream de audio
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}