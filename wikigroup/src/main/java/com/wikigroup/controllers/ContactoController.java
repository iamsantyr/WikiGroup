package com.wikigroup.controllers;

import com.wikigroup.models.Contacto;
import com.wikigroup.repositories.ContactoRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactoController {

    private final ContactoRepository repo;

    public ContactoController(ContactoRepository repo) {
        this.repo = repo;
    }

    // --- Mostrar formulario de contacto ---
    @GetMapping("/contacto")
    public String form(Model model) {
        model.addAttribute("pageTitle", "Contáctenos");
        if (!model.containsAttribute("contacto")) {
            model.addAttribute("contacto", new Contacto());
        }
        return "contacto";
    }

    // --- Recibir envío del formulario ---
    @PostMapping("/contacto")
    public String submit(@Valid @ModelAttribute("contacto") Contacto contacto,
                         BindingResult br,
                         RedirectAttributes ra) {
        if (br.hasErrors()) {
            // Mantener errores y datos al redirigir
            ra.addFlashAttribute("org.springframework.validation.BindingResult.contacto", br);
            ra.addFlashAttribute("contacto", contacto);
            ra.addFlashAttribute("errorMsg", "Revisa los campos en rojo.");
            return "redirect:/contacto";
        }
        repo.save(contacto);
        ra.addFlashAttribute("okMsg", "¡Mensaje enviado correctamente!");
        return "redirect:/contacto";
    }

    // --- Vista simple de administración/listado ---
    @GetMapping("/contacto/admin")
    public String admin(Model model) {
        model.addAttribute("pageTitle", "Contactos");
        model.addAttribute("items", repo.findAll()); // luego puedes paginar/ordenar
        return "contacto_admin";
    }
}
