package com.iuh.ChatWebApp.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.ChatWebApp.Service.UserServiceImpl;
import com.iuh.ChatWebApp.entity.User;
import com.iuh.ChatWebApp.entity.dto.respone.LogoutRespone;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

	@Autowired
	private UserServiceImpl userService;

	@PostMapping("/loginM")
	public ResponseEntity<?> loginMobile(@RequestParam("phoneNumber") String phoneNumber,
			@RequestParam("password") String password, HttpSession session) {
		User user = userService.findByPhoneNumberAndPassword(phoneNumber, password);

		if (user != null) {
			user.setOnline(true);
			userService.saveUser(user);
			session.setAttribute("loggedInUser", user);
			return ResponseEntity.status(HttpStatus.OK).body(user);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Phone number or password is incorrect");
		}
	}

	@PostMapping("/saveM")
	public ResponseEntity<?> saveUserM(@RequestBody @Validated User user, BindingResult bindingResult) {
		// Kiểm tra xem số điện thoại đã tồn tại trong hệ thống hay chưa
		String messageFail = userService.findByPhoneNumberExist(user.getPhoneNumber());
		if (messageFail.equals("Bị trùng số điện thoại")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bị trùng số điện thoại");
		} else {
			if (user.getGender().equalsIgnoreCase("male")) {
				user.setAvatar("nam.jpg");
			} else if (user.getGender().equalsIgnoreCase("female")) {
				user.setAvatar("nu.jpg");
			}

			// Nếu số điện thoại chưa tồn tại, lưu người dùng mới và thông báo thành công
			String message = userService.saveUser(user);

			if (message.equals("Đăng ký thành công")) {
				return ResponseEntity.status(HttpStatus.OK).body("Đăng ký thành công");
			} else {
				// Nếu có lỗi, trả về thông báo lỗi
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra khi đăng ký");
			}
		}
	}

	@PostMapping("/logoutM")
	public ResponseEntity<LogoutRespone> logoutM(HttpSession session) {
	    User loggedInUser = (User) session.getAttribute("loggedInUser");
	    LogoutRespone response;
	    if (loggedInUser != null) {
	        loggedInUser.setOnline(false);
	        userService.saveUser(loggedInUser);
	        session.removeAttribute("loggedInUser");
	        response = new LogoutRespone("Logout thành công");
	        return ResponseEntity.ok(response);
	    } else {
	        response = new LogoutRespone("Chưa đăng nhập");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }
	}



	@PostMapping("/updateM")
	public ResponseEntity<?> updateUserM(@RequestBody @Validated User updatedUser, BindingResult bindingResult,
			HttpSession session) {
		User loggedInUser = (User) session.getAttribute("loggedInUser");

		if (loggedInUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");
		}

		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body("Invalid user data");
		}

		if (!updatedUser.getPassword().isEmpty()) {
			loggedInUser.setPassword(updatedUser.getPassword());
		}

		if (!updatedUser.getFullName().isEmpty()) {
			loggedInUser.setFullName(updatedUser.getFullName());
		}

		if (!updatedUser.getGender().isEmpty()) {
			loggedInUser.setGender(updatedUser.getGender());
		}

		if (updatedUser.getDob() != null) {
			loggedInUser.setDob(updatedUser.getDob());
		}

		userService.saveUser(loggedInUser);

		return ResponseEntity.ok("Cập nhật thành công");
	}

	@PostMapping("/updateForgotM")
	public ResponseEntity<?> updateForgotPasswordM(@RequestParam("phoneNumber") String phoneNumber,
			@RequestParam("password") String password) {
		User user = userService.findUserByPhoneNumber(phoneNumber);

		if (user != null) {
			user.setPassword(password);
			userService.saveUser(user);
			return ResponseEntity.ok("Cấp mật khẩu mới thành công");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}
	}
	
	@GetMapping("/searchM")
	public ResponseEntity<List<User>> searchUsersM(@RequestParam("searchText") String searchText,
			HttpSession session) {
		User loggedInUser = (User) session.getAttribute("loggedInUser");

		if (loggedInUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		List<User> foundUsers = userService.searchUsers(searchText, loggedInUser.getPhoneNumber());
		return ResponseEntity.ok(foundUsers);
	}

}
