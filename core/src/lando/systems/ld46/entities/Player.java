package lando.systems.ld46.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld46.screens.GameScreen;
import lando.systems.ld46.world.SpawnPlayer;

public class Player extends GameEntity {

    enum JumpState { none, jumping }

    private JumpState jumpState;

    private final float jumpVelocity = 200f;
    private final float horizontalSpeed = 50f;
    private final float horizontalSpeedMinThreshold = 5f;

    private ZombieMech mech;

    public Player(GameScreen screen, SpawnPlayer spawn) {
        this(screen, spawn.pos.x, spawn.pos.y);
    }

    public Player(GameScreen screen, float x, float y) {
        super(screen, screen.game.assets.playerAnimation);
        this.collisionBounds.set(x, y, keyframe.getRegionWidth(), keyframe.getRegionHeight());
        this.imageBounds.set(x, y, keyframe.getRegionWidth(), keyframe.getRegionHeight());
        this.position.set(x, y);
        this.jumpState = JumpState.none;
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        // Horizontal ----------------------------------------

        if (state != State.jumping) {
            // Check for and apply horizontal movement
            boolean moveLeftPressed = Gdx.input.isKeyPressed(Input.Keys.A)
                    || Gdx.input.isKeyPressed(Input.Keys.LEFT);
            boolean moveRightPressed = Gdx.input.isKeyPressed(Input.Keys.D)
                    || Gdx.input.isKeyPressed(Input.Keys.RIGHT);

            if (moveLeftPressed) {
                move(Direction.left);
            } else if (moveRightPressed) {
                move(Direction.right);
            }

            // Apply horizontal drag
            if (!grounded && !moveLeftPressed && !moveRightPressed) {
                velocity.x = 0f;
            } else {
                velocity.x *= 0.85f;
            }

            // Clamp minimum horizontal velocity to zero
            if (Math.abs(velocity.x) < horizontalSpeedMinThreshold) {
                velocity.x = 0f;
            }
        }
        // Vertical ------------------------------------------

        boolean jumpPressed = Gdx.input.isKeyJustPressed(Input.Keys.W);
        if (jumpPressed) {
            jump();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            if (inMech()) {
                jumpOut();
            } else {
                ZombieMech mech = this.screen.zombieMech;
                if (collisionBounds.overlaps(mech.collisionBounds)) {
                    jumpIn(this.screen.zombieMech);
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (inMech()) {
            return;
        }
        super.render(batch);
    }

    public boolean inMech() {
        return mech != null;
    }

    private void move(Direction direction) {
        if (inMech()) {
            mech.move(direction, horizontalSpeed);
        } else {
            move(direction, horizontalSpeed);
        }
    }

    private void jump() {
        jump(inMech() ? 0.5f : 1f);
    }

    private void jump(float velocityMultiplier) {
        if (grounded) {
            velocity.y = jumpVelocity * velocityMultiplier;
            velocity.x /= 2;
            jumpState = JumpState.jumping;
            grounded = false;
        }
    }

    @Override
    public void changeDirection() {
        // noop so it doesn't flip rapidly when pushing against a wall.
    }

    public void jumpIn(ZombieMech mech) {
        this.mech = mech;
         // add state and animation jumping up its ass - update rendering call
    }

    public void jumpOut() {
        if (inMech()) {
            setPosition(mech.position.x, mech.position.y + 20);
            mech = null;
        }
    }
}