package com.zte.zshop.dao;

import com.zte.zshop.entity.Sysuser;
import com.zte.zshop.params.SysuserParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Author:helloboy
 * Date:2021-06-11 8:49
 * Description:<描述>
 */
public interface SysuserDao {

    public List<Sysuser> selectAll();

    public Sysuser selectById(Integer id);

    public void update(Sysuser sysuser);

    public void insert(Sysuser sysuser);

    public void updateStatus(@Param("id") Integer id, @Param("isValid") Integer isValid);

    public Sysuser selectByName(String loginName);

    public List<Sysuser> selectByParams(SysuserParam sysuserParam);

    public Sysuser selectByLoginNameAndPass(@Param("loginName") String loginName, @Param("password") String password, @Param("isValid") int sysuserValid);
}
