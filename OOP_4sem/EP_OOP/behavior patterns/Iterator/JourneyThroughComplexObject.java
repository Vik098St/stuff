package Iterator;

public class JourneyThroughComplexObject {
    public static void main(String[] args) {
        String[] elements = {"first","second","third","last"};
        SomeComplexObject complexObject = new SomeComplexObject("ArrayOfStrings",elements);

        Iterator iterator = complexObject.getIterator();
        System.out.println("Object name" + complexObject.getDescription());
        System.out.println("Elements:");

        while (iterator.hasNext()){
            System.out.println(iterator.next().toString() + " ");
        }
    }
}
