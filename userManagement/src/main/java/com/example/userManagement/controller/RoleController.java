package com.example.userManagement.controller;

import com.example.userManagement.entity.Role;
import com.example.userManagement.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
//@CrossOrigin(origins = {"http://localhost:5173"})
//@CrossOrigin(origins = {"*"})
@RequestMapping("/api/roles")
public class RoleController {


    @Autowired
    private RoleService roleService;

    @GetMapping("/")
    Iterable<Role> findAll(){
        return roleService.getRoles();

    }

    @PostMapping("/insertRole")
    public Role insertRole(@RequestBody final Role role){

        return roleService.insertRole(role);
    }

    @PutMapping("/updateRole/{id}")
    public Role updateRole(@RequestBody final Role role,@PathVariable int id){

        return roleService.updateRole(role,id);
    }

    @DeleteMapping("/{id}")
    public boolean deleteRole(@PathVariable int id) {
        return roleService.deleteRole(id);
    }

}
