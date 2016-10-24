package com.tys.ms.controller;

import com.geetest.sdk.java.GeetestLib;
import com.geetest.sdk.java.web.demo.GeetestConfig;
import com.tys.ms.converter.ProductXlsView;
import com.tys.ms.model.ProductIns;
import com.tys.ms.model.User;
import com.tys.ms.model.UserProfile;
import com.tys.ms.service.ProductInsService;
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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
@SessionAttributes("roles")
public class AppController {
    @Autowired
    UserService userService;

    @Autowired
    UserProfileService userProfileService;

    @Autowired
    ProductInsService productInsService;

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
    public String loginPage() {
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
        String geetUserId = "tys-user"; //自定义geetUserId
        //进行验证预处理
        int gtServerStatus = gtSdk.preProcess(geetUserId);
        //将服务器状态设置到session中
        request.getSession().setAttribute(gtSdk.gtServerStatusSessionKey, gtServerStatus);
        //将geetUserId设置到session中
        request.getSession().setAttribute("geetUserId", geetUserId);

        resStr = gtSdk.getResponseStr();
        model.addAttribute("resStr", resStr);

        return resStr;
    }

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
//        List<User> users;
//        List<User> allUsers = userService.findAllUsers();
//        String loginUserJobId = getPrincipal();
//        User loginUser = userService.findByJobId(loginUserJobId);
//        if (loginUser.getUserProfile().getType().equals("ADMIN")) {
//            users = allUsers.stream()
//                    .filter(user -> !user.getLeaderId().equals("NONE"))
//                    .collect(Collectors.toList());
//        } else {
//            List<User> upOneLevelUser = allUsers.stream()
//                    .filter(user -> user.getLeaderId().equals(loginUserJobId))
//                    .collect(Collectors.toList());
//            List<User> upTwoLevelUser = upOneLevelUser.stream()
//                    .filter(user -> user.getLeaderId().equals("area22"))
//                    .collect(Collectors.toList());
//            users  = new ArrayList<>();
//            users.addAll(upOneLevelUser);
//            users.addAll(upTwoLevelUser);
//        }
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

        } else {
            users = userService.findAllDownUsers(loginUser.getJobId());
        }

        model.addAttribute("users", users);
        model.addAttribute("loginUser", getPrincipal());
        return "userList";
    }

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

        model.addAttribute("success", "会员 " + user.getName() + " " + " 添加成功");
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

    @RequestMapping(value = "/list-product-car", method = RequestMethod.GET)
    public String listProductCar(ModelMap model) {
        List<ProductIns> productInsList = productInsService.findByType("car");
        model.addAttribute("productInsList", productInsList);
        model.addAttribute("loginUser", getPrincipal());
        return "listProductCar";
    }

    @RequestMapping(value = "/export-product-car", method = RequestMethod.GET)
    public ModelAndView getExcel(ModelMap model) {
        List<ProductIns> productInsList = productInsService.findByType("car");
        model.addAttribute("productInsList", productInsList);
        return new ModelAndView(new ProductXlsView(), model);
    }

    @RequestMapping(value = "/list-product-person", method = RequestMethod.GET)
    public String listProductPerson(ModelMap model) {
        List<ProductIns> productInsList = productInsService.findByType("person");
        model.addAttribute("productInsList", productInsList);
        model.addAttribute("loginUser", getPrincipal());
        return "listProductPerson";
    }

    @RequestMapping(value = "/list-product-team", method = RequestMethod.GET)
    public String listProductTeam(ModelMap model) {
        List<ProductIns> productInsList = productInsService.findByType("team");
        model.addAttribute("productInsList", productInsList);
        model.addAttribute("loginUser", getPrincipal());
        return "listProductTeam";
    }

    @RequestMapping(value = "/list-product-card", method = RequestMethod.GET)
    public String listProductCard(ModelMap model) {
        List<ProductIns> productInsList = productInsService.findByType("card");
        model.addAttribute("productInsList", productInsList);
        model.addAttribute("loginUser", getPrincipal());
        return "listProductCard";
    }

    @RequestMapping(value = "/add-product-car", method = RequestMethod.GET)
    public String addProductCar(ModelMap model) {
        ProductIns productIns = new ProductIns();
        model.addAttribute("productIns", productIns);
        model.addAttribute("car", true);
        model.addAttribute("loginUser", getPrincipal());
        return "addProductIns";
    }

    @RequestMapping(value = "/add-product-car", method = RequestMethod.POST)
    public String saveProductCar(@Valid ProductIns productIns, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("car", true);
            model.addAttribute("loginUser", getPrincipal());
            return "addProductIns";
        } else if (userService.findByJobId(productIns.getEmployeeId()) == null) {
            FieldError employeeIdError =new FieldError("productIns","employeeId",messageSource.getMessage("non.exist.employeeId", new String[]{productIns.getEmployeeId()}, Locale.getDefault()));
            result.addError(employeeIdError);
            model.addAttribute("car", true);
            model.addAttribute("loginUser", getPrincipal());
            return "addProductIns";
        } else if (!userService.findByJobId(productIns.getEmployeeId()).getName().equals(productIns.getEmployee())) {
            FieldError employeeError =new FieldError("productIns","employee",messageSource.getMessage("non.corresponding.employee", new String[]{productIns.getEmployee(), productIns.getEmployeeId()}, Locale.getDefault()));
            result.addError(employeeError);
            model.addAttribute("car", true);
            model.addAttribute("loginUser", getPrincipal());
            return "addProductIns";
        } else if (add(productIns.getCarBusinessMoney(), productIns.getCarMandatoryMoney(), productIns.getCarTaxMoney()).compareTo(new BigDecimal(productIns.getInsMoney())) != 0) {
            // 0 相等，1 不相等
            FieldError insMoneyError =new FieldError("productIns","insMoney",messageSource.getMessage("valid.calculate.productIns.insMoney", new String[]{productIns.getInsMoney()}, Locale.getDefault()));
            result.addError(insMoneyError);
            model.addAttribute("car", true);
            model.addAttribute("loginUser", getPrincipal());
            return "addProductIns";
        } else {
            productInsService.save(productIns);
            model.addAttribute("car", true);
            model.addAttribute("loginUser", getPrincipal());
            return "addProductInsDone";
        }
    }

    public static BigDecimal add(String num1, String num2, String num3) {
        BigDecimal bd1 = new BigDecimal(num1);
        BigDecimal bd2 = new BigDecimal(num2);
        BigDecimal bd3 = new BigDecimal(num3);
        return bd1.add(bd2).add(bd3);
    }

    @RequestMapping(value = "/add-product-person", method = RequestMethod.GET)
    public String addProductPerson(ModelMap model) {
        ProductIns productIns = new ProductIns();
        model.addAttribute("productIns", productIns);
        model.addAttribute("person", true);
        model.addAttribute("loginUser", getPrincipal());
        return "addProductIns";
    }

    @RequestMapping(value = "/add-product-person", method = RequestMethod.POST)
    public String saveProductPerson(@Valid ProductIns productIns, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("person", true);
            model.addAttribute("loginUser", getPrincipal());
            return "addProductIns";
        } else if (userService.findByJobId(productIns.getEmployeeId()) == null) {
            FieldError employeeIdError =new FieldError("productIns","employeeId",messageSource.getMessage("non.exist.employeeId", new String[]{productIns.getEmployeeId()}, Locale.getDefault()));
            result.addError(employeeIdError);
            model.addAttribute("person", true);
            model.addAttribute("loginUser", getPrincipal());
            return "addProductIns";
        } else if (!userService.findByJobId(productIns.getEmployeeId()).getName().equals(productIns.getEmployee())) {
            FieldError employeeError =new FieldError("productIns","employee",messageSource.getMessage("non.corresponding.employee", new String[]{productIns.getEmployee(), productIns.getEmployeeId()}, Locale.getDefault()));
            result.addError(employeeError);
            model.addAttribute("person", true);
            model.addAttribute("loginUser", getPrincipal());
            return "addProductIns";
        } else {
            productInsService.save(productIns);
            model.addAttribute("person", true);
            model.addAttribute("loginUser", getPrincipal());
            return "addProductInsDone";
        }
    }

    @RequestMapping(value = "/add-product-team", method = RequestMethod.GET)
    public String addProductTeam(ModelMap model) {
        ProductIns productIns = new ProductIns();
        model.addAttribute("productIns", productIns);
        model.addAttribute("team", true);
        model.addAttribute("loginUser", getPrincipal());
        return "addProductIns";
    }

    @RequestMapping(value = "/add-product-team", method = RequestMethod.POST)
    public String saveProductTeam(@Valid ProductIns productIns, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("team", true);
            model.addAttribute("loginUser", getPrincipal());
            return "addProductIns";
        } else if (userService.findByJobId(productIns.getEmployeeId()) == null) {
            FieldError employeeIdError =new FieldError("productIns","employeeId",messageSource.getMessage("non.exist.employeeId", new String[]{productIns.getEmployeeId()}, Locale.getDefault()));
            result.addError(employeeIdError);
            model.addAttribute("team", true);
            model.addAttribute("loginUser", getPrincipal());
            return "addProductIns";
        } else if (!userService.findByJobId(productIns.getEmployeeId()).getName().equals(productIns.getEmployee())) {
            FieldError employeeError =new FieldError("productIns","employee",messageSource.getMessage("non.corresponding.employee", new String[]{productIns.getEmployee(), productIns.getEmployeeId()}, Locale.getDefault()));
            result.addError(employeeError);
            model.addAttribute("team", true);
            model.addAttribute("loginUser", getPrincipal());
            return "addProductIns";
        } else {
            productInsService.save(productIns);
            model.addAttribute("team", true);
            model.addAttribute("loginUser", getPrincipal());
            return "addProductInsDone";
        }
    }

    @RequestMapping(value = "/add-product-card", method = RequestMethod.GET)
    public String addProductCard(ModelMap model) {
        ProductIns productIns = new ProductIns();
        model.addAttribute("productIns", productIns);
        model.addAttribute("card", true);
        model.addAttribute("loginUser", getPrincipal());
        return "addProductIns";
    }

    @RequestMapping(value = "/add-product-card", method = RequestMethod.POST)
    public String saveProductCard(@Valid ProductIns productIns, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("card", true);
            model.addAttribute("loginUser", getPrincipal());
            return "addProductIns";
        } else if (userService.findByJobId(productIns.getEmployeeId()) == null) {
            FieldError employeeIdError =new FieldError("productIns","employeeId",messageSource.getMessage("non.exist.employeeId", new String[]{productIns.getEmployeeId()}, Locale.getDefault()));
            result.addError(employeeIdError);
            model.addAttribute("card", true);
            model.addAttribute("loginUser", getPrincipal());
            return "addProductIns";
        } else if (!userService.findByJobId(productIns.getEmployeeId()).getName().equals(productIns.getEmployee())) {
            FieldError employeeError =new FieldError("productIns","employee",messageSource.getMessage("non.corresponding.employee", new String[]{productIns.getEmployee(), productIns.getEmployeeId()}, Locale.getDefault()));
            result.addError(employeeError);
            model.addAttribute("card", true);
            model.addAttribute("loginUser", getPrincipal());
            return "addProductIns";
        } else {
            productInsService.save(productIns);
            model.addAttribute("card", true);
            model.addAttribute("loginUser", getPrincipal());
            return "addProductInsDone";
        }
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
