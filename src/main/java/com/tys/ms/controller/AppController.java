package com.tys.ms.controller;

import com.geetest.sdk.java.GeetestLib;
import com.geetest.sdk.java.web.demo.GeetestConfig;
import com.tys.ms.model.User;
import com.tys.ms.model.UserProfile;
import com.tys.ms.service.UserProfileService;
import com.tys.ms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/")
@SessionAttributes("roles")
public class AppController {
    @Autowired
    UserService userService;

    @Autowired
    UserProfileService userProfileService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;

    @Autowired
    AuthenticationTrustResolver authenticationTrustResolver;

    private String getPrincipal(){
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

    private boolean isCurrentAuthenticationAnonymous() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authenticationTrustResolver.isAnonymous(authentication);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)

    public String loginPage(ModelMap model) {

        GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key());

        String resStr = "{}";

        //自定义userid
        String userid = "test";

        //进行验证预处理
        int gtServerStatus = gtSdk.preProcess(userid);

//        //将服务器状态设置到session中
//        request.getSession().setAttribute(gtSdk.gtServerStatusSessionKey, gtServerStatus);
//        //将userid设置到session中
//        request.getSession().setAttribute("userid", userid);

        resStr = gtSdk.getResponseStr();
        model.addAttribute("resStr", resStr);

        if (isCurrentAuthenticationAnonymous()) {
            return "login";
        } else {
            return "redirect:/info";
        }
    }

    @RequestMapping(value = "/geetValidate", method = RequestMethod.GET)
    @ResponseBody
    public String geetValidate(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key());

        String resStr = "{}";

        //自定义userid
        String userid = "test";

        //进行验证预处理
        int gtServerStatus = gtSdk.preProcess(userid);

        //将服务器状态设置到session中
        request.getSession().setAttribute(gtSdk.gtServerStatusSessionKey, gtServerStatus);
        //将userid设置到session中
        request.getSession().setAttribute("userid", userid);

        resStr = gtSdk.getResponseStr();
        model.addAttribute("resStr", resStr);
        return resStr;
    }

