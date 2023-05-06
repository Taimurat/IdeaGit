package app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import misc.Misc;
import misc.Vector2d;

import java.util.Objects;

/**
 * Класс точки
 */
public class Point {
    /**
     * Координаты точки
     */
    public final Vector2d pos;
    /**
     * выделение
     */
    @JsonIgnore
    public boolean isAns;

    /**
     * Конструктор точки
     *
     * @param pos     положение точки
     */
    @JsonCreator
    public Point(@JsonProperty("pos") Vector2d pos) {
        this.pos = pos;
        isAns = false;
    }

    /**
     * Получить положение
     * (нужен для json)
     *
     * @return положение
     */
    public Vector2d getPos() {
        return pos;
    }

    /**
     * Строковое представление объекта
     *
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return "Point{" +
                "pos=" + pos +
                '}';
    }

    /**
     * Получить хэш-код объекта
     *
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(pos);
    }
    /**
     * Получить цвет точки
     *
     * @return цвет точки
     */
    @JsonIgnore
    public int getColor() {
        return isAns ? Misc.getColor(0xCC, 0xF8, 0x3B, 0xBB) : Misc.getColor(0xCC, 0x00, 0xFF, 0x0);
    }
    /**
     * пометить точку
     */
    public void addToAns() {
        isAns = true;
    }
    /**
     * убрать из ответа
     */
    public void RemoveFromAns() {
        isAns = false;
    }
}
