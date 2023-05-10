package app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import misc.Vector2d;

import java.util.ArrayList;

/**
 * Класс линии
 */
public class Rectangle {
    /**
     * Первая опорная точка
     */
    public final Vector2d pointA;
    /**
     * Вторая опорная точка
     */
    public final Vector2d pointB;
    /**
     * Третья опорная точка
     */
    public final Vector2d pointC;
    /**
     * Четвертая опорная точка
     */
    public final Vector2d pointD;
    /**
     * Первый отрезок
     */
    public final Line AB;
    /**
     * Второй отрезок
     */
    public final Line BC;
    /**
     * Третий отрезок
     */
    public final Line CD;
    /**
     * Четвертый отрезок
     */
    public final Line DA;

    /**
     * Конструктор прямоугольника
     *
     * @param pointA первая опорная точка
     * @param pointB вторая опорная точка
     * @param pointC третья опорная точка
     * @param pointD четвёртая опорная точка
     * @param AB первый отрезок
     * @param BC второй отрезок
     * @param CD третий отрезок
     * @param DA четвёртый отрезок
     */
    @JsonCreator
    public Rectangle(@JsonProperty("pointA") Vector2d pointA, @JsonProperty("pointB") Vector2d pointB,
                     @JsonProperty("pointC") Vector2d pointC, @JsonProperty("pointD") Vector2d pointD,
                     @JsonProperty("AB") Line AB, @JsonProperty("BC") Line BC,
                     @JsonProperty("CD") Line CD, @JsonProperty("DA") Line DA) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.pointC = pointC;
        this.pointD = pointD;

        this.AB = AB;
        this.BC = BC;
        this.CD = CD;
        this.DA = DA;


    }

    /**
     * Строковое представление объекта
     *
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return "Rectangle{" +
                "A=" + pointA +
                ", B=" + pointB +
                ", C=" + pointC +
                ", D=" + pointD +
                ", AB=" + AB +
                ", BC=" + BC +
                ", CD=" + CD +
                ", DA=" + DA +
                '}';
    }
    /**
     * добавлен в решение
     */
    public void addtoansR () {
        AB.addToAns(); BC.addToAns(); CD.addToAns(); DA.addToAns();
    }
    /**
     * получить список отрезков
     */
    @JsonIgnore
    public ArrayList<Line> getListLines() {
        ArrayList<Line> ans = new ArrayList<>();
        ans.add(AB); ans.add(BC); ans.add(CD); ans.add(DA);
        return ans;
    }
    /**
     * получить список точек
     */
    @JsonIgnore
    public ArrayList<Vector2d> getListPoints() {
        ArrayList<Vector2d> ans = new ArrayList<>();
        ans.add(pointA); ans.add(pointB); ans.add(pointC); ans.add(pointD);
        return ans;
    }
    /**
     * получить нулевой прямоугольник
     */
    public static Rectangle nullRectangle() {
        return new Rectangle(new Vector2d(0, 0), new Vector2d(0, 0),
                new Vector2d(0, 0), new Vector2d(0, 0),
                new Line(new Vector2d(0, 0), new Vector2d(0, 0)),
                new Line(new Vector2d(0, 0), new Vector2d(0, 0)),
                new Line(new Vector2d(0, 0), new Vector2d(0, 0)),
                new Line(new Vector2d(0, 0), new Vector2d(0, 0)));
    }
}