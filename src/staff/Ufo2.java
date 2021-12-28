package staff;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import killergame4.KillerGame4;
import staff.Autonomous;

public class Ufo2 extends Autonomous {

    private double centerX;
    private double centerY;

    protected BufferedImage[] img;
    protected BufferedImage frame;
    protected double riiing;

    public Ufo2(KillerGame4 game, int state, double x, double y, int width, int height, double vX, double vY, double v) {
        super(game, state, x, y, width, height, vX, vY, v);
        centerX = x + width / 2;
        centerY = y + height / 2;
        
        this.img = new BufferedImage[2];
        loadImg();
        this.frame = img[0];
        riiing = System.currentTimeMillis();
    }

    private void loadImg() {
        try {
            this.img[0] = ImageIO.read(new File("img/nave3.png"));
            this.img[1] = ImageIO.read(new File("img/nave4.png"));
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }

    private void changeFrame() {
        if (this.frame == this.img[0]) {
            this.frame = this.img[1];
        } else {
            this.frame = this.img[0];
        }
        riiing = System.currentTimeMillis();
    }

    @Override
    public void run() {
        while (this.getState() > 0) {
            long nextFrameTime = System.currentTimeMillis() + 5;
            while ((System.currentTimeMillis() < nextFrameTime)) {
                //cambiar preguntar fotogramas
            }

            if (System.currentTimeMillis() - riiing > 450) {
                changeFrame();
            }
                move();
                this.game.testCollision(this);
            try {
                Thread.sleep(8);
            } catch (InterruptedException ex) {
                Logger.getLogger(Ufo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /*
    @Override
    public void run() {
        while (state > 0) {

            try {
                move();
                this.game.testCollision(this);
                Thread.sleep(8);
            } catch (InterruptedException ex) {

            }
        }
    }*/
    @Override
    public void move() {
        /* Movimiento con tiempo
        double actualTime = System.nanoTime();
        double t = (actualTime - time) / 10000000;

        x += vX * v * t;
        y += vY * v * t;
        
        time = System.nanoTime();
         */

        x += vX * v;
        y += vY * v;

    }

    @Override
    public boolean intersect(VisibleObject o) {
        Rectangle me = new Rectangle((int) this.x, (int) this.y, this.width, this.height);
        Rectangle obstacle = new Rectangle((int) o.x, (int) o.y, o.width, o.height);

        return me.intersects(obstacle);
    }

    @Override
    public void render(Graphics2D g) {
        g.drawImage(this.frame, (int)x, (int)y, width, height, null);
    }

}
