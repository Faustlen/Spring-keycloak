
package com.mentoring.keycloak;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DemoController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return " a public endpoint accessible by anyone.";
    }

    @GetMapping("/protected")
    public String protectedEndpoint() {
        return "This is a protected endpoint accessible only by authenticated users.";
    }

    @GetMapping("/employee")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String employeeEndpoint() {
        return "endpoint accessible by employee";
    }

    @GetMapping("/manager")
    @PreAuthorize("hasRole('MANAGER')")
    public String managerEndpoint() {
        return "endpoint accessible by manager";
    }

    @GetMapping("/director")
    @PreAuthorize("hasRole('DIRECTOR')")
    public String directorEndpoint() {
        return "endpoint accessible by director";
    }
}
