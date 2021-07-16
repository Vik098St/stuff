public class LandscapePainterFactory implements PainterFactory{
    @Override
    public Painter createPainter(){
        return new LandscapePainter();
    }
}
