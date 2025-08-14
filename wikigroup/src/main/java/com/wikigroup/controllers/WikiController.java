package com.wikigroup.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WikiController {

  // Redirige el inicio a /proyecto (puedes cambiar a /equipo si prefieres)
  @GetMapping("/")
  public String root() {
    return "redirect:/proyecto";
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

  // Requerimientos (funcionales y no funcionales)
  @GetMapping("/requerimientos")
  public String requerimientos(Model model) {
    model.addAttribute("pageTitle", "Requerimientos");
    return "requerimientos";
  }

  // Arquitectura (C4, ER, despliegue)
  @GetMapping("/arquitectura")
  public String arquitectura(Model model) {
    model.addAttribute("pageTitle", "Arquitectura");
    return "arquitectura";
  }

  // Despliegue (versiones, pipeline, apache)
  @GetMapping("/despliegue")
  public String despliegue(Model model) {
    model.addAttribute("pageTitle", "Despliegue");
    return "despliegue";
  }

  // Para desarrollo (descripciones de endpoints)
  @GetMapping("/desarrollo")
  public String desarrollo(Model model) {
    model.addAttribute("pageTitle", "Para desarrollo");
    return "desarrollo";
  }

  // Pruebas (frontend y backend)
  @GetMapping("/pruebas")
  public String pruebas(Model model) {
    model.addAttribute("pageTitle", "Pruebas");
    return "pruebas";
  }
}
