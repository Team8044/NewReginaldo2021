package frc.lib.util;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Twist2d;

public abstract class Interpolatable<T> {
    private final T value;

    public Interpolatable(T value) {
        this.value = value;
    }

    public T get() {
        return this.value;
    }

    public abstract T interpolate(T endValue, double t);

    public T interpolate(Interpolatable<T> endValue, double t) {
        return interpolate(endValue.value, t);
    }

    /** Use for inserting double */
    public static Interpolatable<Double> interDouble(double value) {
        return new Interpolatable<Double>(value) {
            @Override
            public Double interpolate(Double endValue, double t) {
                return (1 - t) * super.value + t * endValue;
            }
        };
    }

    /** Use for inserting Pose2d */
    public static Interpolatable<Pose2d> interPose2d(Pose2d value) {
        return new Interpolatable<Pose2d>(value) {
            @Override
            public Pose2d interpolate(Pose2d endValue, double t) {
                if (t < 0) {
                   return this.get();
                } else if (t >= 1) {
                    return endValue;
                } else {
                    var twist = super.value.log(endValue);
                    return new Pose2d().exp(new Twist2d(twist.dx * t, twist.dy * t, twist.dtheta * t));
                }
            }
        };
    }
}