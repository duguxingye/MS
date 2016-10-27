package com.tys.ms.controller;

import com.geetest.sdk.java.GeetestLib;
import com.geetest.sdk.java.web.demo.GeetestConfig;
import com.tys.ms.dao.FileBucket;
import com.tys.ms.view.ProductXlsxView;
import com.tys.ms.model.ProductIns;
import com.tys.ms.model.User;
import com.tys.ms.model.UserProfile;
import com.tys.ms.service.ProductInsService;
import com.tys.ms.service.UserProfileService;
import com.tys.ms.service.UserService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        List<User> users;
        List<User> allUsers = userService.findAllUsers();
        String loginUserJobId = getPrincipal();
        User loginUser = userService.findByJobId(loginUserJobId);
        if (loginUser.getUserProfile().getType().equals("ADMIN")) {
            users = allUsers.stream()
                    .filter(user -> !user.getLeaderId().equals("NONE"))
                    .collect(Collectors.toList());
        } else {
            users = userService.findAllDownUsers(getPrincipal());
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

        if ("NONE".equals(user.getLeaderId()) ||  userService.findByJobId(user.getLeaderId()) == null) {
            FieldError leaderIdError =new FieldError("user","leaderId",messageSource.getMessage("valid.user.leaderId", new String[]{user.getLeaderId()}, Locale.getDefault()));
            result.addError(leaderIdError);
            List<UserProfile> userProfileList = userProfileService.findDownAll(upId);
            model.addAttribute("profile", userProfileList);
            return "registration";
        }

        if(!userService.isUserJobIdUnique(user.getId(), user.getJobId())){
            FieldError jobIdError =new FieldError("user","jobId",messageSource.getMessage("non.unique.jobId", new String[]{user.getJobId()}, Locale.getDefault()));
            result.addError(jobIdError);
            List<UserProfile> userProfileList = userProfileService.findDownAll(upId);
            model.addAttribute("profile", userProfileList);
            return "registration";
        }

        if(!user.getPassword().equals(user.getRetypePassword() ) ) {
            FieldError pwdError =new FieldError("user","jobId",messageSource.getMessage("valid.passwordConfDiff", new String[]{user.getJobId()}, Locale.getDefault()));
            result.addError(pwdError);
            List<UserProfile> userProfileList = userProfileService.findDownAll(upId);
            model.addAttribute("profile", userProfileList);
            return "registration";
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
        List<ProductIns> targetProductInsList = new ArrayList<>();
        String loginUserJobId = getPrincipal();
        User loginUser = userService.findByJobId(loginUserJobId);
        if (loginUser.getUserProfile().getType().equals("ADMIN")) {
            targetProductInsList.addAll(productInsList);
        } else {
            List<String> jobIdList = userService.findAllDownJobId(loginUserJobId);
            for (int i = 0; i < jobIdList.size(); i++) {
                for (int j = 0; j < productInsList.size(); j++) {
                    if (productInsList.get(j).getEmployeeId().equals(jobIdList.get(i))) {
                        targetProductInsList.add(productInsList.get(j));
                    }
                }
            }
        }

        model.addAttribute("productInsList", targetProductInsList);
        model.addAttribute("loginUser", loginUserJobId);
        return "listProductCar";
    }

    @RequestMapping(value = "/export-product-car", method = RequestMethod.GET)
    public ModelAndView getExcel(ModelMap model) {
        List<ProductIns> productInsList = productInsService.findByType("car");
        List<String> jobIdList = userService.findAllDownJobId(getPrincipal());
        List<ProductIns> targetProductInsList = new ArrayList<>();
        for (int i = 0; i < jobIdList.size(); i++) {
            for (int j = 0; j < productInsList.size(); j++) {
                if (productInsList.get(j).getEmployeeId().equals(jobIdList.get(i))) {
                    targetProductInsList.add(productInsList.get(j));
                }
            }
        }
        model.addAttribute("productInsList", targetProductInsList);
//        return new ModelAndView(new ProductXlsView(), model);
        return new ModelAndView(new ProductXlsxView(), model);
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

    @RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
    public String accessDeniedPage(ModelMap model) {
        model.addAttribute("loginUser", getPrincipal());
        return "accessDenied";
    }

    private String getPrincipal() {
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

    @ModelAttribute("roles")
    public List<UserProfile> initializeProfiles() {
        return userProfileService.findAll();
    }

    @RequestMapping(value="/singleUpload", method = RequestMethod.GET)
    public String getSingleUploadPage(ModelMap model) {
        FileBucket fileModel = new FileBucket();
        model.addAttribute("fileBucket", fileModel);
        return "upload";
    }

//    private static String UPLOAD_LOCATION="C:/mytemp/";

    @RequestMapping(value="/singleUpload", method = RequestMethod.POST)
    public String singleFileUpload(@Valid FileBucket fileBucket, BindingResult result, ModelMap model) throws IOException {

        if (result.hasErrors()) {
            System.out.println("validation errors");
            return "upload";
        } else {
            System.out.println("Fetching file");
            MultipartFile multipartFile = fileBucket.getFile();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(multipartFile.getBytes());
            Workbook workbook;
            if (multipartFile.getOriginalFilename().endsWith("xls")) {
                workbook = new HSSFWorkbook(byteArrayInputStream);
            } else if (multipartFile.getOriginalFilename().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(byteArrayInputStream);
            } else {
                return "upload";
            }

            Sheet sheet = null;
            Row row = null;
            Cell cell = null;

            List<List<Object>> list = new ArrayList<List<Object>>();
            //遍历Excel中所有的sheet
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                sheet = workbook.getSheetAt(i);
                if(sheet==null){continue;}

                //遍历当前sheet中的所有行
                for (int j = sheet.getFirstRowNum(); j < sheet.getLastRowNum(); j++) {
                    row = sheet.getRow(j);
                    if(row==null||row.getFirstCellNum()==j){continue;}

                    //遍历所有的列
                    List<Object> li = new ArrayList<Object>();
                    for (int y = row.getFirstCellNum() + 1 ; y < row.getLastCellNum(); y++) {
                        cell = row.getCell(y);
                        li.add(getCellValue(cell));
                    }
                    list.add(li);
                }
            }
            workbook.close();

            // TODO add convert
            for (int j = 0; j < list.size(); j++) {
                for (int k = 0; k < list.get(j).size(); k++) {
                    ProductIns productIns = new ProductIns();
                }
            }



            //Now do something with file...
            FileCopyUtils.copy(fileBucket.getFile().getBytes(), new File("C:/mytemp/" + fileBucket.getFile().getOriginalFilename()));

            String fileName = multipartFile.getOriginalFilename();
            model.addAttribute("fileName", fileName);
            return "success";
        }
    }

    public  Object getCellValue(Cell cell){
        Object value = null;
        DecimalFormat df = new DecimalFormat("0");  //格式化number String字符
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");  //日期格式化
        DecimalFormat df2 = new DecimalFormat("0.00");  //格式化数字

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if("General".equals(cell.getCellStyle().getDataFormatString())){
                    value = df.format(cell.getNumericCellValue());
                }else if("m/d/yy".equals(cell.getCellStyle().getDataFormatString())){
                    value = sdf.format(cell.getDateCellValue());
                }else{
                    value = df2.format(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_BLANK:
                value = "";
                break;
            default:
                break;
        }
        return value;
    }


}
