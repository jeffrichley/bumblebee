package com.infinity.bumblebee.training;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class Ding {

	public void ding() {
		play(new File("./src/main/resources/ding.wav"));
	}

	public void play(File file) {
		try {
			final Clip clip = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
			final CountDownLatch latch = new CountDownLatch(1);

			clip.addLineListener(new LineListener() {
				@Override
				public void update(LineEvent event) {
					if (event.getType() == LineEvent.Type.STOP) {
						clip.close();
						latch.countDown();
					}
				}
			});

			clip.open(AudioSystem.getAudioInputStream(file));
			clip.start();
			
			latch.await();
		} catch (Exception exc) {
			exc.printStackTrace(System.out);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		new Ding().ding();
	}
}
