package com.sadiwala.shivam.models.common;

public class CodeName implements ICodeName {
    private String code;
    private String name;
    private String type;
    private String startState;
    private String description;


    public CodeName(String code, String name) {
        this(code, name, null);
    }


    public CodeName(String code, String name, String type) {
        this.type = type;
        this.code = code;
        this.name = name;
    }


    public CodeName() {
    }


    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getStartState() {
        return startState;
    }

    public void setStartState(String startState) {
        this.startState = startState;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
