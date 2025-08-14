package com.wikigroup.controllers;

import com.wikigroup.models.Contacto;
import com.wikigroup.repositories.ContactoRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
            ra.addFlashAttribute("org.springframework.validation.BindingResult.contacto", br);
            ra.addFlashAttribute("contacto", contacto);
            ra.addFlashAttribute("errorMsg", "Revisa los campos en rojo.");
            return "redirect:/contacto";
        }
        repo.save(contacto);
        ra.addFlashAttribute("okMsg", "¡Mensaje enviado correctamente!");
        return "redirect:/contacto";
    }

    // --- Listado simple (opcional) ---
    @GetMapping("/contacto/admin")
    public String admin(Model model) {
        model.addAttribute("pageTitle", "Contactos");
        model.addAttribute("items", repo.findAll());
        return "contacto_admin";
    }

    // --- Listado “bonito”, ordenado desc por fecha ---
    @GetMapping("/mensajes")
    public String mensajes(Model model, @RequestParam(value="okMsg", required=false) String okMsg) {
        model.addAttribute("pageTitle", "Mensajes recibidos");
        model.addAttribute("items", repo.findAll(Sort.by(Sort.Direction.DESC, "createdAt")));
        if (okMsg != null) model.addAttribute("okMsg", okMsg);
        return "mensajes";
    }

    // --- Eliminar por id ---
    @PostMapping("/mensajes/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            ra.addFlashAttribute("okMsg", "Mensaje #" + id + " eliminado.");
        } else {
            ra.addFlashAttribute("errorMsg", "El mensaje #" + id + " no existe.");
        }
        return "redirect:/mensajes";
    }
}
