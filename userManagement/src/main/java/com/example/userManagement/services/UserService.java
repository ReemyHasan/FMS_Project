package com.example.userManagement.services;


import com.example.userManagement.entity.User;
import com.example.userManagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;
    private final Logger log = LoggerFactory.getLogger(UserService.class);
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    public void initUser() {
//
//        User adminUser = new User();
//        adminUser.setUsername("reem1");
//        adminUser.setRole("admin");
//        adminUser.setEmail("reem1@123.com");
//        adminUser.setEnabled(true);
//        adminUser.setPassword(passwordEncoder.encode("admin@pass"));
//        userRepository.save(adminUser);
//
//    }
    public Iterable<User> getUsers() {

        try{
            return userRepository.findAll();
        } catch (Exception e) {
            log.error("Error getting data from Elastic", e);
            return null;
        }
    }
    public User getUserById(int id){
        try{
            return userRepository.findById(id).orElse(null);
        } catch (Exception e) {
            log.error("Error getting data from Elastic", e);
            return null;
        }
    }
    public User insertUser(final User user) {
        try {
            userRepository.save(user);

            log.info("Data saved successfully in Elastic");
            return user;
        } catch (Exception e) {
            log.error("Error saving data in Elastic", e);
        }
        return null;
    }
    public User updateUser(User user, int id) {
        try {
            User user1 = userRepository.findById(id).get();
            user1.setUsername(user.getUsername());
            user1.setEmail(user.getEmail());
            user1.setPassword(
//                passwordEncode.encode(
                    user.getPassword()
//                )
            );
            user1.setRole(user.getRole());
            log.info("Data Updated successfully in Elastic");
            return user1;
        } catch (Exception e) {
            log.error("Error Updating data in Elastic", e);
        }
        return null;

    }

    public boolean deleteUser(int id) {
        try {

            userRepository.deleteById(id);

            log.info("User deleted successfully in Elastic");
            return true;
        }
        catch (Exception e) {
            log.error("Error  deleting data in Elastic", e);
        }
        return false;

    }

}
