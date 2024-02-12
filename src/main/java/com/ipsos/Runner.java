package com.ipsos;

import com.ipsos.entities.dtos.UserDto;
import com.ipsos.services.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;


public class Runner implements ApplicationRunner {

    private final UserService userService;

    public Runner(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        createTestUser();
    }

//    public void createTestUser() {
//        Scanner scanner = new Scanner(System.in);
//
//        String username = scanner.nextLine();
//        String password = scanner.nextLine();
//
//        userService.createUser(username, password);
//
//        System.out.println("Added user to the DB");
//    }
}
