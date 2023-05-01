package app;

import com.fasterxml.jackson.annotation.JsonCreator;
import misc.Misc;
import misc.Vector2d;
import panels.PanelLog;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Класс линии
 */
public class Line {
    /**
     * Первая опорная точка
     */
    public final Vector2d pointA;
    /**
     * Вторая опорная точка
     */
    public final Vector2d pointB;
    /**
     * Первый коэффициент канонического уравнения прямой
     */
    private final double a;
    /**
     * Второй коэффициент канонического уравнения прямой
     */
    private final double c;
    /**
     * Третий коэффициент канонического уравнения прямой
     */
    private final double b;
    /**
     * принадлежит ли отрезок к ответу
     */
    private boolean isAns;

    /**
     * Конструктор линии
     *
     * @param pointA первая опорная точка
     * @param pointB вторая опорная точка
     */
    @JsonCreator
    public Line(Vector2d pointA, Vector2d pointB) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.isAns = false;

        a = pointA.y - pointB.y;
        b = pointB.x - pointA.x;
        c = pointA.x * pointB.y - pointB.x * pointA.y;
    }

    /**
     * Получить расстояние до точки
     *
     * @param pos координаты точки
     * @return расстояние
     */
    public double getDistance(Vector2d pos) {
        return Math.abs(a * pos.x + b * pos.y + c) / Math.sqrt(a * a + b * b);
    }
    /**
     * Строковое представление объекта
     *
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return "Line{" +
                "fps=" + pointA +
                ", sps=" + pointB +
                '}';
    }
    /**
     * добавить к ответу
     */
    public void addToAns() {
        isAns = true;
    }

    /**
     * добавить к ответу
     */
    public void RemoveFromAns() {
        isAns = false;
    }
    /**
     * Получить цвет отрезка по его множеству
     *
     * @return цвет отрезка
     */
    public int getColor() {
        return (isAns ? Misc.getColor(0xCC, 0x00, 0xFF, 0x00) : Misc.getColor(0xCC, 0xFF, 0xFF, 0xFF));
    }
    /**
     * пересекаются ли прямые
     */
    public boolean isIntersLines(Line other) {
        return a / b != other.a / other.b;
    }
    /**
     * получить пересечение прямых
     */
    public Vector2d getIntersLines(Line other) {
        double x = (other.c * b - c * other.b) / (a * other.b - other.a * b);
        return new Vector2d(x, - a * x / b - c / b);
    }
    /**
     * вспомогательный метод к нижнему
     */
    public boolean isInterLines(double aOther, double bOther, double cOther) {
        double x;
        if (b != 0) {
            x = (cOther * b - c * bOther) / (a * bOther - aOther * b);
            return min(pointA.x, pointB.x) < x && x < max(pointA.x, pointB.x);
        }
        x = -c / a;
        double y = -(aOther * x + cOther) / bOther;
        return min(pointA.y, pointB.y) < y && y < max(pointA.y, pointB.y);
    }
    /**
     * получить пересечение отрезка и прямой через коэффиценты
     */
    public Vector2d getInterLines(double aOther, double bOther, double cOther) {
        double x;
        if (b != 0) {
            x = (cOther * b - c * bOther) / (a * bOther - aOther * b);
            return new Vector2d(x, -a * x / b - c / b);
        }
        x = -c / a;
        double y = -(aOther * x + cOther) / bOther;
        return new Vector2d(x, y);
    }
}