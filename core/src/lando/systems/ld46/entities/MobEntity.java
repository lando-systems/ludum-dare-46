package lando.systems.ld46.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import lando.systems.ld46.Assets;

public class MobEntity extends EnemyEntity {

    private static Animation<TextureRegion> getAnimation(Assets assets) {
        return MathUtils.randomBoolean() ? assets.mobPitchforkAnimation : assets.mobTorchAnimation;
    }

    private float nextMoveTime;
    private Mob boss;
    private float fromX, toX;
    private float lerpTime = 0;

    public MobEntity(Mob boss) {
        super(boss.screen, MobEntity.getAnimation(boss.screen.assets));

        this.boss = boss;

        stateTime = MathUtils.random();
        direction = MathUtils.randomBoolean() ? Direction.left : Direction.right;

        nextMoveTime = MathUtils.random(2f, 5f);

        // prevent first update until physics engine has had a pass
        grounded = false;

        damage = 20;

        initEntity(0, 0, keyframe.getRegionWidth() * 2f, keyframe.getRegionHeight() * 2f);
    }

    private void setBehavior() {
        nextMoveTime = MathUtils.random(2f, 5f);


        toX = boss.position.x + MathUtils.random(-boss.maxDistance, boss.maxDistance) * MathUtils.random(1f, 2f);

    }

    @Override
    public void setGrounded(boolean groundValue) {
        if (grounded != groundValue) {
            super.setGrounded(groundValue);

            if (grounded) {
                setBehavior();
            }
        }
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if (!isGrounded()) return;
        if (toX > position.x + 5) {
            if (screen.physicsSystem.isPositionAboveGround(position.x + 20, collisionBounds.y +30, 50)) {
                velocity.x += 15;
            } else {
                toX = position.x;
            }
        } else if (toX < position.x - 5) {
            if (screen.physicsSystem.isPositionAboveGround(position.x - 20, collisionBounds.y + 30, 90)) {
                velocity.x -= 15;
            } else {
                toX = position.x;
            }
        } else {
            toX = position.x;
        }


        if (position.x == toX) {
            nextMoveTime -= dt;
            if (nextMoveTime < 0) {
                setBehavior();
            }
        }
    }
}
