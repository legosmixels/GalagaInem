package com.zetcode.sprite;
import java.util.random.RandomGenerator;
import javax.swing.ImageIcon;

public class Alien extends Sprite {

    private Bomb bomb;

    public Alien(int x, int y) {

        initAlien(x, y);
    }

    private void initAlien(int x, int y) {

        this.x = x;
        this.y = y;

        bomb = new Bomb(x, y);
        int ima_alien = RandomGenerator.getDefault().nextInt(1,5);
        var alienImg="";
        switch (ima_alien){
            case 1:
                alienImg = "src/images/alien1.jpg";
                break;
            case 2:
                alienImg = "src/images/alien2.jpg";
                break;
            case 3:
                alienImg = "src/images/alien3.jpg";
                break;
            case 4:
                alienImg = "src/images/alien4.jpg";
                break;
        }
        var ii = new ImageIcon(alienImg);

        setImage(ii.getImage());
    }

    public void act(int direction) {

        this.x += direction;
    }

    public Bomb getBomb() {

        return bomb;
    }

    public class Bomb extends Sprite {

        private boolean destroyed;

        public Bomb(int x, int y) {

            initBomb(x, y);
        }

        private void initBomb(int x, int y) {

            setDestroyed(true);

            this.x = x;
            this.y = y;
            var bombImg = "src/images/bomb1.png";

            var ii = new ImageIcon(bombImg);
            setImage(ii.getImage());
        }

        public void setDestroyed(boolean destroyed) {

            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {

            return destroyed;
        }
    }
}
