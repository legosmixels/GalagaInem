package com.zetcode.sprite;

import com.zetcode.Data_graficas;

import javax.swing.ImageIcon;
import java.util.random.RandomGenerator;

public class Shot extends Sprite {

    public Shot() {
    }

    public Shot(int x, int y) {

        initShot(x, y);
    }

    private void initShot(int x, int y) {
        var shotImg = "";
        int ima_shot = RandomGenerator.getDefault().nextInt(1,3);
        switch (ima_shot) {
            case 1:
                shotImg = "src/images/shot1.png";
                break;
            case 2:
                shotImg = "src/images/shot2.png";
                break;
        }
        var ii = new ImageIcon(shotImg);
        setImage(ii.getImage());

        int H_SPACE = 6;
        setX(x + H_SPACE);

        int V_SPACE = 1;
        setY(y - V_SPACE);

    }
}
