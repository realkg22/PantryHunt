package com.example.kyl3g.sunhacksnov2018.Search;

import com.example.kyl3g.sunhacksnov2018.Objects.Grocery;

import java.util.Comparator;


public class DistanceComparator implements Comparator<Grocery> {
    @Override
    public int compare(Grocery o1, Grocery o2) {

        int result = 0;
        double compareNum = o1.getDistance() - o2.getDistance();
        if (compareNum < 0)
        {
            result = -1;
        }

        else if (compareNum > 0)
        {
            result = 1;
        }
        else if (compareNum == 0)
        {
            result = 0;
        }

        return result;
    }
}
