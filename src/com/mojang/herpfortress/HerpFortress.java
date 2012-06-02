package com.mojang.herpfortress;

import java.awt.*;
import java.awt.image.*;

import javax.swing.JFrame;

import com.mojang.herpfortress.level.EntityListCache;

public class HerpFortress extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 3;

	private boolean keepRunning = true;
	private BufferedImage screenImage;

	private Bitmap screenBitmap;

	private InputHandler inputHandler;
	private Input mouse;
	private Game game;
	private PlayerView playerView;

	public HerpFortress() {
		Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);

		setPreferredSize(size);
		setMaximumSize(size);
		setMinimumSize(size);

		inputHandler = new InputHandler(this);
	}

	public void start() {
		new Thread(this, "Game Thread").start();
	}

	public void stop() {
		keepRunning = false;
	}

	public void init() {
		Art.init();
		screenImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		screenBitmap = new Bitmap(screenImage);
		mouse = inputHandler.updateMouseStatus(SCALE);

		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "invisible"));

		game = new Game();
		playerView = new PlayerView(game, game.level.redPlayer, mouse);
		requestFocus();
	}

	public void run() {
		init();

		double nsPerFrame = 1000000000.0 / 60.0;
		double unprocessedTime = 0;
		double maxSkipFrames = 10;

		long lastTime = System.nanoTime();
		long lastFrameTime = System.currentTimeMillis();
		int frames = 0;

		while (keepRunning) {
			long now = System.nanoTime();
			double passedTime = (now - lastTime) / nsPerFrame;
			lastTime = now;

			if (passedTime < -maxSkipFrames) passedTime = -maxSkipFrames;
			if (passedTime > maxSkipFrames) passedTime = maxSkipFrames;

			unprocessedTime += passedTime;

			boolean render = false;
			while (unprocessedTime > 1) {
				unprocessedTime -= 1;
				mouse = inputHandler.updateMouseStatus(SCALE);
				EntityListCache.reset();
				tick();
				render = true;
			}
			render = true;
			if (render) {
				EntityListCache.reset();
				render(screenBitmap);
				frames++;
			}

			if (System.currentTimeMillis() > lastFrameTime + 1000) {
				System.out.println(frames + " fps");
				lastFrameTime += 1000;
				frames = 0;
			}

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			swap();
		}
	}

	private void swap() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(2);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		int screenW = getWidth();
		int screenH = getHeight();
		int w = WIDTH * SCALE;
		int h = HEIGHT * SCALE;

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screenW, screenH);
		g.drawImage(screenImage, (screenW - w) / 2, (screenH - h) / 2, w, h, null);
		g.dispose();

		bs.show();
	}

	private void render(Bitmap screen) {
		playerView.render(screen);
		if (mouse.onScreen) screen.draw(Art.i.cursors[0][0], mouse.x - 1, mouse.y - 1);
	}

	private void tick() {
		game.tick();
		playerView.tick();
	}

	public static void main(String[] args) {
		HerpFortress gameComponent = new HerpFortress();

		JFrame frame = new JFrame("Herp Fortress");
		frame.add(gameComponent);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		gameComponent.start();
	}
}
