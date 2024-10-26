package puppy.code.utils;

import puppy.code.Entidades.Ball2;
import puppy.code.Entidades.Bullet;
import puppy.code.Entidades.Nave4;

public class CollisionUtils {

    public static boolean checkCollision(Bullet bullet, Ball2 asteroid) {
        float bulletRadius = bullet.getSprite().getWidth() / 2;
        float asteroidRadius = asteroid.getSprite().getWidth() / 2;

        float dx = bullet.getSprite().getX() + bulletRadius - (asteroid.getSprite().getX() + asteroidRadius);
        float dy = bullet.getSprite().getY() + bulletRadius - (asteroid.getSprite().getY() + asteroidRadius);

        // Utiliza Math.sqrt en lugar de MathUtils.sqrt
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < bulletRadius + asteroidRadius;
    }

    public static boolean checkCollision(Nave4 nave, Ball2 asteroid) {
        float naveRadius = nave.getSpr().getWidth() / 2;
        float asteroidRadius = asteroid.getSprite().getWidth() / 2;

        float dx = nave.getSpr().getX() + naveRadius - (asteroid.getSprite().getX() + asteroidRadius);
        float dy = nave.getSpr().getY() + naveRadius - (asteroid.getSprite().getY() + asteroidRadius);

        // Utiliza Math.sqrt en lugar de MathUtils.sqrt
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < naveRadius + asteroidRadius;
    }
    
    public static boolean checkCollision(Ball2 fragment1, Ball2 fragment2) {
        float fragment1Radius = fragment1.getSprite().getWidth() / 2;
        float fragment2Radius = fragment2.getSprite().getWidth() / 2;

        float dx = fragment1.getSprite().getX() + fragment1Radius - (fragment2.getSprite().getX() + fragment2Radius);
        float dy = fragment1.getSprite().getY() + fragment1Radius - (fragment2.getSprite().getY() + fragment2Radius);

        // Utiliza Math.sqrt en lugar de MathUtils.sqrt
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < fragment1Radius + fragment2Radius;
    }
}
