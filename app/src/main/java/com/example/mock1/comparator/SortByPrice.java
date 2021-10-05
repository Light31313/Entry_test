package com.example.mock1.comparator;

import com.example.mock1.entity.PhuongTien;

import java.util.Comparator;

public class SortByPrice implements Comparator<PhuongTien> {
    @Override
    public int compare(PhuongTien phuongTien, PhuongTien t1) {
        if (phuongTien.getPrice() == t1.getPrice())
            return 0;
        else if (phuongTien.getPrice() > t1.getPrice())
            return 1;
        return -1;
    }
}
