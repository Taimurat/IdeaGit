package app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import misc.Misc;
import misc.Vector2d;

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
    private boolean isans;

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
        this.isans = false;

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
    public void addtoans() {
        isans = true;
    }
    /**
     * Получить цвет отрезка по его множеству
     *
     * @return цвет отрезка
     */
    public int getColor() {
        return (isans ? Misc.getColor(0xCC, 0x00, 0xFF, 0x00) : Misc.getColor(0xCC, 0xFF, 0xFF, 0xFF));
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
}