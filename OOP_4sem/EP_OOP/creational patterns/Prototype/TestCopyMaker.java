public class TestCopyMaker {
    public static void main(String[] args) {
        Cell prototype = new Cell("epithelium",100);
        Cell cellClone = (Cell) prototype.copy();
        cellClone.showInfo();

        cellClone.setType("muscular");
        Cell clone = (Cell) cellClone.copy();
        clone.showInfo();

        Human original = new Human(25,"Vasya");
        Human copy = (Human) original.copy();
        copy.showInfo();
    }
}
