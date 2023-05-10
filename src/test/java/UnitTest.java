import app.Line;
import app.Point;
import app.Rectangle;
import app.Task;
import misc.CoordinateSystem2d;
import misc.Vector2d;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Класс тестирования
 */
public class UnitTest {


    /**
     * Этот тест проверяет, правильно ли метод getIntersLines() находит пересечение прямых.
     */
    @Test
    public void test1() {


        Line l1 = new Line(new Vector2d(1, -4), new Vector2d(1, 2));
        Line l2 = new Line(new Vector2d(0, 0), new Vector2d(2, 2));

        Vector2d i = l1.getIntersLines(l2);

        assert Math.abs(i.x-1) < 0.001 && Math.abs(i.y-1) < 0.001;
    }

    /**
     * Этот тест проверяет, правильно ли метод getIntersLines() находит пересечение прямых.
     */
    @Test
    public void test2() {


        Line l1 = new Line(new Vector2d(1, 1), new Vector2d(2, 2));
        Line l2 = new Line(new Vector2d(2, 1), new Vector2d(1, 2));

        Vector2d i = l1.getIntersLines(l2);

        assert Math.abs(i.x-1.5) < 0.001 && Math.abs(i.y-1.5) < 0.001;
    }

    /**
     * Этот тест проверяет, правильно ли метод getDistance() находит расстояние от точки до прямой.
     */
    @Test
    public void test3() {



        Vector2d p = new Vector2d(0, 0);
        Line l = new Line(new Vector2d(2, 1), new Vector2d(1, 2));

        double x = l.getDistance(p);

        assert Math.abs(x-2.1213) < 0.001;
    }

    /**
     * Этот тест проверяет, правильно ли метод getIntersLines() находит пересечение прямых.
     */
    @Test
    public void test4() {



        Line l1 = new Line(new Vector2d(0, 2), new Vector2d(5, 0));
        Line l2 = new Line(new Vector2d(0, 0), new Vector2d(8, 4));


        Vector2d x= l1.getIntersLines(l2);

        assert Math.abs(x.x - 2.2222) < 0.001 && Math.abs(x.y - 1.1111) < 0.001;
    }

    /**
     * Этот тест проверяет, правильно ли метод isIntersRect() определяет принадлежность точки к прямоугольнику.
     */
    @Test
    public void test5() {



        Vector2d p = new Vector2d(0, 0);
        Rectangle rectangle = new Rectangle(new Vector2d(-2, -1),
                new Vector2d(-2, 3), new Vector2d(5, 3), new Vector2d(5, -1),
                new Line(new Vector2d(-2, -1), new Vector2d(-2, 3)),
                new Line(new Vector2d(-2, 3), new Vector2d(5, 3)),
                new Line(new Vector2d(5, 3), new Vector2d(5, -1)),
                new Line(new Vector2d(5, -1), new Vector2d(-2, -1)));

        assert p.isIntersRect(rectangle);
    }

    /**
     * Этот тест проверяет, правильно ли метод isIntersRect() определяет принадлежность точки к прямоугольнику.
     */
    @Test
    public void test6() {



        Vector2d p = new Vector2d(0, 0);
        Rectangle rect = new Rectangle(new Vector2d(-0.39, -2.19), new Vector2d(3.89, 0.27),
                new Vector2d(2.20, 3.22), new Vector2d(-2.08, 0.76),
                new Line(new Vector2d(-0.39, -2.19), new Vector2d(3.89, 0.27)),
                new Line(new Vector2d(3.89, 0.27), new Vector2d(2.20, 3.22)),
                new Line(new Vector2d(2.20, 3.22), new Vector2d(-2.08, 0.76)),
                new Line(new Vector2d(-2.08, 0.76), new Vector2d(-0.39, -2.19)));


        assert p.isIntersRect(rect);
    }

