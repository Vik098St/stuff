public class DirectorBob {
    /** Поле, хранящее интерфейс взаимодействия со сторителем (т.е. пошаговый план строительства) **/
    private HouseBuilderPlan builder;
    /** создание прораба Боба для выдачи указаний к постройке различных проектов домов **/
    public DirectorBob(HouseBuilderPlan _builder) {
        builder = _builder;
    }
    /** Заплатить Бобу за коттедж с бассейном и садои **/
    public void makeCottage() {
        builder.reset();
        builder.setNumOfFloors(1).buildWalls(4, "Wood").buildDoors(1, "Wood").buildRoof("Wood").buildWindows(3).buildSwimmingPool().settleGarden();
    }
    /** Заплатить Бобу за многоэтажку **/
    public void makeApartmentBuilding() {
        builder.reset();
        builder.setNumOfFloors(5).buildWalls(4, "Stone & Iron").buildDoors(5, "Iron").buildRoof("Tile").buildWindows(40).buildGarage();
    }
}

