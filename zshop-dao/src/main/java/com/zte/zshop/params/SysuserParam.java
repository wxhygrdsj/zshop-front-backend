package com.zte.zshop.params;

/**
 * Author:helloboy
 * Date:2021-06-15 14:42
 * Description:用于封装表单中需要查询的数据
 */
public class SysuserParam {

    private String name;

    private String loginName;


    private String phone;


    private Integer roleId;



    private Integer isValid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }
}
