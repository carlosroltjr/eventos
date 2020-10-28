package com.example.eventos_mobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.eventos_mobile.database.entity.EventosEntity;
import com.example.eventos_mobile.modelo.Evento;

import java.util.ArrayList;
import java.util.List;

public class EventosDAO {

    private DatabaseGateway databaseGateway;
    private final String SQL_LISTAR_TODOS = "SELECT * FROM " + EventosEntity.TABLE_NAME;

    public EventosDAO(Context context) {
        databaseGateway = DatabaseGateway.getInstance(context);
    }

    public boolean salvar(Evento evento) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(EventosEntity.COLUMN_NAME_NOME, evento.getNome());
        contentValues.put(EventosEntity.COLUMN_NAME_DATA, evento.getData());
        contentValues.put(EventosEntity.COLUMN_NAME_LOCAL, evento.getLocal());

        if (evento.getId() > 0) {
            return databaseGateway.getSqLiteDatabase().update(EventosEntity.TABLE_NAME,
                    contentValues,
                    EventosEntity._ID + "=?",
                    new String[]{String.valueOf(evento.getId())}) > 0;
        }

        return databaseGateway.getSqLiteDatabase()
                .insert(EventosEntity.TABLE_NAME, null, contentValues) > 0;
    }

    public List<Evento> listar() {
        List<Evento> eventos = new ArrayList<>();
        Cursor cursor = databaseGateway.getSqLiteDatabase().rawQuery(SQL_LISTAR_TODOS, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(EventosEntity._ID));
            String nome = cursor.getString(cursor.getColumnIndex(EventosEntity.COLUMN_NAME_NOME));
            String data = cursor.getString(cursor.getColumnIndex(EventosEntity.COLUMN_NAME_DATA));
            String local = cursor.getString(cursor.getColumnIndex(EventosEntity.COLUMN_NAME_LOCAL));
            eventos.add(new Evento(id, nome, data, local));
        }

        cursor.close();

        return eventos;
    }

    public void excluir(Evento evento) {
        databaseGateway.getSqLiteDatabase().delete(EventosEntity.TABLE_NAME,
                EventosEntity._ID + " LIKE ?", new String[]{String.valueOf(evento.getId())});
    }
}
