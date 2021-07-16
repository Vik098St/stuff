public class PortraitPainterFactory implements PainterFactory{
    @Override
    public Painter createPainter(){
        return new PortraitPainter();
    }
}
