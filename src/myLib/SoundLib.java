package myLib;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;

public class SoundLib
{
  Hashtable<String, AudioClip> sounds;
  Vector<AudioClip> loopingClips;

  public SoundLib()
  {
    this.sounds = new Hashtable<String,AudioClip>();
    this.loopingClips = new Vector<AudioClip>();
  }

  public void loadSound(String name, String path) {
    if (this.sounds.containsKey(name)) {
      return;
    }

    URL sound_url = getClass().getClassLoader().getResource(path);
    this.sounds.put(name, Applet.newAudioClip(sound_url));
  }

  public void playSound(String name)
  {
    AudioClip audio = (AudioClip)this.sounds.get(name);
    audio.play();
  }

  public void loopSound(String name) {
    AudioClip audio = (AudioClip)this.sounds.get(name);
    this.loopingClips.add(audio);
    audio.loop();
    
  }

  public void stopLoopingSound() {
    for (AudioClip c : this.loopingClips)
      c.stop();
  }
}