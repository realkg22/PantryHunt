package com.example.kyl3g.sunhacksnov2018.Search;

import com.example.kyl3g.sunhacksnov2018.Objects.SumTag;

import java.util.Comparator;

public class DeltaSumComparator implements Comparator<SumTag> {

    @Override
    public int compare(SumTag o1, SumTag o2) {
        return (int)(o1.getSum() - o2.getSum());
    }
}
