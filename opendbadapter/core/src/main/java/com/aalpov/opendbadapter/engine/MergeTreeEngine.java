package com.aalpov.opendbadapter.engine;

import com.aalpov.opendbadapter.engine.family.MergeTreeFamily;

public class MergeTreeEngine extends MergeTreeFamily {

    @Override
    public String toSqlDefinition() {
        return "MergeTree()";
    }

    @Override
    public String toSqlString() {
        return toSqlDefinition();
    }
}
