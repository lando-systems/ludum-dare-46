package lando.systems.ld46.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld46.screens.GameScreen;

public class SpawnBoss {

    public Vector2 pos;
    public float size = Level.TILE_SIZE;
    public TextureRegion texture;

    public SpawnBoss(GameScreen gameScreen, float x, float y) {
        this.pos = new Vector2(x, y);
        this.texture = gameScreen.assets.whitePixel;
    }

    public void render(SpriteBatch batch) {
        batch.setColor(1f, 0f, 1f, 0.5f);
        batch.draw(texture, pos.x, pos.y, size, size);
        batch.setColor(1f, 1f, 1f, 1f);
    }

}