//    @RequestMapping(value = "/geetValidate", method = RequestMethod.POST)
//    @ResponseBody
//    public String geetestValidate(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
//        GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key());
//
//        String challenge = request.getParameter(GeetestLib.fn_geetest_challenge);
//        String validate = request.getParameter(GeetestLib.fn_geetest_validate);
//        String seccode = request.getParameter(GeetestLib.fn_geetest_seccode);
//
//        //从session中获取gt-server状态
//        int gt_server_status_code = (Integer) request.getSession().getAttribute(gtSdk.gtServerStatusSessionKey);
//
//        //从session中获取userid
//        String userid = (String)request.getSession().getAttribute("userid");
//
//        int gtResult = 0;
//
//        if (gt_server_status_code == 1) {
//            //gt-server正常，向gt-server进行二次验证
//
//            gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, userid);
//            System.out.println(gtResult);
//        } else {
//            // gt-server非正常情况下，进行failback模式验证
//
//            System.out.println("failback:use your own server captcha validate");
//            gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
//            System.out.println(gtResult);
//        }
//
////        JSONObject data = new JSONObject();
//        Map<String, String> data = new HashMap<String, String>();
//
//
//        if (gtResult == 1) {
//            // 验证成功
////			PrintWriter out = response.getWriter();
//
//            data.put("status", "success");
//            data.put("version", gtSdk.getVersionInfo());
////			out.println(data.toString());
//        }
//        else {
//            // 验证失败
//
//            data.put("status", "fail");
//            data.put("version", gtSdk.getVersionInfo());
////			PrintWriter out = response.getWriter();
////			out.println(data.toString());
//        }
//        return data.toString();
//    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            //new SecurityContextLogoutHandler().logout(request, response, auth);
            persistentTokenBasedRememberMeServices.logout(request, response, auth);
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return "redirect:/login?logout";
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String userInfo(ModelMap model) {
        User users = userService.findByJobId(getPrincipal());
        model.addAttribute("users", users);
        model.addAttribute("loginUser", getPrincipal());
        model.addAttribute("loginUserType", users.getUserProfile());
        return "userInfo";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listUsers(ModelMap model) {
        List<User> users = null;
        User loginUser = userService.findByJobId(getPrincipal());
        if (loginUser.getUserProfile().getType().equals("ADMIN")) {
            users = userService.findAllUsers();

            Iterator<User> it = users.iterator();
            while(it.hasNext()) {
                if(it.next().getLeaderId().equals("NONE")) {
                    it.remove();
                }
            }
//            while (users.iterator().hasNext()) {
//                if (users.iterator().next().getLeaderId().equals("NONE")) {
//                    users.iterator().remove();
//                }
//            }
        } else {
            users = userService.findAllDownUsers(getPrincipal());
        }
//        List<User> users = userService.findAllDownUsers(getPrincipal());
        model.addAttribute("users", users);
        model.addAttribute("loginUser", getPrincipal());
        return "userList";
    }

//    @RequestMapping(value = { "/", "/all-list" }, method = RequestMethod.GET)
//    public String listAllUsers(ModelMap model) {
//        List<User> users = userService.findAllUsers();
//        model.addAttribute("users", users);
//        model.addAttribute("loginUser", getPrincipal());
//        return "userList";
//    }

    @RequestMapping(value = "/addUser", method = RequestMethod.GET)
    public String newUser(ModelMap model) {
        User user = new User();
        int upId = userService.findByJobId(getPrincipal()).getUserProfile().getId();
        List<UserProfile> userProfileList = userProfileService.findDownAll(upId);
        model.addAttribute("user", user);
        model.addAttribute("edit", false);
        model.addAttribute("profile", userProfileList);
        model.addAttribute("loginUser", getPrincipal());
        return "registration";
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public String saveUser(@Valid User user, BindingResult result, ModelMap model) {
        int upId = userService.findByJobId(getPrincipal()).getUserProfile().getId();
        if (result.hasErrors()) {
            List<UserProfile> userProfileList = userProfileService.findDownAll(upId);
            model.addAttribute("profile", userProfileList);
            return "registration";
        }

        if(!userService.isUserJobIdUnique(user.getId(), user.getJobId())){
            List<UserProfile> userProfileList = userProfileService.findDownAll(upId);
            model.addAttribute("profile", userProfileList);
            FieldError jobIdError =new FieldError("user","jobId",messageSource.getMessage("non.unique.jobId", new String[]{user.getJobId()}, Locale.getDefault()));
            result.addError(jobIdError);
            return "registration";
        }

        if(!user.getPassword().equals(user.getRetypePassword() ) ) {
            List<UserProfile> userProfileList = userProfileService.findDownAll(upId);
            model.addAttribute("profile", userProfileList);
            FieldError pwdError =new FieldError("user","jobId",messageSource.getMessage("valid.passwordConfDiff", new String[]{user.getJobId()}, Locale.getDefault()));
            result.addError(pwdError);
            return "registration";
        }

        if (upId == 1) {
            user.setHasPassed(true);
        }
        userService.saveUser(user);

        model.addAttribute("success", "User " + user.getName() + " " + " registered successfully");
        model.addAttribute("loginUser", getPrincipal());
        return "registrationDone";
    }

    @RequestMapping(value = "/edit-user-{jobId}", method = RequestMethod.GET)
    public String editUser(@PathVariable String jobId, ModelMap model) {
        User user = userService.findByJobId(jobId);
        model.addAttribute("user", user);
        model.addAttribute("edit", true);
        int upId = userService.findByJobId(jobId).getUserProfile().getId();
        List<UserProfile> userProfileList = userProfileService.findDownAll(upId);
        model.addAttribute("profile", userProfileList);
        model.addAttribute("loginUser", getPrincipal());
        return "registration";
    }

    @RequestMapping(value = { "/edit-user-{jobId}" }, method = RequestMethod.POST)
    public String updateUser(@Valid User user, BindingResult result, ModelMap model, @PathVariable String jobId) {
        if (result.hasErrors()) {
            model.addAttribute("edit", true);
            int upId = userService.findByJobId(jobId).getUserProfile().getId();
            List<UserProfile> userProfileList = userProfileService.findDownAll(upId);
            model.addAttribute("profile", userProfileList);
            return "registration";
        }

        userService.updateUser(user);

        model.addAttribute("success", "User " + user.getName()  + " updated successfully");
        model.addAttribute("loginUser", getPrincipal());
        return "registrationDone";
    }

    @RequestMapping(value = "/change-pwd-{jobId}", method = RequestMethod.GET)
    public String changePwd(ModelMap model, @PathVariable String jobId) {
        User user = userService.findByJobId(jobId);
        model.addAttribute("user", user);
        return "cp";
    }

    @RequestMapping(value = "/change-pwd-{jobId}", method = RequestMethod.POST)
    public String changePwd(User user, BindingResult result, ModelMap model, @PathVariable String jobId) {
        User user2 = userService.findByJobId(jobId);
        if (!BCrypt.checkpw(user.getOldPassword(), user2.getPassword())) {
            FieldError passwordError =new FieldError("user","password",messageSource.getMessage("valid.oldPassword", new String[]{user.getPassword()}, Locale.getDefault()));
            result.addError(passwordError);
            return "cp";
        } else if(!user.getPassword().equals(user.getRetypePassword())) {
            System.out.println(user.getPassword());
            System.out.println(user.getRetypePassword());
            FieldError passwordError =new FieldError("user","password",messageSource.getMessage("valid.passwordConfDiff", new String[]{user.getPassword()}, Locale.getDefault()));
            result.addError(passwordError);
            return "cp";
        } else {
            user2.setPassword(String.valueOf(user.getPassword()));
            userService.updateUser(user2);
            model.addAttribute("success", "User " + user.getName()  + " updated successfully");
            model.addAttribute("loginUser", getPrincipal());
            return "cpDone";
        }
    }

    @RequestMapping(value = { "/delete-user-{jobId}" }, method = RequestMethod.GET)
    public String deleteUser(@PathVariable String jobId) {
        userService.deleteUserByJobId(jobId);
        return "redirect:/list";
    }

    @ModelAttribute("roles")
    public List<UserProfile> initializeProfiles() {
        return userProfileService.findAll();
    }

    @RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
    public String accessDeniedPage(ModelMap model) {
        model.addAttribute("loginUser", getPrincipal());
        return "accessDenied";
    }
}
