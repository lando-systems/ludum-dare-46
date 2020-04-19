package lando.systems.ld46.entities;

import lando.systems.ld46.Audio;
import lando.systems.ld46.screens.GameScreen;

public class ZombieMech extends MoveEntity {

    public float moveModifier = 0.5f;

    public ZombieMech(GameScreen screen, float x, float y) {
        super(screen, screen.game.assets.mechAnimation, screen.game.assets.mechAnimation);

        setJump(null, Audio.Sounds.zombie_jump, 250f);

        initEntity(x, y, keyframe.getRegionWidth() * 2, keyframe.getRegionHeight() * 2);
    }

    @Override
    public void move(Direction direction, float speed) {
        super.move(direction, speed * moveModifier);
    }
}
