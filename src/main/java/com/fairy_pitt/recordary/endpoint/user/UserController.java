package com.fairy_pitt.recordary.endpoint.user;

import com.fairy_pitt.recordary.common.entity.UserEntity;
import com.fairy_pitt.recordary.common.repository.UserRepository;
import com.fairy_pitt.recordary.endpoint.user.service.JoinService;
import com.fairy_pitt.recordary.endpoint.user.service.LoginService;
import com.fairy_pitt.recordary.endpoint.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private JoinService joinService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private HttpSession session;

//    @CrossOrigin
    @PostMapping(value = "/joinRequest")
    public Map<String, Boolean> joinRequest(@RequestParam Map<String, String> paramMap){
        String userId = paramMap.get("user_id");
        String userPw = paramMap.get("user_pw");
        String userNm = paramMap.get("user_nm");

        Boolean possibleIdState = joinService.possibleId(userId);
        Boolean joinState = false;

        if (possibleIdState) joinState = joinService.joinUser(userId, userPw, userNm);

        Map<String, Boolean> map = new HashMap<>();
        map.put("isPossibleId", possibleIdState);
        map.put("isJoin", joinState);

        return map;
    }

//    @CrossOrigin
    @PostMapping(value = "/loginRequest")
    public Map<String, Boolean> loginRequest(@RequestParam Map<String, String> paramMap){
        String userId = paramMap.get("user_id");
        String userPw = paramMap.get("user_pw");

        Boolean loginState = loginService.login(userId, userPw);

        Map<String, Boolean> map = new HashMap<>();
        map.put("isLogin", loginState);

        return map;
    }

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/userUpdate")
    public Map<String, Boolean> userUpdate(@RequestParam Map<String, String> paramMap){
        String checkUserPw = paramMap.get("check_user_pw");
        Boolean isChangeUserNm = Boolean.parseBoolean(paramMap.get("is_change_user_nm"));
        String changeUserNm = paramMap.get("change_user_nm");
        Boolean isChangeUserPw = Boolean.parseBoolean(paramMap.get("is_change_user_pw"));
        String changeUserPw = paramMap.get("change_user_pw");

        UserEntity currentUser = (UserEntity)session.getAttribute("loginUser");

        Map<String, Boolean> map = new HashMap<>();
        Boolean checkPwState = false;
        Boolean updateState = false;
        if(changeUserNm.equals("") || changeUserPw.equals("")) updateState =  false;
        else if (userInfoService.checkPw(currentUser, checkUserPw)) {
            checkPwState = true;
            if (isChangeUserNm) userInfoService.updateNm(currentUser, changeUserNm);
            else if (isChangeUserPw) userInfoService.updatePw(currentUser, changeUserPw);
            updateState = true;
        }
        map.put("is_correct_user_pw", checkPwState);
        map.put("is_update", updateState);
        return map;
    }

    @GetMapping(value = "/userSearch")
    public Map<String, Object> userSearch(@RequestParam(value = "userSearch")String userSearch){
        List<UserEntity> searchedUser = userInfoService.search(userSearch);
        Map<String, Object> map = new HashMap<>();
        map.put("searchedUserCount", searchedUser.size());

        List UserMapList = new ArrayList();
        for (int i = 0; i < searchedUser.size(); i++){
            Map<String, Object> userDetailMap = new HashMap<>();
            userDetailMap.put("groupCd", searchedUser.get(i).getUserCd());
            userDetailMap.put("groupPic", "none");
            userDetailMap.put("groupNm", searchedUser.get(i).getUserNm());
            UserMapList.add(userDetailMap);
        }
        map.put("searedUser", UserMapList);

        return map;
    }

}
