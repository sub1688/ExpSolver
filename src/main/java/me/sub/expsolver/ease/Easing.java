package me.sub.expsolver.ease;

@SuppressWarnings("all")
public interface Easing {

    double ease(float x);

    Easing EASE_BOUNCE_IN = new Easing() {
        public double ease(float x) {
            float n1 = 7.5625f;
            float d1 = 2.75f;

            if (x < 1 / d1) {
                return n1 * x * x;
            } else if (x < 2 / d1) {
                return n1 * (x -= 1.5 / d1) * x + 0.75f;
            } else if (x < 2.5 / d1) {
                return n1 * (x -= 2.25 / d1) * x + 0.9375f;
            } else {
                return n1 * (x -= 2.625 / d1) * x + 0.984375f;
            }
        }
    };

    Easing EASE_OUT_QUAD = new Easing() {
        public double ease(float x) {
            return 1 - (1 - x) * (1 - x);
        }
    };

    Easing EASE_OUT_SINE = new Easing() {
        public double ease(float x) {
            return Math.sin((x * Math.PI) / 2);
        }
    };

    Easing EASE_IN_SINE = new Easing() {
        public double ease(float x) {
            return 1 - Math.cos((x * Math.PI) / 2);
        }
    };


    Easing EASE_IN_OUT_SINE = new Easing() {
        public double ease(float x) {
            return -(Math.cos(Math.PI * x) - 1) / 2;
        }
    };

    Easing EASE_OUT_CUBIC = new Easing() {
        public double ease(float x) {
            return 1 - Math.pow(1 - x, 3);
        }
    };

    Easing LINEAR = new Easing() {
        public double ease(float x) {
            return x;
        }
    };

    Easing OUT_IN_POWER = new Easing() {
        public double ease(float x) {
            return Math.pow(x * 2 - 1, 2);
        }
    };

    Easing EASE_OUT_BACK = new Easing() {
        public double ease(float x) {
            float c1 = 1.70158f;
            float c3 = c1 + 1;
            return 1 + c3 * Math.pow(x - 1, 3) + c1 * Math.pow(x - 1, 2);
        }
    };

    Easing EASE_BOUNCE_BACK = new Easing() {
        public double ease(float x) {
            if (x < 0.5f) {
                x = 1 - x;
            }
            return x;
        }
    };

    Easing EASE_IN_EXPO = new Easing() {
        public double ease(float x) {
            return x == 0 ? 0 : Math.pow(2, 10 * x - 10);
        }
    };

    Easing SMOOTH_STEP = new Easing() {
        public double ease(float x) {
            return x * x * (3 - 2 * x);
        }
    };



}