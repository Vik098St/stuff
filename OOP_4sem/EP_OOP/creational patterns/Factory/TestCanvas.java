public class TestCanvas {
    public static void main(String[] args) {
        PainterFactory painterFactory = createPainterBySpeciality("landscape");
        Painter painter = painterFactory.createPainter();
        painter.paintMasterpiece();
    }
    static PainterFactory createPainterBySpeciality(String speciality){
        if(speciality.equalsIgnoreCase("portrait")){
            return new PortraitPainterFactory();
        }else if(speciality.equalsIgnoreCase("landscape")){
            return new LandscapePainterFactory();
        }
        else {
            throw new RuntimeException(speciality + "is unknown speciality");
        }
    }
}
