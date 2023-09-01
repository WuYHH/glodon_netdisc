package com.glodon.glodon_netdisc.service.impl;

import com.glodon.glodon_netdisc.entity.model.UserInfoProps;
import com.glodon.glodon_netdisc.entity.model.UserLoginProps;
import com.glodon.glodon_netdisc.entity.vo.ActionVo;
import com.glodon.glodon_netdisc.entity.vo.PermissionVo;
import com.glodon.glodon_netdisc.entity.vo.RoleVo;
import com.glodon.glodon_netdisc.entity.vo.UserVo;
import com.glodon.glodon_netdisc.exception.CustomExceptions;
import com.glodon.glodon_netdisc.mapper.UserMapper;
import com.glodon.glodon_netdisc.pojo.User;
import com.glodon.glodon_netdisc.service.CaptchaService;
import com.glodon.glodon_netdisc.service.UserService;
import com.glodon.glodon_netdisc.util.CommunityUtil;
import com.glodon.glodon_netdisc.util.Constants;
import com.glodon.glodon_netdisc.util.JwtHelper;
import com.glodon.glodon_netdisc.util.UUIDUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    public CaptchaService captchaService;

    @Autowired
    private UUIDUtil uuidUtil;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 注册用户
     *
     * @param account  账户
     * @param name     昵称
     * @param password 密码
     */
    @Override
    public void registry(String account, String name, String password) {
        //账户名需要满足邮箱格式，正则表达式。
        String accountPattern ="[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
        //用户名需要满足4-16位，可以为中文、数字、字母或下划线。
        String namePattern ="^[\\u4e00-\\u9fa5_a-zA-Z0-9]{4,16}$";
        //密码满足6-16位，含有数字、字母、特殊字符三种以上。
        String passwordPattern="^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[~!@#$%^&*()_+`\\-=\\[\\]\\\\\\:\";'<>?,./])[A-Za-z0-9~!@#$%^&*()_+`\\-=\\[\\]\\\\\\:\";'<>?,./]{6,16}$";
        Pattern accountMatcher = Pattern.compile(accountPattern);
        Pattern nameMatcher = Pattern.compile(namePattern);
        Pattern passwordMatcher = Pattern.compile(passwordPattern);
        boolean AccountResult = accountMatcher.matcher(account).matches();
        boolean nameResult = nameMatcher.matcher(name).matches();
        boolean passwordResult = passwordMatcher.matcher(password).matches();
        if(AccountResult && nameResult && passwordResult){
            User loginHistory = userMapper.login(account);
            if(loginHistory!=null){
                throw new CustomExceptions(401,"该用户用户已注册");
            }
            //用户密码md5加密
            String md5pwd = CommunityUtil.md5(password);
            userMapper.registry(account, name, md5pwd);

        }else{
            if(!AccountResult){
                throw new CustomExceptions(401,"邮箱格式不正确");
            }else if (!nameResult){
                throw new CustomExceptions(401,"用户名格式不正确");
            }else {
                throw new CustomExceptions(401,"密码格式不正确");
            }


        }


    }


    @Override
    public UserVo login(UserLoginProps userLoginProps) {
        String account = userLoginProps.getAccount();
        String password = userLoginProps.getPassword();
        //1.校验验证码
        captchaService.versifyCaptcha(userLoginProps);
        //2.校验账户、密码格式，减少数据库查询。
        String accountPattern ="[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
        String passwordPattern="^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[~!@#$%^&*()_+`\\-=\\[\\]\\\\\\:\";'<>?,./])[A-Za-z0-9~!@#$%^&*()_+`\\-=\\[\\]\\\\\\:\";'<>?,./]{8,16}$";
        Pattern accountMatcher = Pattern.compile(accountPattern);
        Pattern passwordMatcher = Pattern.compile(passwordPattern);
        boolean AccountResult = accountMatcher.matcher(account).matches();
        boolean passwordResult = passwordMatcher.matcher(password).matches();
        if(AccountResult && passwordResult){
            //3.登录用户名
            User user = userMapper.login(account);
            if(user!=null && user.getPassword().equals(CommunityUtil.md5(password))){
                return getUserInfo(user.getId());
            }
            throw new CustomExceptions(401,"用户不存在或密码错误");
        }else{
            if(!AccountResult){
                throw new CustomExceptions(401,"邮箱格式不正确");
            }else{
                throw new CustomExceptions(401,"密码格式不合法！");
            }

        }


    }

    /**
     * 更改密码
     *
     * @param account  账户
     * @param password 密码
     */
    @Override
    public void modify(String account, String oldPassword,String newPassword){
        String passwordPattern="^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[~!@#$%^&*()_+`\\-=\\[\\]\\\\\\:\";'<>?,./])[A-Za-z0-9~!@#$%^&*()_+`\\-=\\[\\]\\\\\\:\";'<>?,./]{8,16}$";
        Pattern passwordMatcher = Pattern.compile(passwordPattern);
        boolean passwordResult = passwordMatcher.matcher(newPassword).matches();
        if(passwordResult){
            User user = userMapper.login(account);
            if(user==null){
                throw new CustomExceptions(401,"账户名不存在");
            }
            else if(!user.getPassword().equals(CommunityUtil.md5(oldPassword))){
                throw new CustomExceptions(401,"用户无权限");
            }else{
                if(user.getPassword().equals(CommunityUtil.md5(newPassword))){
                    throw new CustomExceptions(401,"密码未修改");
                }else{
                    userMapper.modify(account,CommunityUtil.md5(newPassword));
                }
            }


        }else{
            throw new CustomExceptions(401,"密码格式有误");
        }
    }

    /**
     * 获取用户列表
     *
     * @param type 用户类型
     * @return
     */
    @Override
    public List<UserInfoProps> getUserList(Integer type) {
        if(type!=null && type != 0 && type != 1){
            throw new CustomExceptions(400,"请求参数错误");
        }
        List<User> userLists = userMapper.getUserList(type);
       List<UserInfoProps> userInfoPropsList = new ArrayList<>();
       for(int i=0;i<userLists.size();i++){
           UserInfoProps userInfoProps = new UserInfoProps();
           BeanUtils.copyProperties(userLists.get(i),userInfoProps);
           userInfoPropsList.add(userInfoProps);
       }
        return userInfoPropsList;
    }

    /**
     * 获取指定用户类型的用户数量
     * @param type
     * @return
     */
    @Override
    public int getUserCount(Integer type) {
        int userCount = userMapper.getUserCount(type);
        return userCount;
    }

    /**
     * 根据用户id，更新用户状态。
     * @param id 用户id
     * @param status 用户状态
     * @return
     */
    @Override
    public int updateStatus(Integer id, Integer status) {
        User user = userMapper.getUserById(id);
        if(user==null || (status!=0 && status!=1)){
            throw new CustomExceptions(400,"请求参数错误");
        }
        userMapper.updateStatus(id,status);
        return 1;
    }

    private List<ActionVo> actionVoList = new ArrayList<>();
    private String actions = "[{\"action\":\"add\",\"defaultCheck\":false,\"describe\":\"新增\"},{\"action\":\"query\",\"defaultCheck\":false,\"describe\":\"查询\"},{\"action\":\"get\",\"defaultCheck\":false,\"describe\":\"详情\"},{\"action\":\"update\",\"defaultCheck\":false,\"describe\":\"修改\"},{\"action\":\"delete\",\"defaultCheck\":false,\"describe\":\"删除\"}]";

    @Override
    public UserVo getUserInfo(Integer id) {
        User user = userMapper.getUserById(id);
        getDefaultData();
        PermissionVo permissionVo1 = new PermissionVo();
        PermissionVo permissionVo2 = new PermissionVo();
        PermissionVo permissionVo3 = new PermissionVo();
        if (user.getType() == 1) {
            // 管理员
            PermissionVo permissionVo4 = new PermissionVo();
            permissionVo1.setPermissionId("adminDashboard");
            permissionVo1.setPermissionName("仪表盘");
            permissionVo1.setRoleId("admin");
            permissionVo1.setActions(this.actions);
            permissionVo1.setActionEntitySet(this.actionVoList);

            permissionVo2.setPermissionId("file");
            permissionVo2.setPermissionName("文件管理");
            permissionVo2.setRoleId("admin");
            permissionVo2.setActions(this.actions);
            permissionVo2.setActionEntitySet(this.actionVoList);

            permissionVo3.setPermissionId("user");
            permissionVo3.setPermissionName("用户管理");
            permissionVo3.setRoleId("admin");
            permissionVo3.setActions(this.actions);
            permissionVo3.setActionEntitySet(this.actionVoList);

            permissionVo4.setPermissionId("self");
            permissionVo4.setPermissionName("个人中心");
            permissionVo4.setRoleId("admin");
            permissionVo4.setActions(this.actions);
            permissionVo4.setActionEntitySet(this.actionVoList);

            List<PermissionVo> permissionList = List.of(permissionVo1, permissionVo2, permissionVo3, permissionVo4);

            RoleVo roleVo = new RoleVo();
            roleVo.setId("admin");
            roleVo.setName("管理员");
            roleVo.setPermissions(permissionList);

            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            userVo.setRoleId("admin");

            String token = JwtHelper.createToken(user.getAccount(),user.getName());
            redisTemplate.opsForValue().set(user.getAccount(), user.getName(), Constants.TOKEN_TIME_OUT, TimeUnit.MINUTES);
            userVo.setToken(token);
            //userVo.setToken("qwjewdhewuheuhfeufherufhe");
            userVo.setRoleVo(roleVo);
            return userVo;
        } else {
            permissionVo1.setPermissionId("userDashboard");
            permissionVo1.setPermissionName("仪表盘");
            permissionVo1.setRoleId("user");
            permissionVo1.setActions(this.actions);
            permissionVo1.setActionEntitySet(this.actionVoList);

            permissionVo2.setPermissionId("file");
            permissionVo2.setPermissionName("文件管理");
            permissionVo2.setRoleId("user");
            permissionVo2.setActions(this.actions);
            permissionVo2.setActionEntitySet(this.actionVoList);

            permissionVo3.setPermissionId("self");
            permissionVo3.setPermissionName("个人中心");
            permissionVo3.setRoleId("admin");
            permissionVo3.setActions(this.actions);
            permissionVo3.setActionEntitySet(this.actionVoList);

            List<PermissionVo> permissionList = List.of(permissionVo1, permissionVo2, permissionVo3);

            RoleVo roleVo = new RoleVo();
            roleVo.setId("user");
            roleVo.setName("普通用户");
            roleVo.setPermissions(permissionList);

            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            userVo.setRoleId("user");

            String token = JwtHelper.createToken(user.getAccount(),user.getName());
            redisTemplate.opsForValue().set(user.getAccount(), user.getName(), Constants.TOKEN_TIME_OUT, TimeUnit.MINUTES);
            userVo.setToken(token);
            //userVo.setToken("edjehfefhfehjfreh");
            userVo.setRoleVo(roleVo);
            return userVo;
        }

    }


    private void getDefaultData() {
        ActionVo actionVo1 = new ActionVo();
        ActionVo actionVo2 = new ActionVo();
        ActionVo actionVo3 = new ActionVo();
        ActionVo actionVo4 = new ActionVo();
        ActionVo actionVo5 = new ActionVo();
        actionVo1.setAction("add");
        actionVo1.setDescribe("添加");
        actionVo1.setDefaultCheck(false);
        actionVo2.setAction("update");
        actionVo2.setDescribe("修改");
        actionVo2.setDefaultCheck(false);
        actionVo3.setAction("get");
        actionVo3.setDescribe("详情");
        actionVo3.setDefaultCheck(false);
        actionVo4.setAction("delete");
        actionVo4.setDescribe("删除");
        actionVo4.setDefaultCheck(false);
        actionVo5.setAction("export");
        actionVo5.setDescribe("导出");
        actionVo5.setDefaultCheck(false);
        List<ActionVo> actionVoList = List.of(actionVo1, actionVo2, actionVo3, actionVo4, actionVo5);
        this.actionVoList = actionVoList;
    }

}
