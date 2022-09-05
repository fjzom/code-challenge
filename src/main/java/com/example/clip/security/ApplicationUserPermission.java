package com.example.clip.security;

public enum ApplicationUserPermission {

	   ADMIN_READ("admin:read"),
	    ADMIN_WRITE("admin:write");
	
	private final String permission;

		ApplicationUserPermission(String permission) {
			// TODO Auto-generated constructor stub
			this.permission = permission;
		}
		
		public String getPermission() {
			return permission;
		}
}
