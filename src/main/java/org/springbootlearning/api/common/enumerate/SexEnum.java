package org.springbootlearning.api.common.enumerate;

public enum SexEnum {

    Unknown(0,"未知"),Male(1, "男"), Female(2, "女");

    private final int code;
    private final String desc;

    private SexEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return this.name() + "[code:" + this.code + ",desc:" + desc + "]";
    }

    public static SexEnum valueOf(int code) {
        for (SexEnum e : SexEnum.values()) {
            if (e.getCode() == code) {
                return e;
            }
        }
        return null;
    }
    
    public static SexEnum fromDesc(String desc) {
        for (SexEnum e : SexEnum.values()) {
            if (e.desc.equals(desc)) {
                return e;
            }
        }
        return null;
    }
    
    public static SexEnum fromCodeStr(String codeStr) {
        for (SexEnum e : SexEnum.values()) {
            if (String.valueOf(e.code).equals(codeStr)) {
                return e;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(SexEnum.Unknown);
        System.out.println(SexEnum.Male);
        System.out.println(SexEnum.Female);
        System.out.println(SexEnum.valueOf(0));
        System.out.println(SexEnum.fromDesc("未知"));
        System.out.println(SexEnum.fromDesc("男"));
        System.out.println(SexEnum.fromDesc("seterwer"));
        System.out.println(SexEnum.fromDesc("女"));
        System.out.println(SexEnum.fromCodeStr("1"));
    }
    
}
