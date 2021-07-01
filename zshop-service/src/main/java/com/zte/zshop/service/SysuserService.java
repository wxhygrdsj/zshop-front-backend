package com.zte.zshop.service;

import com.zte.zshop.entity.Sysuser;
import com.zte.zshop.exception.SysuserNotExistsException;
import com.zte.zshop.params.SysuserParam;
import com.zte.zshop.vo.SysuserVO;

import java.util.List;

/**
 * Author:helloboy
 * Date:2021-06-11 9:11
 * Description:<描述>
 */
public interface SysuserService{

        public List<Sysuser> findAll();

        public Sysuser findById(Integer id);

        public void modify(SysuserVO sysuserVO);

        public void add(SysuserVO sysuserVO);

        public void modifyStatus(Integer id);

        public boolean checkName(String loginName);

        public List<Sysuser> findByParams(SysuserParam sysuserParam);

        public Sysuser login(String loginName, String password)throws SysuserNotExistsException;
}
