package com.example.clip.security;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import static com.example.clip.security.ApplicationUserPermission.*;
public enum ApplicationUserRole {

    ADMIN(Stream.of(ADMIN_READ, ADMIN_WRITE).collect(Collectors.toSet()));
	private final Set<ApplicationUserPermission> permissions;
	ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
		// TODO Auto-generated constructor stub
	this.permissions = permissions;
	}
	
	public Set<ApplicationUserPermission> getPermissions(){
		return permissions;
	}
}
