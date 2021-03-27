package com.example.myapplication.database;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Permite almacenar en la BD tipos de datos complejos
 */
public class Converters {
    /**
     * Recoge la fecha de la BD
     */
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    /**
     * Almacena la fecha en BD
     * @param date
     * @return fecha
     */
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}