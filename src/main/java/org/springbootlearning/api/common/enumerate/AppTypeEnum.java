package org.springbootlearning.api.common.enumerate;


public enum AppTypeEnum {
    
	Android(1),IOS(2),PC(3);
	
	private final int value;
	
	private AppTypeEnum(int value){
		this.value = value;
	}
	
	public Integer getValue() {
		return this.value;
	}

	
	@Override
	public String toString() {
		return this.name() +"(" + this.value+")";
	}
	
    public static AppTypeEnum valueOf(int value) {
        for (AppTypeEnum  e : AppTypeEnum.values()) {
            if (e.getValue() == value) {
                return e;
            }
        }
        return null;
    }
	
}
