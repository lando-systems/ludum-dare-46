package lando.systems.ld46.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld46.Audio;
import lando.systems.ld46.screens.GameScreen;
import lando.systems.ld46.world.SpawnPlayer;

public class Player extends MoveEntity {

    enum JumpState { none, jumping }

    private JumpState jumpState;

    private final float jumpVelocity = 450f;
    private final float horizontalSpeed = 50f;
    private final float horizontalSpeedMinThreshold = 5f;

    private Animation<TextureRegion> punchAnimation;
    private float punchTime = -1;

    private ZombieMech mech = null;

    public Player(GameScreen screen, SpawnPlayer spawn) {
        this(screen, spawn.pos.x, spawn.pos.y);
    }

    public Player(GameScreen screen, float x, float y) {
        super(screen, screen.game.assets.playerAnimation, screen.game.assets.playerMoveAnimation);
        float playerWidth = keyframe.getRegionWidth() * 2;
        float playerHeight = keyframe.getRegionHeight() * 2;
        this.collisionBounds.set(x, y, playerWidth, playerHeight);
        this.imageBounds.set(this.collisionBounds);
        this.setPosition(x, y);

        punchAnimation = screen.game.assets.playerAttackAnimation;

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

        updatePunch(dt);

        // Vertical ------------------------------------------

        boolean jumpPressed = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
        if (jumpPressed) {
            jump();
        }

        boolean punchPressed = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
        if (punchPressed) {
            punch();
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

    private void updatePunch(float dt) {
        if (punchTime != -1) {
            punchTime += dt;
            if (punchTime > punchAnimation.getAnimationDuration()) {
                punchTime = -1;
            } else {
                keyframe = screen.assets.playerAttackAnimation.getKeyFrame(punchTime);
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
            centerOn(mech);
        } else {
            move(direction, horizontalSpeed);
        }
    }

    private void jump() {
        if (inMech()) {
            mech.jump(1f);
        } else {
            jump(1f);
        }
    }

    private void jump(float velocityMultiplier) {
        if (grounded) {
            playSound(Audio.Sounds.doc_jump);
            velocity.y = jumpVelocity * velocityMultiplier;
            velocity.x /= 2;
            jumpState = JumpState.jumping;
            grounded = false;
        }
    }

    private void punch() {
        if (punchTime == -1) {
            playSound(Audio.Sounds.doc_punch);
            punchTime = 0;
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