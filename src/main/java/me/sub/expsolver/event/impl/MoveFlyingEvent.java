package me.sub.expsolver.event.impl;

public class MoveFlyingEvent {

    private float strafe;
    private float forward;
    private float friction;
    private float rotationYaw;


    public MoveFlyingEvent(float strafe, float forward, float friction, float rotationYaw) {
        this.strafe = strafe;
        this.forward = forward;
        this.friction = friction;
        this.rotationYaw = rotationYaw;
    }


    public float getStrafe() {
        return strafe;
    }

    public void setStrafe(float strafe) {
        this.strafe = strafe;
    }

    public float getForward() {
        return forward;
    }

    public void setForward(float forward) {
        this.forward = forward;
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public float getRotationYaw() {
        return rotationYaw;
    }

    public void setRotationYaw(float rotationYaw) {
        this.rotationYaw = rotationYaw;
    }
}
