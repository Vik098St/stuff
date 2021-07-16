/** наш строитель (лучший в своём деле!)**/
public class StahanovTheHouseBuilder implements HouseBuilderPlan {
    private House house;

    /** начать строительство нового дома **/
    public void reset() {
        house = new House();
    }

    @Override
    public HouseBuilderPlan setNumOfFloors(int count){
        if (house != null) {
            house.setFloors(count);
        }
        return this;
    }

    @Override
    public HouseBuilderPlan buildWalls(int count, String material) {
        if (house != null) {
            house.setWalls(count, material);
        }
        return this;
    }
    @Override
    public HouseBuilderPlan buildDoors(int count, String material) {
        if (house != null) {
            house.setDoors(count, material);
        }
        return this;
    }
    @Override
    public HouseBuilderPlan buildRoof(String material) {
        if (house != null) {
            house.setRoof(material);
        }
        return this;
    }
    @Override
    public HouseBuilderPlan buildWindows(int count) {
        if (house != null) {
            house.setWindows(count);
        }
        return this;
    }
    @Override
    public HouseBuilderPlan buildGarage() {
        if (house != null) {
            house.setHasGarage(true);
        }
        return this;
    }
    @Override
    public HouseBuilderPlan buildSwimmingPool() {
        if (house != null) {
            house.setHasSwimmingPool(true);
        }
        return this;
    }

    @Override
    public HouseBuilderPlan settleGarden() {
        if (house != null)house.setHasGarden(true);
        return this;
    }

    /** Сдача построенного объекта **/
    public House getResult() {
        return house;
    }

}
