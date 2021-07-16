public class Cell implements Copyable {
    private String type;
    private int divisionSpeed;
    Cell(String type,int divSpeed ){
        this.type = type;
        this.divisionSpeed = divSpeed;
    }
    public Cell copy(){
        return new Cell(type,divisionSpeed);
    }

    public void setType(String type) {
        this.type = type;
    }

    public void showInfo() {
        System.out.println("Cell info: \n " + type + "cell with" + divisionSpeed + "per/sec speed of division");
    }
}
