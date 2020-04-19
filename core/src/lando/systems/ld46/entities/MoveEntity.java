package lando.systems.ld46.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld46.screens.GameScreen;

public class MoveEntity extends GameEntity {

    private final float horizontalSpeedMinThreshold = 5f;

    private State lastState;

    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> moveAnimation;

    protected MoveEntity(GameScreen screen, Animation<TextureRegion> idle, Animation<TextureRegion> move) {
        super(screen, idle);

        idleAnimation = idle;
        moveAnimation = move;

        lastState = state;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        
        if (lastState != state) {
            if (state == State.standing) {
                setAnimation(idleAnimation);
            } else if (state == State.walking) {
                setAnimation(moveAnimation);
            }
            lastState = state;
        }

        // Apply horizontal drag
        if (grounded) {
            velocity.x *= 0.85f;
        }

        // Clamp minimum horizontal velocity to zero
        if (Math.abs(velocity.x) < horizontalSpeedMinThreshold) {
            velocity.x = 0f;
        }
    }
}