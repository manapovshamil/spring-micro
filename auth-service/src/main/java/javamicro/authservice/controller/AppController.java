package javamicro.authservice.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app")
public class AppController {

    @GetMapping("/all")
    public String allAccess() {
        return "All access";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String adminAccess() {
        return "Admin access";
    }

    @GetMapping("/manager")
    @PreAuthorize("hasAnyRole('MANAGER')")
    public String managerAccess() {
        return "Manager access";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER') or hasAnyRole('ADMIN') or hasAnyRole('MANAGER')")
    public String userAccess() {
        return "User access";
    }
}
