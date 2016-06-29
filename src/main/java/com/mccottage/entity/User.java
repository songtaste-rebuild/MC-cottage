package com.mccottage.entity;

import java.util.Date;

public class User {
	private Long id;

	private String password;

	private Long roleId;

	private Date createTime;

	private Integer isDeleted;

	private Integer userStatus;

	private String userName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName == null ? null : userName.trim();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", password=" + password + ", roleId=" + roleId + ", createTime=" + createTime
				+ ", isDeleted=" + isDeleted + ", userStatus=" + userStatus + ", userName=" + userName + "]";
	}

}