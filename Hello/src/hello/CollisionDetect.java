package hello;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.geom.Area;

import javax.swing.JFrame;

// From: http://stackoverflow.com/questions/15690846/
//              java-collision-detection-between-two-shape-objects

/**
 * Even though user2221343 already answered Monkeybro10's question, 
 * I thought it might be helpful in some cases to know, that the outline 
 * of a shape might play a role if you use his described technique: 
 * 
 * For example, if you draw two polygons, the collision of them 
 * won't be detected if it occurs only on the exact outline of the polygons. 
 * Only, if the areas that are included inside the polygons' outlines 
 * will overlap, the collision is detected. If you fill two polygons, 
 * but don't draw them, the collision will be detected even 
 * on the outline of the visible area. 
 * 
 * I wrote a small example to show what I mean. Either uncomment the draw or 
 * fill command, and rise the second polygon vertically by one pixel 
 * by uncommenting the given line. Run the code and watch the result 
 * in the JFrame. If the second Polygon is risen, and both polygons 
 * are only visible by the "fill" command, they intersect with their outlines 
 * and collision is detected. If the second polygon is not risen, 
 * and both polygons are visible by the "draw" command, they intersect 
 * with their outlines but collision is not detected:
 *
 */

public class CollisionDetect {

    private JFrame frame;
    private Polygon polygon1;
    private Polygon polygon2;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	CollisionDetect window = new CollisionDetect();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public CollisionDetect() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame(){
            private static final long serialVersionUID = 1L;

            @Override
            public void paint(Graphics g){

                super.paint(g);

                doDrawing(g);

            }
        };
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int nShape1 = 4;
        int xPoly1[] = {30,50,50,30};
        int yPoly1[] = {30,30,50,50};
        polygon1 = new Polygon(xPoly1,yPoly1,nShape1);

        int nShape2 = 4;
        int xPoly2[] = {35,55,55,35};
        int yPoly2[] = {50,50,70,70};

        // uncomment next line to rise second polygon vertically by one pixel
        //int yPoly2[] = {49,49,69,69};

        polygon2 = new Polygon(xPoly2,yPoly2,nShape2);
    }
    public synchronized void doDrawing(Graphics g){
        g.setColor(new Color(255,0,0));

        // if you draw the polygon, collision on the exact outline won't be detected.
        // uncomment draw or fill command to see what I mean.
        g.drawPolygon(polygon1);
        //g.fillPolygon(polygon1);

        g.setColor(new Color(0,0,255));

        // if you draw the polygon, collision on the exact outline won't be detected.
        // uncomment draw or fill command to see what I mean.
        g.drawPolygon(polygon2);
        //g.fillPolygon(polygon2);

        Area area = new Area(polygon1);
        area.intersect(new Area(polygon2));
        if(!area.isEmpty()){
            System.out.println("intersects: yes");
        }
        else{
            System.out.println("intersects: no");
        }
    }

}