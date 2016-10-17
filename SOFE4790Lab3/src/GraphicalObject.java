import java.awt.Rectangle;
import java.awt.Color;
import java.io.Serializable;

public class GraphicalObject implements Serializable {
	private static final long serialVersionUID = 1L;
	public String type;
	public Rectangle enclosing;
	public Color line;
	public Color fill;
	public boolean isFilled;

	public GraphicalObject() {}
	
	public GraphicalObject(String t, Rectangle e, Color l, Color f, boolean x) {
		type = t;
		enclosing = e;
		line = l;
		fill = f;
		isFilled = x;
	}

	public void print() {
		System.out.print(type + " ");
		System.out.print(enclosing.x + ", " + enclosing.y + ", " + enclosing.width
			+ ", " + enclosing.height + " ");
		if(isFilled) System.out.println("filled"); 
		else System.out.println("not filled");
	}
}
