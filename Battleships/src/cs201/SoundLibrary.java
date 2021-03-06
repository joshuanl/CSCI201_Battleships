package cs201;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.sound.sampled.*;


public class SoundLibrary {
	private static Semaphore semaphore = new Semaphore(1);
	private static Map<String, File> soundMap;
	private static File toPlay;
	private static Thread t;
	private String sound;
	static{
		soundMap = new HashMap<String,File>();
		soundMap.put("cannon.wav", new File("Sounds/cannon.wav"));
		soundMap.put("explode.wav", new File("Sounds/explode.wav"));
		soundMap.put("sinking.wav", new File("Sounds/sinking.wav"));
		soundMap.put("splash.wav", new File("Sounds/splash.wav"));
	}
	
//	public SoundLibrary(String sound){
//		this.sound = sound;
//	}

	public static void playSound(String sound) {
		toPlay = soundMap.get(sound);
		t = new Thread(){
			public void run(){
				if(toPlay == null) {
					toPlay = new File(sound);
					soundMap.put(sound, toPlay);
				}
				
				try {
				AudioInputStream stream = AudioSystem.getAudioInputStream(toPlay);
				AudioFormat format = stream.getFormat();
				SourceDataLine.Info info = new DataLine.Info(SourceDataLine.class,format,(int) (stream.getFrameLength() * format.getFrameSize()));
				SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
				
				line.open(stream.getFormat());
				line.start();
				int num_read = 0;
				byte[] buf = new byte[line.getBufferSize()];
				while ((num_read = stream.read(buf, 0, buf.length)) >= 0)
				{
					int offset = 0;
					
					while (offset < num_read)
					{
						offset += line.write(buf, offset, num_read - offset);
					}
				}
				line.drain();
				line.stop();
				} catch(IOException | UnsupportedAudioFileException | LineUnavailableException ioe) {
					System.out.println("Audio file is invalid!");
				}
			}//end of run
		};//end of thread
		try{
			t.start();
		}catch(IllegalThreadStateException itse){
			System.out.println("illegal thread state exception, trying to play sound: "+itse.getMessage());
		}
	}//end of playsound
}
