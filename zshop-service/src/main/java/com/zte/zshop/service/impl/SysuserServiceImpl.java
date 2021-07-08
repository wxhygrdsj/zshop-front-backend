package com.zte.zshop.service.impl;

import com.zte.zshop.constants.Constant;
import com.zte.zshop.dao.SysuserDao;
import com.zte.zshop.entity.Role;
import com.zte.zshop.entity.Sysuser;
import com.zte.zshop.exception.SysuserNotExistsException;
import com.zte.zshop.params.SysuserParam;
import com.zte.zshop.service.SysuserService;
import com.zte.zshop.vo.SysuserVO;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Author:helloboy
 * Date:2021-06-11 9:13
 * Description:<描述>
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class SysuserServiceImpl implements SysuserService {

    @Autowired
    private SysuserDao sysuserDao;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public List<Sysuser> findAll() {
        return sysuserDao.selectAll();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public Sysuser findById(Integer id) {
        return sysuserDao.selectById(id);
    }

    @Override
    public void modify(SysuserVO sysuserVO) {
        Sysuser sysuser=new Sysuser();
        try {
            PropertyUtils.copyProperties(sysuser,sysuserVO);
            Role role =new Role();
            role.setId(sysuserVO.getRoleId());
            sysuser.setRole(role);
            sysuserDao.update(sysuser);
        }catch (Exception e){
            throw new RuntimeException("aaa"+e.getMessage());
        }

    }

    @Override
    public void add(SysuserVO sysuserVO) {

        Sysuser sysuser = new Sysuser();
        try {
            PropertyUtils.copyProperties(sysuser,sysuserVO);
            //默认为有效状态
            sysuser.setIsValid(Constant.SYSUSER_VALID);
            //默认响应时间就是当前时间
            sysuser.setCreateDate(new Date());
            //设置角色
            Role role = new Role();
            role.setId(sysuserVO.getRoleId());
            sysuser.setRole(role);
            sysuserDao.insert(sysuser);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());

        }


    }

    @Override
    public void modifyStatus(Integer id) {
        Sysuser sysuser=sysuserDao.selectById(id);
        Integer isValid = sysuser.getIsValid();
        if(isValid==Constant.SYSUSER_VALID){
            isValid=Constant.SYSUSER_INVALID;
        }
        else{
            isValid=Constant.SYSUSER_VALID;
        }
        sysuserDao.updateStatus(id,isValid);

    }

    @Override
    public boolean checkName(String loginName) {
        Sysuser sysuser=sysuserDao.selectByName(loginName);
        if(sysuser!=null){

            return false;
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public List<Sysuser> findByParams(SysuserParam sysuserParam) {
        return sysuserDao.selectByParams(sysuserParam);
    }

    @Override
    public Sysuser login(String loginName, String password) throws SysuserNotExistsException {
        Sysuser sysuser=sysuserDao.selectByLoginNameAndPass(loginName,password,Constant.SYSUSER_VALID);
        if(sysuser!=null){
            return sysuser;
        }
        throw new SysuserNotExistsException("用户名或密码不正确") ;
    }
}
