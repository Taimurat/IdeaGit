package misc;


import app.Line;
import app.Rectangle;

import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Класс двумерного вектора double
 */
public class Vector2d {
    /**
     * x - координата вектора
     */
    public double x;
    /**
     * y - координата вектора
     */
    public double y;
    /**
     * Конструктор вектора
     *
     * @param x координата X вектора
     * @param y координата Y вектора
     */
    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Конструктор вектора создаёт нулевой вектор
     */
    public Vector2d() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Сложить два вектора
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return сумма двух векторов
     */

    public static Vector2d sum(Vector2d a, Vector2d b) {
        return new Vector2d(a.x + b.x, a.y + b.y);
    }


    /**
     * Добавить вектор к текущему вектору
     *
     * @param v вектор, который нужно добавить
     */
    public void add(Vector2d v) {
        this.x = this.x + v.x;
        this.y = this.y + v.y;
    }

    /**
     * Вычесть второй вектор из первого
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return разность двух векторов
     */
    public static Vector2d subtract(Vector2d a, Vector2d b) {
        return new Vector2d(a.x - b.x, a.y - b.y);
    }

    /**
     * Вычесть вектор из текущего
     *
     * @param v вектор, который нужно вычесть
     */
    public void subtract(Vector2d v) {
        this.x = this.x - v.x;
        this.y = this.y - v.y;
    }

    /**
     * Умножение вектора на число
     *
     * @param v вектор
     * @param a число
     * @return результат умножения вектора на число
     */

    public static Vector2d mul(Vector2d v, double a) {
        return new Vector2d(v.x * a, v.y * a);
    }

    /**
     * Получить случайное значение в заданном диапазоне [min,max)
     *
     * @param min нижняя граница
     * @param max верхняя граница
     * @return случайное значение в заданном диапазоне [min,max)
     */

    public static Vector2d rand(Vector2d min, Vector2d max) {
        return new Vector2d(
                ThreadLocalRandom.current().nextDouble(min.x, max.x),
                ThreadLocalRandom.current().nextDouble(min.y, max.y)
        );
    }

    /**
     * Получить длину вектора
     *
     * @return длина вектора
     */
    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Векторное умножение векторов
     *
     * @param v второй вектор
     * @return результат умножения
     */
    public double cross(Vector2d v) {
        return this.x * v.y - this.y * v.x;
    }

    /**
     * Повернуть вектор
     *
     * @param a угол
     * @return повёрнутый вектор
     */
    public Vector2d rotated(double a) {
        return new Vector2d(
                x * Math.cos(a) - y * Math.sin(a),
                x * Math.sin(a) + y * Math.cos(a)
        );
    }

    /**
     * Нормализация вектора
     *
     * @return нормированный вектор
     */
    public Vector2d norm() {
        double length = length();
        return new Vector2d(x / length, y / length);
    }

    /**
     * Умножение вектора на число
     *
     * @return итоговый вектор
     */
    public Vector2d mult(double dist) {
        return new Vector2d(x * dist, y * dist);
    }

    /**
     * Получить целочисленный вектор
     *
     * @return целочисленный вектор
     */
    public Vector2i intVector() {
        return new Vector2i((int) x, (int) y);
    }

    /**
     * Строковое представление объекта
     *
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return "(" + String.format("%.2f", x).replace(",", ".") +
                ", " + String.format("%.2f", y).replace(",", ".") +
                ')';
    }

    /**
     * Проверка двух объектов на равенство
     *
     * @param o объект, с которым сравниваем текущий
     * @return флаг, равны ли два объекта
     */
    @Override
    public boolean equals(Object o) {
        // если объект сравнивается сам с собой, тогда объекты равны
        if (this == o) return true;
        // если в аргументе передан null или классы не совпадают, тогда объекты не равны
        if (o == null || getClass() != o.getClass()) return false;

        // приводим переданный в параметрах объект к текущему классу
        Vector2d vector2d = (Vector2d) o;

        // если не совпадают x координаты
        if (Double.compare(vector2d.x, x) != 0) return false;
        // объекты совпадают тогда и только тогда, когда совпадают их координаты
        return Double.compare(vector2d.y, y) == 0;
    }

    /**
     * Получить хэш-код объекта
     *
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /**
     * принадлежит ли точка сразу двум отрезкам
     *
     * @param f первый отрезок
     * @param s второй отрезок
     */
    public boolean isBelongSegment(Line f, Line s) {
        return max(f.pointA.x, f.pointB.x) >= x && x >= min(f.pointA.x, f.pointB.x) && max(f.pointA.y, f.pointB.y) >= y && y >= min(f.pointA.y, f.pointB.y) &&
                max(s.pointA.x, s.pointB.x) >= x && x >= min(s.pointA.x, s.pointB.x) && max(s.pointA.y, s.pointB.y) >= y && y >= min(s.pointA.y, s.pointB.y);
    }

    /**
     * принадлежит ли точка прямоугольнику
     */
    public boolean isIntersRect(Rectangle rect) {
        double width = Math.sqrt((rect.pointB.x - rect.pointC.x) * (rect.pointB.x - rect.pointC.x) + (rect.pointB.y - rect.pointC.y) * (rect.pointB.y - rect.pointC.y));
        double height = Math.sqrt((rect.pointC.x - rect.pointD.x) * (rect.pointC.x - rect.pointD.x) + (rect.pointD.y - rect.pointC.y) * (rect.pointD.y - rect.pointC.y));

        return Math.abs(rect.AB.getDistance(this) + rect.CD.getDistance(this) - width) < 0.001 &&
                Math.abs(rect.BC.getDistance(this) + rect.DA.getDistance(this) - height) < 0.001;
    }

}