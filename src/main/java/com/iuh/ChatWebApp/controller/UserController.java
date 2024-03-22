package com.iuh.ChatWebApp.controller;

import java.io.Console;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iuh.ChatWebApp.Service.ChatRoomServiceImpl;
import com.iuh.ChatWebApp.Service.FriendServiceImpl;
import com.iuh.ChatWebApp.Service.UserServiceImpl;
import com.iuh.ChatWebApp.entity.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private FriendServiceImpl friendService;
	
	 @Autowired
	    private ChatRoomServiceImpl chatRoomService;
	
	@GetMapping("/")
    private String SignIn(HttpSession session) {
        // Kiểm tra xem người dùng đã đăng nhập và isOnline là true không
        if (session.getAttribute("loggedInUser") != null) {
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            if (loggedInUser.isOnline()) {
                // Nếu isOnline là true, chuyển hướng đến trang chính
                return "redirect:/showFormHome";
            }
        }
        return "SignIn";
    }

    @GetMapping("/showFormSignUp")
    private String SignUp(HttpSession session) {
        // Kiểm tra xem người dùng đã đăng nhập và isOnline là true không
        if (session.getAttribute("loggedInUser") != null) {
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            if (loggedInUser.isOnline()) {
                // Nếu isOnline là true, chuyển hướng đến trang chính
                return "redirect:/showFormHome";
            }
        }
        return "SignUp";
    }
	
    @GetMapping("/showFormHome")
    private String HomePage(HttpSession session, Model model) {
        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        if (session.getAttribute("loggedInUser") == null) {
            // Nếu chưa đăng nhập, chuyển hướng đến trang đăng nhập
            return "redirect:/";
        }
        // Lấy thông tin người dùng đã đăng nhập từ session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        // Lấy danh sách bạn bè của người dùng đã đăng nhập
        List<User> friendList = friendService.getFriendListByPhoneNumber(loggedInUser.getPhoneNumber());  
        
        // Đặt danh sách bạn bè vào model để sử dụng trong template
        model.addAttribute("friendList", friendList);
        
        return "HomePage";
    }
    
    @GetMapping("/showHomeEdit")
    private String EditPage(HttpSession session, Model model) {
        // Lấy thông tin người dùng đã đăng nhập từ session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        if (loggedInUser == null) {
            // Nếu chưa đăng nhập, chuyển hướng đến trang đăng nhập
            return "redirect:/";
        }
        
        // Đặt thông tin người dùng vào model để hiển thị trong trang "EditProfile"
        model.addAttribute("loggedInUser", loggedInUser);
        
        // Nếu đã đăng nhập, cho phép truy cập vào trang "EditProfile"
        return "EditProfile";
    }
  
	@PostMapping("/save")
	public String saveUser(@ModelAttribute @Validated User user, BindingResult bindingResult,
	                       HttpSession session, Model model) {
	
	    // Kiểm tra xem số điện thoại đã tồn tại trong hệ thống hay chưa
	    String messageFail = userService.findByPhoneNumberExist(user.getPhoneNumber());
	    session.setAttribute("messageFail", messageFail);
	    if (messageFail.equals("Bị trùng số điện thoại")) {
	        return "SignUp";
	    }else {
	    	if (user.getGender().equalsIgnoreCase("male")) {
	            user.setAvatar("nam.jpg");
	        } else if (user.getGender().equalsIgnoreCase("female")) {
	            user.setAvatar("nu.jpg");
	        }
	    	
	    	// Nếu số điện thoại chưa tồn tại, lưu người dùng mới và thông báo thành công
		    String message = userService.saveUser(user);
		    session.setAttribute("message", message);
		    
		    

		    if (message.equals("Đăng ký thành công")) {
		        return "redirect:/";
		    } else {
		        // Nếu có lỗi, chuyển hướng về trang đăng ký và giữ lại thông tin đã nhập
		        model.addAttribute("user", user);
		        return "SignUp";
		    }
	    }

	    
	}


  

	@PostMapping("/doLogin")
	public String doLogin(@RequestParam("phoneNumber") String phoneNumber,
	                      @RequestParam("password") String password,
	                      HttpSession session, Model model) {

	    // Kiểm tra xem người dùng có tồn tại trong hệ thống hay không
	    User user = userService.findByPhoneNumberAndPassword(phoneNumber, password);
	    
	    if (user != null) {
	    	user.setOnline(true);
	        userService.saveUser(user);
	        // Nếu đăng nhập thành công, lưu thông tin người dùng vào session và chuyển hướng đến trang chính
	        session.setAttribute("loggedInUser", user);
	        System.out.print(user);
	        return "redirect:/showFormHome";
	    } else {
	        // Nếu không tồn tại người dùng hoặc thông tin không đúng, hiển thị thông báo lỗi và chuyển hướng đến trang đăng nhập
	        model.addAttribute("error", "Số điện thoại hoặc mật khẩu không đúng");
	        return "redirect:/";
	    }
	}
	
	@GetMapping("/logout")
    public String logout(HttpSession session) {
        // Lấy người dùng đã đăng nhập từ session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            // Đặt thuộc tính isOnline về false trước khi đăng xuất
            loggedInUser.setOnline(false);
            // Lưu lại thông tin người dùng sau khi cập nhật
            userService.saveUser(loggedInUser);
        }
        // Xóa thuộc tính đã đặt trong session khi người dùng đăng xuất
        session.removeAttribute("loggedInUser");
        return "redirect:/";
    }
	
	@PostMapping("/update")
	public String updateUser(@ModelAttribute @Validated User updatedUser, BindingResult bindingResult,
			 HttpSession session, Model model) {
	    // Lấy thông tin người dùng đã đăng nhập từ session
	    User loggedInUser = (User) session.getAttribute("loggedInUser");

	    // Kiểm tra xem người dùng đã đăng nhập hay chưa
	    if (loggedInUser == null) {
	        // Nếu chưa đăng nhập, chuyển hướng đến trang đăng nhập
	        return "redirect:/";
	    }

	    // Kiểm tra tính hợp lệ của thông tin người dùng chỉnh sửa
	    if (bindingResult.hasErrors()) {
	        // Nếu có lỗi, chuyển hướng về trang chỉnh sửa và giữ lại thông tin đã nhập
	        model.addAttribute("loggedInUser", loggedInUser);
	        return "EditProfile";
	    }
	    
	 // Cập nhật thông tin người dùng từ dữ liệu mới được gửi từ form
	    if(updatedUser.getFullName()== "") {
	    	 loggedInUser.setGender(updatedUser.getGender());
		 	    loggedInUser.setDob(updatedUser.getDob());
	    }else {
	    	 loggedInUser.setFullName(updatedUser.getFullName());
	 	    loggedInUser.setGender(updatedUser.getGender());
	 	    loggedInUser.setDob(updatedUser.getDob());
	    }

	    
	    userService.saveUser(loggedInUser);
	    // Chuyển hướng về trang chính sau khi cập nhật thành công
	    return "redirect:/showFormHome";
	}


	
	// UserController.java

	@GetMapping("/search")
	public String searchUsers(@RequestParam("searchText") String searchText, Model model, HttpSession session) {
	    // Lấy người dùng đã đăng nhập từ session
	    User loggedInUser = (User) session.getAttribute("loggedInUser");

	    // Kiểm tra xem người dùng đã đăng nhập hay chưa
	    if (loggedInUser == null) {
	        // Nếu chưa đăng nhập, chuyển hướng đến trang đăng nhập
	        return "redirect:/";
	    }

	    // Thực hiện tìm kiếm người dùng với searchText
	    List<User> foundUsers = userService.searchUsers(searchText, loggedInUser.getPhoneNumber());

	    // Thêm danh sách người dùng tìm được vào model để hiển thị trên trang
	    model.addAttribute("foundUsers", foundUsers);

	    // Trả về view hiển thị danh sách người dùng tìm được
	    return "SearchResults";
	}

	

	

}
