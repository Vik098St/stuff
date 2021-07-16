public class FurnitureShop {
    public static void main(String[] args) {
        FurnitureFactory furnitureFactory  = new ModernFurnitureFactory();
        Chair chair = furnitureFactory.createChair();
        Table table = furnitureFactory.createTable();
        Sofa sofa = furnitureFactory.createSofa();

        System.out.println("Furniture in your order:");
        chair.hasLags();
        table.eatOn();
        sofa.isComfortable();
    }
}
