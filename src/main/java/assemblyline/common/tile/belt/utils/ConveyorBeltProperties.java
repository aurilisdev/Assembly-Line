package assemblyline.common.tile.belt.utils;

public class ConveyorBeltProperties {

    public int invSize = 1;

    public boolean canBePuller = true;

    public boolean canBePusher = true;

    public final ConveyorClass conveyorClass;

    private ConveyorBeltProperties(ConveyorClass conveyorClass) {
        this.conveyorClass = conveyorClass;
    }

    public static ConveyorBeltProperties builder(ConveyorClass conveyorClass) {
        return new ConveyorBeltProperties(conveyorClass);
    }

    public ConveyorBeltProperties setInvSize(int size) {
        invSize = size;
        return this;
    }

    public ConveyorBeltProperties setNoPusher() {
        canBePusher = false;
        return this;
    }

    public ConveyorBeltProperties setNoPuller() {
        canBePuller = false;
        return this;
    }

}
