package app;

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
     * Конструктор линии
     *
     * @param pointA первая опорная точка
     * @param pointB вторая опорная точка
     */
    public Line(Vector2d pointA, Vector2d pointB) {
        this.pointA = pointA;
        this.pointB = pointB;

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
        return "Point{" +
                "fps=" + pointA +
                ", sps=" + pointB +
                '}';
    }
    /**
     * Получить цвет отрезка по его множеству
     *
     * @return цвет отрезка
     */
    public int getColor() {
        return Misc.getColor(0xCC, 0xFF, 0xFF, 0xFF);
    }
}