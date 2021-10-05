package com.example.mock1.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mock1.entity.PhuongTien;

import java.util.List;

@Dao
public interface PhuongTienDao {
    @Query("select * from PhuongTien")
    List<PhuongTien> getAll();

    @Query("select * from PhuongTien where id = :id")
    PhuongTien getById(int id);

    @Insert
    void create(PhuongTien phuongTien);

    @Query("Delete from PhuongTien where id = :id")
    void deleteById(int id);

    @Query("select * from PhuongTien where price > :price")
    List<PhuongTien> getGreaterThanPrice(long price);

    @Query("Update PhuongTien set name = :name, category = :category, price = :price where id = :id")
    void updateByID(int id, String name, String category, long price);

    @Query("Select name from PhuongTien")
    String[] getAllNames();

    @Query("Select * from PhuongTien where name = :name")
    List<PhuongTien> getByName(String name);

}
