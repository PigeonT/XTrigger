package de.darmstadt.tu.kom.XTrigger.datajavadao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.darmstadt.tu.kom.XTrigger.config.Config;
import de.darmstadt.tu.kom.XTrigger.datamodel.HistroyData;
import de.darmstadt.tu.kom.XTrigger.helper.Constants;
import de.darmstadt.tu.kom.XTrigger.helper.Helper;

public final class HistroyDataDAO extends SQLiteOpenHelper implements IDAO<HistroyData> {


    private static String KEY_ID;
    private static String DATABASE;
    private static String UPGRADE_TABLE;
    private static Context context;
    private static String TABLE;
    private static String CREATE_TABLE;
    private SQLiteDatabase db;
    private static String KEY_TIME;
    private static String KEY_WEEKDAY;
    private static String KEY_WEATHER;
    private static String KEY_TEMPERATURE;
    private static String KEY_POSITIONGPSTUPLE;
    private static String KEY_POSITIONIDENTITY;
    private static String KEY_CREATEDATE;
    private static String KEY_CHOSEN;

    private HistroyDataDAO(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    public HistroyDataDAO() {
        this(Constants.CONTEXT);

        HistroyDataDAO.context = Constants.CONTEXT;
        HistroyDataDAO.DATABASE = Constants.DATABASE_NAME;
        HistroyDataDAO.TABLE = Constants.TABLE_NAME;
        HistroyDataDAO.CREATE_TABLE = Config.ON_CREATE_TABLE;
        HistroyDataDAO.UPGRADE_TABLE = Config.ON_UPGRADE_TABLE;
        HistroyDataDAO.KEY_TIME = Constants.COLUMN_TIME;
        HistroyDataDAO.KEY_WEEKDAY = Constants.COLUMN_WEEKDAY;
        HistroyDataDAO.KEY_WEATHER = Constants.COLUMN_WEATHER;
        HistroyDataDAO.KEY_TEMPERATURE = Constants.COLUMN_TEMPERATURE;
        HistroyDataDAO.KEY_POSITIONGPSTUPLE = Constants.COLUMN_POSITIONGPSTUPLE;
        HistroyDataDAO.KEY_POSITIONIDENTITY = Constants.COLUMN_POSITIONIDENTITY;
        HistroyDataDAO.KEY_CREATEDATE = Constants.COLUMN_CREATEDATE;
        HistroyDataDAO.KEY_ID = Constants.COLUMN_ID;
        HistroyDataDAO.KEY_CHOSEN = Constants.COLUMN_CHOSEN;
    }

    @Override
    public void create(HistroyData _histroyData) {

        HistroyData histroyData = doCompareHistroyData(_histroyData);

        ContentValues cv = new ContentValues();

        cv.put(HistroyDataDAO.KEY_TIME, histroyData.getTime());
        cv.put(HistroyDataDAO.KEY_WEEKDAY, histroyData.getWeekday());
        cv.put(HistroyDataDAO.KEY_WEATHER, histroyData.getWeather());
        cv.put(HistroyDataDAO.KEY_TEMPERATURE, histroyData.getTemperature());
        cv.put(HistroyDataDAO.KEY_POSITIONGPSTUPLE, histroyData.getPositionGPSTuple());
        cv.put(HistroyDataDAO.KEY_POSITIONIDENTITY, histroyData.getPositionIdentity());
        cv.put(HistroyDataDAO.KEY_CHOSEN, histroyData.getChosen());
        cv.put(HistroyDataDAO.KEY_CREATEDATE, histroyData.getCreateDate());

        try {
            //insert value into database
            db = this.getWritableDatabase();
            db.insert(HistroyDataDAO.TABLE, null, cv);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();

        }
    }

    public HistroyData doCompareHistroyData(HistroyData _histroyData) {

        try {
            db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(Config.ON_QUERY, null);

            if (cursor.getCount() >= 1) {

                while (cursor.moveToNext()) {
                    int gpsIdentity;

                    double database_lat = Double.parseDouble(cursor.getString(5).split(",")[0]);
                    double database_lon = Double.parseDouble(cursor.getString(5).split(",")[1]);
                    double lat = Double.parseDouble(_histroyData.getPositionGPSTuple().split(",")[0]);
                    double lon = Double.parseDouble(_histroyData.getPositionGPSTuple().split(",")[1]);

                    //two points distance smaller than 100 meters? identity is the same (one point basically) : two points (do nothing)
                    if (Helper.distance(database_lat, lat, database_lon, lon, 0, 0) < 100) {
                        gpsIdentity = cursor.getInt(6);
                        _histroyData.setPositionIdentity(gpsIdentity);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return _histroyData;
        } finally {
            db.close();
        }
        return _histroyData;
    }

    //get the first data set
    @Override
    public HistroyData get() {
        HistroyData _histroyData = new HistroyData();
        db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(Config.ON_QUERY, null);


            if (cursor.getCount() >= 1) {
                if (cursor.moveToFirst()) {
                    _histroyData.setId(cursor.getInt(0))
                            .setTime(cursor.getString(1))
                            .setWeekday(cursor.getString(2))
                            .setWeather(cursor.getString(3))
                            .setTemperature(cursor.getString(4))
                            .setPositionGPSTuple(cursor.getString(5))
                            .setPositionIdentity(cursor.getInt(6))
                            .setChosen(cursor.getString(7))
                            .setCreateDate(cursor.getLong(8));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return _histroyData;
        } finally {
            db.close();
        }
        return _histroyData;

    }

    //get all dataset
    @Nullable
    public List<HistroyData> getAllHistroyData() {
        List<HistroyData> histroyDataList = new ArrayList<>();

        try {
            db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(Config.ON_QUERY, null);
            if (cursor.getCount() >= 1) {
                while (cursor.moveToNext()) {
                    HistroyData h = new HistroyData();
                    h.setId(cursor.getInt(0))
                            .setTime(cursor.getString(1))
                            .setWeekday(cursor.getString(2))
                            .setWeather(cursor.getString(3))
                            .setTemperature(cursor.getString(4))
                            .setPositionGPSTuple(cursor.getString(5))
                            .setPositionIdentity(cursor.getInt(6))
                            .setChosen(cursor.getString(7))
                            .setCreateDate(cursor.getLong(8));

                    histroyDataList.add(h);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            db.close();
        }
        return histroyDataList;
    }

    @Override
    public void delete(HistroyData histroyData) {
        db = this.getReadableDatabase();
        try {
            int result = db.delete(HistroyDataDAO.TABLE, "id=?", new String[]{String.valueOf(histroyData.getId())});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    @Override
    public void update(HistroyData _histroyData) {
        db = this.getReadableDatabase();
        try {

            ContentValues contentValues = new ContentValues();
            contentValues.put(HistroyDataDAO.KEY_ID, _histroyData.getId());
            int result = db.update(HistroyDataDAO.TABLE, contentValues, "id=?", new String[]{String.valueOf(_histroyData.getId())});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    //0 means remove all records
    public boolean removeAll() {
        db = this.getReadableDatabase();
        try {
            int result = db.delete(HistroyDataDAO.TABLE, null, null);

            return result == 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    @Nullable
    public HistroyData get(int id) {
        HistroyData histroyData = new HistroyData();
        try {
            db = this.getReadableDatabase();

            Cursor cursor = db.query(

                    HistroyDataDAO.TABLE,
                    null,
                    "id=?",
                    new String[]{String.valueOf(id)},
                    null,
                    null,
                    HistroyDataDAO.KEY_ID);

            if (cursor.getCount() >= 1) {
                if (cursor.moveToFirst()) {
                    histroyData.setId(cursor.getInt(0))
                            .setTime(cursor.getString(1))
                            .setWeekday(cursor.getString(2))
                            .setWeather(cursor.getString(3))
                            .setTemperature(cursor.getString(4))
                            .setPositionGPSTuple(cursor.getString(5))
                            .setPositionIdentity(cursor.getInt(6))
                            .setChosen(cursor.getString(7))
                            .setCreateDate(cursor.getLong(8));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            db.close();
        }
        return histroyData;

    }

    //deprecated should use create API instead
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(HistroyDataDAO.CREATE_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //deprecated should use create API instead
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("Upgrading database", String.format("Upgrading database from %d to %d", oldVersion, newVersion));
        try {
            db.execSQL(HistroyDataDAO.UPGRADE_TABLE);
            onCreate(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getCount() {
        try {
            db = getReadableDatabase();
            return  db.rawQuery(Config.ON_QUERY, null).getCount();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            db.close();
        }

        return 0;
    }
}
