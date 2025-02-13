package edu.escuelaing.arep.taller3.controller;

import edu.escuelaing.arep.taller3.server.annotations.GetMapping;
import edu.escuelaing.arep.taller3.server.annotations.RequestParam;
import edu.escuelaing.arep.taller3.server.annotations.RestController;

@RestController
public class GretingController {

    @GetMapping("/spring/hello")
    public static String greeting(@RequestParam(value = "name", defaultValue = "world")String name) {
        return "Hello " + name + " !";
    }
}
