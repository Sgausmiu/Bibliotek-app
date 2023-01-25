package ru.sennikov.incourse.controllers;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/first")
public class FirstController {
    @GetMapping("/hello")
    public String helloPage(@RequestParam(value="name", required = false) String name,
                            @RequestParam(value="surname", required = false) String surname,
                            Model model) {
        System.out.println("Hi " + name +" "+ surname);
        model.addAttribute("message", "Hi " + name +" "+ surname);
        return "first/hello";

    }
    @GetMapping("/calculator")
    public String calculator(@RequestParam("a") int a,
                          @RequestParam("b") int b,
                          @NotNull @RequestParam("action") String action,
                          Model model) {
        double itog;

        switch (action) {
            case "multiplication":
                itog = a * b;
                break;
            case "addition":
                itog = a + b;
                break;
            case "subtraction":
                itog = a - b;
                break;
            case "division":
                itog = a / (double) b;
                break;
            default:
                itog=0;
                break;
        }

        model.addAttribute("itog", itog);

        return "first/calculator";

    }
    @GetMapping("/bye")
    public String byePage() {

        return "first/bye";
    }

}
