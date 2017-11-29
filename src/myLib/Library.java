package myLib;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Library
{
  public BufferedImage[] blauerocket;
  public BufferedImage[] gelberocket;
  public BufferedImage[] roterocket;
  public BufferedImage[] gezündeteRoteRocket;
  public BufferedImage[] schwarzerocket;
  public BufferedImage[] explosion;
  public BufferedImage[] yspeedup;
  public BufferedImage[] yspeeddown;
  public BufferedImage[] xspeedup;
  public BufferedImage[] xspeeddown;
  public BufferedImage[] shieldPowerUp;
  public BufferedImage[] leuchtkugelPowerUp;
  public BufferedImage[] shield;
  public BufferedImage[] leuchtkugel;
  public BufferedImage markerSchwarz;
  public BufferedImage markerGelb;
  public BufferedImage background;
  public BufferedImage splashscreen;
  public BufferedImage splashscreenSW;
  public BufferedImage[] heli;
  public SoundLib sound;

  public Library()
  {
    try
    {
      this.heli = loadPics("heli.gif", 4);
      this.leuchtkugel = loadPics("leuchtkugel.png", 5);
      this.shield = loadPics("schildfertig.png", 4);

      this.blauerocket = loadPics("blauerocket.gif", 8);

      this.gelberocket = loadPics("gelberocket.gif", 8);
      this.markerGelb = loadPics("MarkerGelb.png", 1)[0];

      this.roterocket = loadPics("roterocket.gif", 8);
      this.gezündeteRoteRocket = loadPics("gezündetrot.gif", 8);

      this.schwarzerocket = loadPics("schwarzerocket.gif", 8);
      this.markerSchwarz = loadPics("MarkerSchwarz.png", 1)[0];

      this.splashscreen = loadPics("splashscreen.jpg", 1)[0];
      
      this.splashscreenSW = loadPics("splashscreenSW.jpg",1)[0];
      
      this.background = loadPics("background.jpg", 1)[0];
      this.explosion = loadPics("explosion.gif", 5);

      loadPics("MarkerGelb.png", 1);
      loadPics("MarkerSchwarz.png", 1);

      this.yspeedup = loadPics("yspeedup.gif", 1);
      this.yspeeddown = loadPics("yspeeddown.gif", 1);
      this.xspeedup = loadPics("xspeedup.gif", 1);
      this.xspeeddown = loadPics("xspeeddown.gif", 1);
      this.shieldPowerUp = loadPics("shield.gif", 1);
      this.leuchtkugelPowerUp = loadPics("umlenk.gif", 1);

      this.sound = new SoundLib();
      this.sound.loadSound("bumm", "boom.wav");
      this.sound.loadSound("rocket", "rocket_alarm.wav");
      this.sound.loadSound("heli", "heli.wav");
      this.sound.loadSound("rstart", "rocket_start.wav");
    } catch (IllegalArgumentException e) {
      JOptionPane.showMessageDialog(null, "Mindestens eine Ressource konnte nicht geladen werden. Programm wird beendet.\n" + e.getMessage(), "Error", 0);
      System.exit(0);
    }
  }

  public BufferedImage[] loadPics(String path, int pics)
  {
    BufferedImage[] anim = new BufferedImage[pics];
    BufferedImage source = null;

    URL pic_url = getClass().getClassLoader().getResource(path);
    try
    {
      source = ImageIO.read(pic_url);
    }
    catch (IOException localIOException) {
    }
    for (int x = 0; x < pics; x++) {
      anim[x] = source.getSubimage(x * source.getWidth() / pics, 0, source.getWidth() / pics, source.getHeight());
    }
    return anim;
  }
}