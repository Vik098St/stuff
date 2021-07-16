package Iterator;

public class SomeComplexObject implements Collection{
    private  String Description;
    private String[] elements;

    public SomeComplexObject(String description, String[] elements) {
        Description = description;
        this.elements = elements;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String[] getElements() {
        return elements;
    }

    public void setElements(String[] elements) {
        this.elements = elements;
    }

    @Override
    public Iterator getIterator() {
        return new ElementsIterator();
    }

    private class ElementsIterator implements  Iterator{
        int index;
        @Override
        public boolean hasNext() {
            return index < elements.length;
        }

        @Override
        public Object next() {
            return elements[index++];
        }
    }
}
