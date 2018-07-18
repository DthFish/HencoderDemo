package com.dthfish.hencoderdemo.jbox2d;

import android.util.Log;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import static com.dthfish.hencoderdemo.jbox2d.Constant.FRICTION;

/**
 * Description
 * Author DthFish
 * Date  2018/7/17.
 */
public class Box2DUtil {

    public static RectColor createBox(float x, float y, float halfWidth, float halfHeight,
                                      boolean isStatic, World world, int color) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.setType(isStatic ? BodyType.STATIC : BodyType.DYNAMIC);
        bodyDef.position.set(x / Constant.RATE, y / Constant.RATE);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(halfWidth / Constant.RATE, halfHeight / Constant.RATE);

        //设置系数
        FixtureDef def = new FixtureDef();
        def.shape = shape;
        def.density = isStatic ? 0 : 1.0f;
        def.friction = FRICTION;
        def.restitution = 0.6f;
        Body tempBody = world.createBody(bodyDef);
        tempBody.createFixture(def);
        return new RectColor(tempBody, halfWidth, halfHeight, color);

    }

    public static CircleColor createCircle(float x, float y, float radius, World world, int color) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.setType(BodyType.DYNAMIC);
        bodyDef.position.set(x / Constant.RATE, y / Constant.RATE);
        Log.d("createCircle", "createCircle: ");
        CircleShape shape = new CircleShape();
        shape.setRadius(radius / Constant.RATE);
        //设置系数
        FixtureDef def = new FixtureDef();
        def.shape = shape;
        def.density = 3.0f;
        def.friction = FRICTION;
        def.restitution = 2f;
        Body tempBody = world.createBody(bodyDef);
        tempBody.createFixture(def);
        return new CircleColor(tempBody, radius, color);

    }
}