    /**
     * Этот тест проверяет, правильно ли метод getAreaIntersRect() находит площадь пересечения прямоугольников.
     */
    @Test
    public void test7() {


        Rectangle rectangle1 = new Rectangle(new Vector2d(3.69, 1.46),
                new Vector2d(2.65, -3.18), new Vector2d(-0.78, -2.41), new Vector2d(0.26, 2.23),
                new Line(new Vector2d(3.69, 1.46), new Vector2d(2.65, -3.18)),
                new Line(new Vector2d(2.65, -3.18), new Vector2d(-0.78, -2.41)),
                new Line(new Vector2d(-0.78, -2.41), new Vector2d(0.26, 2.23)),
                new Line(new Vector2d(0.26, 2.23), new Vector2d(3.69, 1.46)));

        Rectangle rectangle2 = new Rectangle(new Vector2d(3.04, -5.37), new Vector2d(0.17, -2.22),
                new Vector2d(2.38, -0.20), new Vector2d(5.25, -3.35),
                new Line(new Vector2d(3.04, -5.37), new Vector2d(0.17, -2.22)),
                new Line(new Vector2d(0.17, -2.22), new Vector2d(2.38, -0.20)),
                new Line(new Vector2d(2.38, -0.20), new Vector2d(5.25, -3.35)),
                new Line(new Vector2d(5.25, -3.35), new Vector2d(3.04, -5.37)));


        Task task = new Task(new CoordinateSystem2d(10, 10, 20, 20),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        assert Math.abs(task.getAreaIntersRect(rectangle1, rectangle2, 2)- 4.98297) < 0.001;
    }

    /**
     * Этот тест проверяет, правильно ли решена задача.
     */
    @Test
    public void test8() {


        Rectangle rectangle1 = new Rectangle(new Vector2d(3.69, 1.46), new Vector2d(2.65, -3.18),
                new Vector2d(-0.78, -2.41), new Vector2d(0.26, 2.23),
                new Line(new Vector2d(3.69, 1.46), new Vector2d(2.65, -3.18)),
                new Line(new Vector2d(2.65, -3.18), new Vector2d(-0.78, -2.41)),
                new Line(new Vector2d(-0.78, -2.41), new Vector2d(0.26, 2.23)),
                new Line(new Vector2d(0.26, 2.23), new Vector2d(3.69, 1.46)));

        Rectangle rectangle2 = new Rectangle(new Vector2d(3.04, -5.37), new Vector2d(0.17, -2.22),
                new Vector2d(2.38, -0.20), new Vector2d(5.25, -3.35),
                new Line(new Vector2d(3.04, -5.37), new Vector2d(0.17, -2.22)),
                new Line(new Vector2d(0.17, -2.22), new Vector2d(2.38, -0.20)),
                new Line(new Vector2d(2.38, -0.20), new Vector2d(5.25, -3.35)),
                new Line(new Vector2d(5.25, -3.35), new Vector2d(3.04, -5.37)));

        Rectangle rectangle3 = new Rectangle(new Vector2d(-1.56, -0.60), new Vector2d(-1.16, 4.13),
                new Vector2d(5.84, 3.54), new Vector2d(5.44, -1.19),
                new Line(new Vector2d(-1.56, -0.60), new Vector2d(-1.16, 4.13)),
                new Line(new Vector2d(-1.16, 4.13), new Vector2d(5.84, 3.54)),
                new Line(new Vector2d(5.84, 3.54), new Vector2d(5.44, -1.19)),
                new Line(new Vector2d(5.44, -1.19), new Vector2d(-1.56, -0.60)));

        ArrayList<Point> points = new ArrayList<>();

        points.add(new Point(new Vector2d(3.69, 1.46)));
        points.add(new Point(new Vector2d(2.65, -3.18)));
        points.add(new Point(new Vector2d(-0.78, -2.41)));
        points.add(new Point(new Vector2d(0.26, 2.23)));

        points.add(new Point(new Vector2d(3.04, -5.37)));
        points.add(new Point(new Vector2d(0.17, -2.22)));
        points.add(new Point(new Vector2d(2.38, -0.20)));
        points.add(new Point(new Vector2d(5.25, -3.35)));

        points.add(new Point(new Vector2d(-1.56, -0.60)));
        points.add(new Point(new Vector2d(-1.16, 4.13)));
        points.add(new Point(new Vector2d(5.84, 3.54)));
        points.add(new Point(new Vector2d(5.44, -1.19)));

        ArrayList<Line> lines = new ArrayList<>();
        lines.add(new Line(new Vector2d(3.69, 1.46), new Vector2d(2.65, -3.18)));
        lines.add(new Line(new Vector2d(2.65, -3.18), new Vector2d(-0.78, -2.41)));
        lines.add(new Line(new Vector2d(-0.78, -2.41), new Vector2d(0.26, 2.23)));
        lines.add(new Line(new Vector2d(0.26, 2.23), new Vector2d(3.69, 1.46)));

        lines.add(new Line(new Vector2d(3.04, -5.37), new Vector2d(0.17, -2.22)));
        lines.add(new Line(new Vector2d(0.17, -2.22), new Vector2d(2.38, -0.20)));
        lines.add(new Line(new Vector2d(2.38, -0.20), new Vector2d(5.25, -3.35)));
        lines.add(new Line(new Vector2d(5.25, -3.35), new Vector2d(3.04, -5.37)));

        lines.add(new Line(new Vector2d(-1.56, -0.60), new Vector2d(-1.16, 4.13)));
        lines.add(new Line(new Vector2d(-1.16, 4.13), new Vector2d(5.84, 3.54)));
        lines.add(new Line(new Vector2d(5.84, 3.54), new Vector2d(5.44, -1.19)));
        lines.add(new Line(new Vector2d(5.44, -1.19), new Vector2d(-1.56, -0.60)));

        ArrayList<Rectangle> rectangles = new ArrayList<>();
        rectangles.add(rectangle1);
        rectangles.add(rectangle2);
        rectangles.add(rectangle3);

        Task task = new Task(new CoordinateSystem2d(10, 10, 20, 20), points,
                lines, rectangles, new ArrayList<>());

        assert Math.abs(task.getAreaIntersRect(rectangle1, rectangle2, 2) - 4.98297) < 0.001;

        assert Math.abs(task.getAreaIntersRect(rectangle2, rectangle3, 2) - 0.53269) < 0.001;

        assert Math.abs(task.getAreaIntersRect(rectangle1, rectangle3, 2) - 9.7005) < 0.005;

        ArrayList<Vector2d> res = new ArrayList<>();
        res.add(new Vector2d(-0.396, -0.698));
        res.add(new Vector2d(3.139, -0.997));
        res.add(new Vector2d(3.69, 1.46));
        res.add(new Vector2d(0.26, 2.23));
        for (int i = 0; i < 4; i++) {
            assert Math.abs(res.get(i).x - task.solve1().get(i).x) < 0.001;
            assert Math.abs(res.get(i).y - task.solve1().get(i).y) < 0.001;
        }
    }

}
