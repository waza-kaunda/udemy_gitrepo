package com.devopsbuddy.enums;

/**
 * Defines possible roles.
 * @author WazaK
 *
 */
public enum RolesEnum {
	/**
	 * Spring security requires roles t be prefixed with "ROLE_"
	 * therefore all roles e.g ADMIN should be ROLE_ADMIN etc. 
	 */
	BASIC(1, "ROLE_BASIC"),
	PRO(2, "ROLE_PRO"),
	ADMIN(3, "ROLE_ADMIN");
	
	private final int id;
	
	private final String roleName;
	
	RolesEnum(int id, String roleName){
		this.id = id;
		this.roleName = roleName;
	}
	
	public int getId(){return id; }
	
	public String getRoleName(){ return roleName; }

}
