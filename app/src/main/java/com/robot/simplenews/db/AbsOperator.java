package com.robot.simplenews.db;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.robot.simplenews.entity.ImageEntity;
import com.robot.simplenews.util.LogUtil;

import java.util.List;

/**
 * 公共操作
 * 注意：表中需有_id字段
 */
public abstract class AbsOperator<T extends Model> {
    private static final String TAG = ImageOperator.class.getSimpleName();

    /**
     * 保存数据
     * @param object
     * @return
     */
    public Long add(T object) {
        return object.save();
    }

    /**
     * 批量保存数据
     * @param objList
     */
    public void addAll(List<T> objList) {
        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < objList.size(); i++) {
                T item = objList.get(i);
                item.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }

    /**
     * 删除根据_id进行删除
     * @param clazz
     * @param _id
     */
    public void deleteById(Class<T> clazz, int _id) {
        T.delete(clazz, _id);
    }

    /**
     * 删除所有记录
     */
    public void deleteAll(Class<T> clazz) {
        new Delete().from(clazz).execute();
    }

    /**
     * 删除所有记录
     */
    public void deleteAll(Class<T> clazz, String whereString) {
        new Delete()
                .from(clazz)
                .where(whereString)
                .execute();
    }

    /**
     * new Update(User.class).set("nickname='王五', address='beijing'").where("age='89' and nickname='robot'").execute();
     * @param clazz
     * @param setString
     * @param whereString
     */
    public void update(Class<ImageEntity> clazz, String setString, String whereString) {
        LogUtil.e(TAG, "update " + clazz.getSimpleName() + " set " + setString + " where " + setString);
        new Update(clazz).set(setString).where(whereString).execute();
    }

    /**
     * 查询所有数据
     * @param clazz
     * @return
     */
    public T getById(Class<T> clazz, int _id) {
        return new Select().from(clazz)
                .where("_id = ?", String.valueOf(_id))
                .executeSingle();
    }

    public T getRandom(Class<T> clazz, String whereString) {
        return new Select()
                .from(clazz)
                .where(whereString)
                .orderBy("RANDOM()")
                .executeSingle();
    }

    /**
     * 查询所有数据（升序）
     * @param clazz
     * @return
     */
    public List<T> getAllOrderByAsc(Class<T> clazz) {
        return new Select()
                .from(clazz)
                .orderBy("_id ASC")
                .execute();
    }

    /**
     * 查询所有数据（升序）
     * @param clazz
     * @return
     */
    public List<T> getAllOrderByAsc(Class<T> clazz, String whereString) {
        return new Select()
                .from(clazz)
                .where(whereString)
                .orderBy("_id ASC")
                .execute();
    }

    /**
     * 查询所有数据（倒序）
     * @param clazz
     * @return
     */
    public List<T> getAllOrderByDesc(Class<T> clazz) {
        return new Select()
                .from(clazz)
                .orderBy("_id DESC")
                .execute();
    }

    /**
     * 查询所有数据（倒序）
     * @param clazz
     * @return
     */
    public List<T> getAllOrderByDesc(Class<T> clazz, String whereString) {
        return new Select()
                .from(clazz)
                .where(whereString)
                .orderBy("_id DESC")
                .execute();
    }
}
