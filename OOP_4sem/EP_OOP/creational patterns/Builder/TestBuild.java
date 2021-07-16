public class TestBuild {
    public static void main(String[] args)
    {
        /** нанимаем нового строителя **/
        StahanovTheHouseBuilder builder = new StahanovTheHouseBuilder();
       /** нанимаем нового прораба и даём ему в подчинение нашего строителя **/
        DirectorBob director = new DirectorBob(builder);

        /** построить коттедж **/
        //director.makeCottage();
        /** построить многоэтажку **/
        director.makeApartmentBuilding();
        builder.getResult().getInfo();
    }
}
