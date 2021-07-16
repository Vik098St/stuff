public class House {
    private int floors;
    private Walls walls;
    private Roof roof;
    private Doors doors;
    private Windows windows;
    private boolean hasSwimmingPool = false;
    private boolean hasGarden = false;
    private boolean hasGarage = false;

    public void setFloors(int floors) {
        this.floors = floors;
    }

    public void setWalls(int count,String material) {
        this.walls = new Walls();
        this.walls.material = material;
        this.walls.num = count;
    }

    public void setRoof(String material) {
        this.roof = new Roof();
        this.roof.material = material;
    }

    public void setDoors(int count,String material) {
        this.doors = new Doors();
        this.doors.material = material;
        this.doors.num = count;
    }

    public void setWindows(int count) {
        this.windows = new Windows();
        this.windows.num = count;
    }

    public void setHasSwimmingPool(boolean hasSwimmingPool) {
        this.hasSwimmingPool = hasSwimmingPool;
    }

    public void setHasGarden(boolean hasGarden) {
        this.hasGarden = hasGarden;
    }

    public void setHasGarage(boolean hasGarage) {
        this.hasGarage = hasGarage;
    }

    public void getInfo() {
            System.out.println("House info:");
        if (floors <4) System.out.println("This is Cottage");
        if (floors > 4) System.out.println("This Apartment Building");
            System.out.println("  " + walls.getNum() + " walls made of " + walls.getMaterial());
            System.out.println("  " + doors.getNum() + " doors made of " + doors.getMaterial());
            System.out.println("  " + "roof made of " + roof.getMaterial());
            System.out.println("  " + windows.getNum() + " windows" );
            if (hasGarage) System.out.println("  " + "with garage");
            if (hasSwimmingPool) System.out.println("  " + "with swimming pool");
        if (hasGarden) System.out.println("  " + "with beautiful garden ");
    }
}

class Walls {
    int num;
    String material;

    public int getNum() {
        return num;
    }

    public String getMaterial() {
        return material;
    }
}

class Roof {
    String material;
    public String getMaterial() {
        return material;
    }
}

class Doors {
    int num;
    String material;

    public int getNum() {
        return num;
    }

    public String getMaterial() {
        return material;
    }
}

class Windows {
    int num;

    public int getNum() {
        return num;
    }

}