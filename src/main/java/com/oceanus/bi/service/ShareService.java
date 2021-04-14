package com.oceanus.bi.service;

import com.oceanus.bi.domain.Share;
import com.oceanus.bi.mapper.ShareMapper;
import com.oceanus.common.util.DateUtil;
import com.oceanus.common.utils.SecurityUtils;
import com.oceanus.common.utils.StringUtils;
import com.oceanus.system.system.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShareService {

    @Autowired
    private ShareMapper shareMapper;

    public int saveShare(String dashboardId,String shareUserIds){
        if(StringUtils.isNotEmpty(shareUserIds)){
            List<Share> shares = new ArrayList<>();
            String[] ids = shareUserIds.split(",");
            long currentUserId = SecurityUtils.getLoginUser().getUser().getUserId();
            for(String id : ids){
                Share share = new Share();
                share.setDashboard_id(dashboardId);
                share.setTo_user_id(Long.parseLong(id));
                share.setShare_user_id(currentUserId);
                share.setShare_date(DateUtil.getCurrentDateFormat());
                shares.add(share);
            }
            return this.shareMapper.insertShare(shares);
        }
        return 0;
    }

    public List<SysUser> getShareUserList(){
        return this.shareMapper.selectShareUserList();
    }

}
