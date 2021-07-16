public interface HouseBuilderPlan {
   /** план действий для строителя (связь между строителем и прорабом)**/
    HouseBuilderPlan setNumOfFloors(int count);
    HouseBuilderPlan buildWalls(int count, String material);
    HouseBuilderPlan buildDoors(int count, String material);
    HouseBuilderPlan buildRoof(String material);
    HouseBuilderPlan buildWindows(int count);
    HouseBuilderPlan buildGarage();
    HouseBuilderPlan buildSwimmingPool();
    HouseBuilderPlan settleGarden();
    void reset();
}

