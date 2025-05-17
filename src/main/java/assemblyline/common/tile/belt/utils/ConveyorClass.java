package assemblyline.common.tile.belt.utils;

public enum ConveyorClass {

    REGULAR(1), FAST(2), EXPRESS(4), TURBO(8);

    public final double speed;

    private ConveyorClass(double speed) {
        this.speed = speed;
    }

}
