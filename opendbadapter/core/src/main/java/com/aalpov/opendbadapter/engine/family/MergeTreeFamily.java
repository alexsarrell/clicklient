package com.aalpov.opendbadapter.engine.family;

import com.aalpov.opendbadapter.engine.Engine;

public abstract class MergeTreeFamily implements Engine {

    protected String familyModifier = "";

    abstract public String toSqlDefinition();
}

