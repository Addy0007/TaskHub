package com.aithinkers.TaskHub.Service;


import com.aithinkers.TaskHub.Entity.User;
import com.aithinkers.TaskHub.Enum.Role;
import com.aithinkers.TaskHub.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    /* we are using private final here as Construction Injection
     * since Spring 4.3 if a class has only one constructor ,spring will autmatically use it for
     * dependency injection even without @Autowired
     * Here Lambok's @requriedArgsconstructor generates that constructor for us
     * autowired best demo projects and private final lomboks constructor is good for production  */
    private final UserRepository userRepo;

    public List<User> findAll(){
        return userRepo.findAll();
    }
    //here we are fetching user by email
    //if not found it throws and illegal argument like user not found by thr given email
    public User findByEmail(String email){
        return userRepo.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("USer Not Found : "+ email));
    }

    /*Here this ensure method ensure that MAKE SURE  USER EXISTS.IF NOT ,CREATE ONe
    * if  a user with that does not exist -> create a new one with the given name and role
    * and save it and return it.
    * ->HOW IT WORKS
    * users.findByEmail(email)
		Calls the repository method you defined earlier.
		Returns Optional<User>.
		->.orElseGet(() -> ...)
		If the Optional<User> is present → return that User.
		->If not present → execute the lambda()
		* This was the Lambda ->save(User.builder()
                        .email(email)
                        .name(name)
                        .role(role)
                        .build())
		 */

    public User ensure(String email, String name, Role role) {
        return userRepo.findByEmail(email).orElseGet(() ->
                userRepo.save(User.builder()
                        .email(email)
                        .name(name)
                        .role(role)
                        .build())
        );
    }
    //builder() a lombark function
    //that generates static builder,you can construct objects clean and simple like above
    //or we need use EX:USer U=new User();->u.setEmail(email)etc
 }
