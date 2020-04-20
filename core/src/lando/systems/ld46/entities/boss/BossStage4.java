package lando.systems.ld46.entities.boss;

import com.badlogic.gdx.math.MathUtils;
import lando.systems.ld46.entities.Snek;
import lando.systems.ld46.screens.GameScreen;

public class BossStage4 implements BossStage {

    Boss boss;
    GameScreen screen;
    float snekTimer;

    public BossStage4(Boss boss) {
        this.boss = boss;
        this.screen = boss.screen;
        snekTimer = 5f;
    }

    @Override
    public void update(float dt) {
        // Some attacks or something here too

        snekTimer -= dt;
        if (snekTimer < 0) {
            snekTimer += 10;
            for (int i = 0 ; i < 5; i++) {
                Snek snek = new Snek(screen);
                float x = MathUtils.random(60f, 400);
                if (MathUtils.randomBoolean()) x += 800;
                snek.addToScreen(x, 700);
                screen.particles.makeSpawnClouds(x, 700);
            }
        }

        // Punch
    }

    @Override
    public boolean isComplete() {

        return boss.hits > 4 && boss.currentAnimation == screen.assets.bossWalkAnimation;
    }

    @Override
    public BossStage nextStage() {
        return new BossDeathStage(boss);
    }
}
