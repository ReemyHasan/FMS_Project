package com.example.userManagement.services;


import com.example.userManagement.entity.Role;
import com.example.userManagement.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    private final Logger log = LoggerFactory.getLogger(RoleService.class);
    public Iterable<Role> getRoles() {

        try{
            return roleRepository.findAll();
        } catch (Exception e) {
            log.error("Error getting data from Elastic", e);
            return null;
        }
    }
    public Role getRoleById(int id){
        try {
            return roleRepository.findById(id).orElse(null);
        } catch (Exception e) {
            log.error("Error getting data from Elastic", e);
            return null;
        }
    }
    public Role insertRole(Role  role) {
        try {
            roleRepository.save(role);

            log.info("Data saved successfully in Elastic");
            return role;
        }
        catch (Exception e) {
            log.error("Error saving data in Elastic", e);
        }
        return null;
    }

    public Role updateRole(Role role, int id) {
        Role role1  = roleRepository.findById(id).get();
        role1.setRole(role.getRole());
        return role1;
    }

    public boolean deleteRole(int id ) {
        try {

            roleRepository.deleteById(id);

            log.info("Role deleted successfully in Elastic");
            return true;
        }
        catch (Exception e) {
            log.error("Error  deleting role in Elastic", e);
        }
        return false;
    }
}
