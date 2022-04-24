package com.example.demo.controllers;

import java.io.IOException;
import java.util.Optional;

import com.splunk.TcpInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/user")
public class UserController {
	private Logger log = LoggerFactory.getLogger(UserController.class);

//	@Autowired
//	private TcpInput tcpInput;

	public UserController(UserRepository userRepository, CartRepository cartRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.cartRepository = cartRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
//		try{
//			tcpInput.submit("INFO: Searching user by username ");
//		} catch(Exception e){
//			System.out.println(e.getLocalizedMessage());
//		}

		log.info("Searching user by username '{}'", username);
		User user = userRepository.findByUsername(username);

		if (user == null) {
//			try{
//				tcpInput.submit("ERROR: User could not be found");
//			} catch(Exception e){
//				System.out.println(e.getLocalizedMessage());
//			}
			log.error("User with username '{}' could not be found", username);
			return ResponseEntity.notFound().build();
		}

//		try{
//			tcpInput.submit("INFO: User found successfully");
//		} catch(Exception e){
//			System.out.println(e.getLocalizedMessage());
//		}
		log.info("User with username '{}' found successfully", username);
		return ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		String password = createUserRequest.getPassword();
		if (password == null || password.length() < 7 || !password.equals(createUserRequest.getConfirmPassword())) {
			log.error("Invalid password. Failed to create user '{}'", user.getUsername());
			return ResponseEntity.badRequest().build();
		}

		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
		userRepository.save(user);
		log.info("User '{}' was successfully created", user.getUsername());
		return ResponseEntity.ok(user);
	}
	
}
