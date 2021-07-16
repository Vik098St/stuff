public class Human implements Copyable{
    private int age;
    private String name;

    Human(int years, String name){
        this.age = years;
        this.name = name;
    }

    public Human copy(){
        return new Human(age,name);
    }

    public void showInfo() {
        System.out.println("Human info: \n " + name + ", is" + age + "years old");
    }
}
