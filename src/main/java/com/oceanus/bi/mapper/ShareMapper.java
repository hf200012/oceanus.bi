package com.oceanus.bi.mapper;

import com.oceanus.bi.domain.Share;
import com.oceanus.system.system.domain.SysUser;

import java.util.List;

public interface ShareMapper {

    public int insertShare(List<Share> shares);

    public List<SysUser> selectShareUserList();
}
