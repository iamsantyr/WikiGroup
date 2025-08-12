package com.wikigroup.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WikiController {

  // Página de inicio
  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("pageTitle", "Inicio");
    model.addAttribute("mensaje", "Hola, esta es la wiki del equipo.");
    return "index";
  }

  // Presentación del equipo
  @GetMapping("/equipo")
  public String equipo(Model model) {
    model.addAttribute("pageTitle", "Equipo");
    return "equipo";
  }

  // Descripción del proyecto
  @GetMapping("/proyecto")
  public String proyecto(Model model) {
    model.addAttribute("pageTitle", "Proyecto");
    return "proyecto";
  }

  // Requerimientos
  @GetMapping("/requerimientos")
  public String requerimientos(Model model) {
    model.addAttribute("pageTitle", "Requerimientos");
    return "requerimientos";
  }

  // Arquitectura
  @GetMapping("/arquitectura")
  public String arquitectura(Model model) {
    model.addAttribute("pageTitle", "Arquitectura");
    return "arquitectura";
  }

  // Despliegue
  @GetMapping("/despliegue")
  public String despliegue(Model model) {
    model.addAttribute("pageTitle", "Despliegue");
    return "despliegue";
  }

  // OJO: aquí ya NO hay @GetMapping("/contacto") 
  // porque esa ruta la manejará ContactoController.
}
